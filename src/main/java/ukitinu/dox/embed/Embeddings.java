package ukitinu.dox.embed;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackagePartName;
import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import ukitinu.dox.embed.docxml.DocXml;
import ukitinu.dox.embed.docxml.DoxElement;
import ukitinu.dox.embed.docxml.XmlElement;
import ukitinu.dox.embed.docxml.XmlTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

final class Embeddings {
    private Embeddings() {
        throw new IllegalStateException("Utility class");
    }

    private static final String EMB_NAME_START = "_Excel";
    private static final int EMB_NAME_START_LEN = EMB_NAME_START.length();
    private static final String EMB_NAME_END = ".";
    private static final XmlTag OLE_OBJ_TAG = new XmlTag("<o:OLEObject>");
    private static final String OLE_OBJ_PROG_ID = "ProgID";

    static List<String> getEmbeddingNames(XWPFDocument doc) throws OpenXML4JException {
        List<PackagePart> embeddings = doc.getAllEmbeddedParts();
        return embeddings.stream()
                .map(PackagePart::getPartName)
                .map(PackagePartName::getName)
                .sorted((o1, o2) -> {
                    int n1 = getEmbeddingNumber(o1);
                    int n2 = getEmbeddingNumber(o2);
                    return Integer.compare(n1, n2);
                })
                .collect(Collectors.toList());
    }

    static Map<Integer, Embedding> buildEmbeddingMap(XWPFDocument doc, DocXml docXml) throws OpenXML4JException {
        Map<Integer, Embedding> map = new HashMap<>();

        List<XmlElement> paragraphs = docXml.getParagraphs();
        List<String> names = getEmbeddingNames(doc);

        int count = 0;
        for (int i : docXml.getEmbeddingIndices()) {
            XmlElement par = paragraphs.get(i);
            String progId = par.getInnerElement(OLE_OBJ_TAG).getAttrValue(OLE_OBJ_PROG_ID);
            map.put(i, new Embedding(i, progId, names.get(count)));
            count++;
        }
        return map;
    }

    private static int getEmbeddingNumber(String name) {
        int start = name.indexOf(EMB_NAME_START);
        int end = name.indexOf(EMB_NAME_END);
        return Integer.parseInt(name.substring(start + EMB_NAME_START_LEN, end));
    }

    static List<DoxElement> createDoxElements(List<IBodyElement> elements, Map<Integer, Embedding> embeddings) {
        List<DoxElement> list = new ArrayList<>(elements.size());

        int parIndex = 0;
        for (IBodyElement element : elements) {
            String embeddingName = null;
            if (element.getElementType() == BodyElementType.PARAGRAPH) {
                if (embeddings.containsKey(parIndex)) embeddingName = embeddings.get(parIndex).getName();
                parIndex++;
            }
            list.add(new DoxElement(element, embeddingName));
        }

        return list;
    }

}

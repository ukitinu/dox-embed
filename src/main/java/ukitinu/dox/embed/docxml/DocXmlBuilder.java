package ukitinu.dox.embed.docxml;

import org.apache.commons.codec.Charsets;
import org.apache.commons.io.FileUtils;
import ukitinu.dox.embed.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class DocXmlBuilder {
    private DocXmlBuilder() {
        throw new IllegalStateException("Not instantiable");
    }

    private static final String DOCUMENT_XML_PATH = File.separator + "word" + File.separator + "document.xml";
    private static final String PARAGRAPH_TAG = "<w:p>";
    private static final String TABLE_TAG = "<w:tbl>";
    private static final String TEXTBOX_TAG = "<v:textbox>";

    public static DocXml build(String docPath, String zipDir, String zipName) throws IOException {
        unpackDocxArchive(docPath, zipDir, zipName);
        String contentXml = extractXmlString(zipDir);
        List<XmlElement> tables = XmlExplorer.findTag(contentXml, TABLE_TAG);
        List<XmlElement> paragraphs = extractParagraphs(contentXml);
        return new DocXml(contentXml, paragraphs, tables);
    }

    private static void unpackDocxArchive(String docPath, String targetDir, String zipName) throws IOException {
        String zipPath = Utils.convertToZip(docPath, targetDir, zipName);
        Utils.extractZip(zipPath, targetDir);
    }

    private static String extractXmlString(String targetDir) throws IOException {
        File docXml = new File(targetDir + DOCUMENT_XML_PATH);
        return FileUtils.readFileToString(docXml, Charsets.UTF_8);
    }

    private static List<XmlElement> extractParagraphs(String contentXml) {
        String textContent = XmlExplorer.removeTag(contentXml, TABLE_TAG);
        textContent = XmlExplorer.removeTag(textContent, TEXTBOX_TAG);
        return XmlExplorer.findTag(textContent, PARAGRAPH_TAG);
    }
}

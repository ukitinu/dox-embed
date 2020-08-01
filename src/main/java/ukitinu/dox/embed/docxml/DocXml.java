package ukitinu.dox.embed.docxml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class DocXml {

    private final String contentXml;
    private final List<XmlElement> paragraphs;
    private final List<XmlElement> tables;

    DocXml(String contentXml, List<XmlElement> paragraphs, List<XmlElement> tables) {
        this.contentXml = contentXml;
        this.tables = Collections.unmodifiableList(tables);
        this.paragraphs = Collections.unmodifiableList(paragraphs);
    }

    //endregion

    public String getContentXml() {
        return contentXml;
    }

    public List<XmlElement> getParagraphs() {
        return paragraphs;
    }

    public List<XmlElement> getTables() {
        return tables;
    }

    public List<String> getTextLines() {
        return paragraphs.stream().map(XmlElement::getText).collect(Collectors.toList());
    }

    public List<EmbeddingLine> getEmbeddingContext() {
        List<EmbeddingLine> list = new ArrayList<>();
        int size = paragraphs.size();

        for (int i = 0; i < size; i++) {
            XmlElement par = paragraphs.get(i);
            if (par.hasEmbeddings()) {
                XmlElement prev = i > 0 ? paragraphs.get(i - 1) : null;
                XmlElement next = i < size - 1 ? paragraphs.get(i + 1) : null;
                list.add(new EmbeddingLine(prev, par, next));
            }
        }
        return list;
    }

    public List<Integer> getEmbeddings() {
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < paragraphs.size(); i++) {
            XmlElement par = paragraphs.get(i);
            if(par.hasEmbeddings()) list.add(i);
        }
        return list;
    }


    public List<XmlElement> findTags(String tag, String... noTags) {
        String content = contentXml;
        if (noTags != null && noTags.length > 0) {
            for (String noTag : noTags) {
                content = XmlExplorer.removeTag(content, noTag);
            }
        }
        return XmlExplorer.findTag(contentXml, tag);
    }
}

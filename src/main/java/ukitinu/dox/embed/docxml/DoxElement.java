package ukitinu.dox.embed.docxml;

import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;

public class DoxElement {
    private final IBodyElement element;
    private final String embeddingName;

    public DoxElement(IBodyElement element, String embeddingName) {
        this.element = element;
        this.embeddingName = embeddingName;
    }

    public IBodyElement getElement() {
        return element;
    }

    public String getEmbeddingName() {
        return embeddingName;
    }

    public boolean isTable() {
        return element.getElementType() == BodyElementType.TABLE;
    }

    public boolean isParagraph() {
        return element.getElementType() == BodyElementType.PARAGRAPH;
    }

    public boolean hasEmbedding() {
        return embeddingName != null;
    }

    public String getText() {
        switch (element.getElementType()) {
            case TABLE:
                return ((XWPFTable) element).getText();
            case PARAGRAPH:
                return ((XWPFParagraph) element).getText();
            case CONTENTCONTROL:
            default:
                return "";
        }
    }

    public XWPFParagraph getParagraph() {
        if(element.getElementType() == BodyElementType.PARAGRAPH) {
            return ((XWPFParagraph) element);
        }
        return null;
    }

    public XWPFTable getTable() {
        if(element.getElementType() == BodyElementType.TABLE) {
            return ((XWPFTable) element);
        }
        return null;
    }

}

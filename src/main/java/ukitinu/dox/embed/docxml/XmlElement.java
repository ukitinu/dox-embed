package ukitinu.dox.embed.docxml;

import ukitinu.dox.embed.utils.Interval;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class XmlElement {
    private static final String TAG_CLOSE = "/>";
    private static final String TEXT_TAG = "<w:t>";
    private static final String EMBEDDING_TAG = "<w:object>";

    private final String fullContent;
    private final String tagContent;
    private final XmlTag tag;
    private final boolean hasInner;
    private final Interval interval;

    public XmlElement(String fullContent, XmlTag tag, int closingIndex, Interval interval) {
        validate(fullContent, tag);

        this.fullContent = fullContent;
        this.interval = interval;
        this.tag = tag;

        this.tagContent = fullContent.substring(0, closingIndex);
        this.hasInner = !fullContent.endsWith(TAG_CLOSE);
    }

    public String getFullContent() {
        return fullContent;
    }

    public String getTagContent() {
        return tagContent;
    }

    public XmlTag getTag() {
        return tag;
    }

    public boolean hasInner() {
        return hasInner;
    }

    public String getText() {
        return XmlExplorer.findTag(getInner(), TEXT_TAG).stream().map(XmlElement::getInner).collect(Collectors.joining(""));
    }

    public Interval getInterval() {
        return interval;
    }

    public String getInner() {
        if (!hasInner) {
            return "";
        } else {
            int s = fullContent.indexOf('>');
            int e = fullContent.lastIndexOf('<');
            return fullContent.substring(s + 1, e);
        }
    }

    public List<XmlElement> getEmbeddings() {
        return XmlExplorer.findTag(getInner(), EMBEDDING_TAG);
    }

    public boolean hasEmbeddings() {
        return !getEmbeddings().isEmpty();
    }

    public XmlElement getInnerElement(XmlTag tag) {
        List<XmlElement> list = XmlExplorer.findTag(getInner(), tag);
        return list.isEmpty() ? null : list.get(0);
    }

    public String getAttrValue(String attr) {
        //TODO Fallisce se " attr=" si trova a sua volta all'interno di un attributo, ad esempio in un caso come
        //TODO anotherAttr="hello attr='spam' world".
        String search = " " + attr + "=";
        int start = tagContent.indexOf(" " + attr + "=");
        if (start == -1) return "";

        start = start + search.length();
        char quote = tagContent.charAt(start);
        int end = tagContent.indexOf(quote, start + 1);

        if (end == -1 || end < start) return "";
        return tagContent.substring(start + 1, end);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof XmlElement)) return false;
        XmlElement xmlElement = (XmlElement) o;
        return fullContent.equals(xmlElement.fullContent) &&
                tag.equals(xmlElement.tag) &&
                interval.equals(xmlElement.interval);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullContent, tag, interval);
    }

    @Override
    public String toString() {
        return String.format("%s::%s::String@%s", interval, tag, Integer.toHexString(fullContent.hashCode()));
    }

    private static void validate(String content, XmlTag tag) {
        Collection<String> errors = new ArrayList<>();
        if (content == null || content.isEmpty()) {
            errors.add("null or empty");
        } else {
            if (!content.startsWith(tag.getOpening())) errors.add("missing opening tag");
            if (!content.endsWith(tag.getClosing()) && !content.endsWith(TAG_CLOSE)) errors.add("missing closing tag");
        }
        if (!errors.isEmpty()) {
            String message = String.format("Tag content errors: %s", String.join("; ", errors));
            throw new IllegalArgumentException(message);
        }
    }
}

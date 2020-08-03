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
    private static final XmlTag EMBEDDING_TAG = new XmlTag("<w:object>");

    private final String fullContent;
    private final String tagContent;
    private final XmlTag tag;
    private final boolean hasInner;
    private final Interval interval;

    public XmlElement(String fullContent, XmlTag tag, int closingIndex, int start, int end) {
        validate(fullContent, tag);

        this.fullContent = fullContent;
        this.interval = new Interval(start, end);
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
        if(tag.equals(EMBEDDING_TAG)) return List.of(this);
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
        List<Integer> attributes = getAttributesStart();
        int start = 0;
        do {
            start = tagContent.indexOf(attr, start);
            if (start == -1) return "";
            start += attr.length();

        } while (!attributes.contains(start));

        char quote = tagContent.charAt(start + 1);
        int end = tagContent.indexOf(quote, start + 2);

        if (end == -1) return "";
        return tagContent.substring(start + 2, end);
    }

    private List<Integer> getAttributesStart() {
        List<Integer> starts = new ArrayList<>();
        int start = XmlExplorer.findOuterCharIndex(tagContent, '=', 0);
        while (start != -1) {
            starts.add(start);
            start = XmlExplorer.findOuterCharIndex(tagContent, '=', start + 1);
        }
        return starts;
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

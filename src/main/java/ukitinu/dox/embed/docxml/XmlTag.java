package ukitinu.dox.embed.docxml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class XmlTag {
    private final String tag;

    public XmlTag(String tag) {
        validate(tag);
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public String getOpening() {
        return tag.substring(0, tag.length() - 1);
    }

    public String getClosing() {
        return "</" + tag.substring(1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof XmlTag)) return false;
        XmlTag xmlTag = (XmlTag) o;
        return tag.equals(xmlTag.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag);
    }

    @Override
    public String toString() {
        return tag;
    }

    private static void validate(String tag) {
        Collection<String> errors = new ArrayList<>();
        if (tag == null || tag.isEmpty()) {
            errors.add("null or empty");
        } else {
            validateNonEmpty(tag, errors);
        }
        if (!errors.isEmpty()) {
            String message = String.format("Tag %s errors: %s", tag, String.join("; ", errors));
            throw new IllegalArgumentException(message);
        }
    }

    private static void validateNonEmpty(String tag, Collection<String> errors) {
        if (tag.length() < 3) errors.add("too short");
        if ('<' != (tag.charAt(0))) errors.add("missing '<' at start");
        if ('>' != (tag.charAt(tag.length() - 1))) errors.add("missing '>' at end");
        if (tag.substring(1, tag.length() - 1).isBlank()) errors.add("empty");

        int open = tag.length() - tag.replace("<", "").length();
        if (open > 1) errors.add("too many '<'");

        int close = tag.length() - tag.replace(">", "").length();
        if (close > 1) errors.add("too many '>'");
    }
}

package ukitinu.dox.embed.docxml;

import java.util.List;
import java.util.Objects;

public class EmbeddingLine {
    private final XmlElement previous;
    private final XmlElement current;
    private final XmlElement next;

    public EmbeddingLine(XmlElement previous, XmlElement current, XmlElement next) {
        if (current == null) throw new IllegalArgumentException("\"current\" element cannot be null");
        this.previous = previous;
        this.current = current;
        this.next = next;
    }

    public XmlElement getPrevious() {
        return previous;
    }

    public XmlElement getCurrent() {
        return current;
    }

    public XmlElement getNext() {
        return next;
    }

    public List<XmlElement> getEmbeddings() {
        return current.getEmbeddings();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmbeddingLine)) return false;
        EmbeddingLine embeddingLine = (EmbeddingLine) o;
        return Objects.equals(previous, embeddingLine.previous) &&
                current.equals(embeddingLine.current) &&
                Objects.equals(next, embeddingLine.next);
    }

    @Override
    public int hashCode() {
        return Objects.hash(previous, current, next);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("> ").append(previous.getText()).append(System.lineSeparator());
        sb.append(">> ").append(current.getText()).append(System.lineSeparator());
        sb.append("> ").append(next.getText()).append(System.lineSeparator());

        for(XmlElement tag : getEmbeddings()) {
            sb.append(tag.getFullContent()).append(System.lineSeparator());
        }
        return sb.append(System.lineSeparator()).toString();
    }
}

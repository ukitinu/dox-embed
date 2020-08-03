package ukitinu.dox.embed;

import java.util.Objects;

public class Embedding {
    private final int index;
    private final String progId;
    private final String name;

    public Embedding(int index, String progId, String name) {
        if (progId == null || name == null)
            throw new IllegalArgumentException("\"progId\" and \"name\" must not be null");
        this.index = index;
        this.progId = progId;
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public String getProgId() {
        return progId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%d\t%s\t%s", index, progId, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Embedding)) return false;
        Embedding embedding = (Embedding) o;
        return index == embedding.index &&
                progId.equals(embedding.progId) &&
                name.equals(embedding.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, progId, name);
    }
}

package Ariadne;

/*
A node label.

*/
public class Label {
    private final String value;

    public Label(String value) {
        this.value = value;
    }

    public String get() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Label label = (Label) o;
        return value.equals(label.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}

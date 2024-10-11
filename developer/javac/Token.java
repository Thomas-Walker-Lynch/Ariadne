package Ariadne;

/*
An error token.

*/
public class Token {
    private final String value;

    public Token(String value) {
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
        Token token = (Token) o;
        return value.equals(token.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}

package utils;

public enum RomanNumbers {
    I("I"), II("II"), III("III"), IV("IV"), V("V"), INVALID("");
    private final String text;

    RomanNumbers(String text) {
        this.text = text;
    }
}


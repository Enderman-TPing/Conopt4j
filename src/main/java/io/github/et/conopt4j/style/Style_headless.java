package io.github.et.conopt4j.style;

public enum Style_headless {
    HINT("%s\033[0m%s %s"),ALL("%s%s\033[0m %s");

    private String s;
    Style_headless(String s) {
        this.s=s;
    }

    @Override
    public String toString(){
        return s;
    }
}

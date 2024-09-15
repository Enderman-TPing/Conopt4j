package io.github.et.conopt4j.style;

public enum Style {
    HINT("[ %s%s\033[0m ] %s \033[33m--%s--\033[0m %s %s"),ALL("%s[ %s ] %s --%s-- %s\033[0m %s");

    private String s;
    Style(String s) {
        this.s=s;
    }

    @Override
    public String toString(){
        return s;
    }
}

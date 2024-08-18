package io.github.et.conopt4j.style;

public enum Color {
    RED("\033[31m"),GREEN("\033[32m"),YELLOW("\033[33m"),BLUE("\033[34m"),PURPLE("\033[35m"), CYAN("\033[36m"), WHITE("\033[37m");
    private final String s;
    Color(String s) {
        this.s=s;
    }

    @Override
    public String toString(){
        return s;
    }
}

package io.github.et.conopt4j.style;

import org.jline.utils.AttributedStyle;

public enum Color {
    RED(AttributedStyle.DEFAULT.foreground(AttributedStyle.RED)),GREEN(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)),YELLOW(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)),BLUE(AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE)),PURPLE(AttributedStyle.DEFAULT.foreground(AttributedStyle.MAGENTA)), CYAN(AttributedStyle.DEFAULT.foreground(AttributedStyle.CYAN)), WHITE(AttributedStyle.DEFAULT.foreground(AttributedStyle.WHITE));
    private final AttributedStyle s;
    Color(AttributedStyle s) {
        this.s = s;
    }
    public AttributedStyle getStyle() {
        return s;
    }
    @Override
    public String toString(){
        return String.valueOf(s);
    }
}

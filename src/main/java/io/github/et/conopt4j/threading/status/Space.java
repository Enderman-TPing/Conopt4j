package io.github.et.conopt4j.threading.status;

enum Space {
    SPACE(' '),ZWSP('\u200B');
    private static char currentSpace=' ';
    private char value;
    Space(char value) {
        this.value = value;
    }
    public String toString() {
        return String.valueOf(value);
    }
    public static char getSpace(){
        if(currentSpace == SPACE.value){
            currentSpace=ZWSP.value;
            return currentSpace;
        }else{
            currentSpace=SPACE.value;
            return currentSpace;
        }
    }
}

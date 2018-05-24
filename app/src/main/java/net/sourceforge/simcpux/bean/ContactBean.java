package net.sourceforge.simcpux.bean;

public class ContactBean {
    private String name;
    private String pinyin;
    private char sectionKey;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public ContactBean(String name, String pinyin) {
        this.name = name;
        this.pinyin = pinyin;
        sectionKey = pinyin.substring(0, 1).toUpperCase().toCharArray()[0];
        if (!Character.isLetter(sectionKey)) {
            this.sectionKey = '#';
        }
    }

    public char getSectionKey() {
        return sectionKey;
    }
}

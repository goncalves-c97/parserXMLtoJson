package com.parserxmltojson;

import java.util.ArrayList;

public class TagClass {

    private String tagName;
    private String tagStringContent;
    private ArrayList<TagClass> TagClass = new ArrayList<>();

    public TagClass() {
        
    }
    
    public TagClass(String tagName) {
        this.tagName = tagName;
    }
    
    public TagClass(String tagName, String tagStringContent) {
        this.tagName = tagName;
        this.tagStringContent = tagStringContent;
    }

    public TagClass(String tagName, String tagStringContent, ArrayList TagClass) {
        this.tagName = tagName;
        this.tagStringContent = tagStringContent;
        this.TagClass = TagClass;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagStringContent() {
        return tagStringContent;
    }

    public void setTagStringContent(String tagStringContent) {
        this.tagStringContent = tagStringContent;
    }

    public ArrayList getTagClass() {
        return TagClass;
    }

    public void setTagClass(ArrayList TagClass) {
        this.TagClass = TagClass;
    }
}

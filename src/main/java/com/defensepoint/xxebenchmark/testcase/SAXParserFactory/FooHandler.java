package com.defensepoint.xxebenchmark.testcase.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

class FooHandler extends DefaultHandler {

    boolean isFoo = false;
    String foo;

    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equalsIgnoreCase("foo")) {
            isFoo = true;
        }
    }

    public void characters(char ch[], int start, int length) {
        if (isFoo) {
            foo = new String(ch, start, length);
            isFoo = false;
        }
    }

    public String getFoo() {
        return foo;
    }
}

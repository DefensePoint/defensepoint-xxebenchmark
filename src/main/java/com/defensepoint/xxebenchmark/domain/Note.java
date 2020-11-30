package com.defensepoint.xxebenchmark.domain;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "note")
@XmlAccessorType(XmlAccessType.FIELD)
public class Note {

    @XmlElement(name = "foo")
    private String foo;

    public String getFoo() {
        return foo;
    }

    public void setFoo(String foo) {
        this.foo = foo;
    }

    @Override
    public String toString() {
        return "foo=" + foo;
    }
}

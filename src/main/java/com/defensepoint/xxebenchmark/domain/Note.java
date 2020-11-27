package com.defensepoint.xxebenchmark.domain;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "note")
public class Note {

    private String foo;

    @XmlElement(name = "foo")
    public String getFoo() {
        return foo;
    }

    public void setFoo(String foo) {
        this.foo = foo;
    }

    @Override
    public String toString() {
        return "Foo{" +
                "foo='" + foo + '\'' +
                '}';
    }
}

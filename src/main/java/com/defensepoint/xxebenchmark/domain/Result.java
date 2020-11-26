package com.defensepoint.xxebenchmark.domain;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

public class Result {

    public static List<Result> results = new ArrayList<>();

    private String testId;
    private String testName;
    private Parser parser;
    private String configuration;
    private Vulnerability vulnerable;

    public Result(Document document) {
    }

    public Result(String testId, String testName, Parser parser, String configuration, Vulnerability vulnerable) {
        this.testId = testId;
        this.testName = testName;
        this.parser = parser;
        this.configuration = configuration;
        this.vulnerable = vulnerable;
    }

    public static List<Result> getResults() {
        return results;
    }

    public static void setResults(List<Result> results) {
        Result.results = results;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public Parser getParser() {
        return parser;
    }

    public void setParser(Parser parser) {
        this.parser = parser;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public Vulnerability getVulnerable() {
        return vulnerable;
    }

    public void setVulnerable(Vulnerability vulnerable) {
        this.vulnerable = vulnerable;
    }

    @Override
    public String toString() {
        return "Result{" +
                "testId='" + testId + '\'' +
                ", testName='" + testName + '\'' +
                ", parser=" + parser +
                ", configuration='" + configuration + '\'' +
                ", vulnerable=" + vulnerable +
                '}';
    }
}

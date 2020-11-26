package com.defensepoint.xxebenchmark.testcase.TransformerFactory;

import com.defensepoint.xxebenchmark.domain.Parser;
import com.defensepoint.xxebenchmark.domain.Vulnerability;
import com.defensepoint.xxebenchmark.util.OSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.Objects;

@Component
public class Xml_tf_011 {
    private static final Logger logger = LoggerFactory.getLogger(Xml_tf_011.class);

    //@PostConstruct
    public void parse() {

        logger.info("Xml_tf_011");

        String testId = "xml-tf-" + OSUtil.getOS() + "-" + System.getProperty("java.version") + "-011";
        String testName = "Local File Inclusion / default configuration";
        Parser parser = Parser.TransformerFactory;
        String configuration = "";
        Vulnerability vulnerable = Vulnerability.YES; // Initial value, vulnerable payload

        String foo = "";

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File xmlFile = new File(Objects.requireNonNull(classLoader.getResource("xml/localFileInclusion.xml")).getFile());

            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            Document document = builder.newDocument();
            try ( FileInputStream in = new FileInputStream(xmlFile)) {
                Source loadSource = new StreamSource(in);
                Result loadResult = new DOMResult(document);
                transformer.transform(loadSource, loadResult);
            }
            document.getDocumentElement().normalize();
            foo = document.getDocumentElement().getTextContent();

            logger.info(foo);

        } catch (TransformerConfigurationException e) {
            logger.error("TransformerConfigurationException: " + e.getMessageAndLocation());
        } catch (TransformerException e) {
            logger.error("TransformerException: " + e.getMessageAndLocation());
        } catch (IOException e) {
            logger.error("IOException: " + e.getMessage());
        } catch (ParserConfigurationException e) {
            logger.error("ParserConfigurationException: " + e.getMessage());
        } finally {
            vulnerable = foo.equalsIgnoreCase("XXE") ? Vulnerability.YES : Vulnerability.NO;

            com.defensepoint.xxebenchmark.domain.Result result = new com.defensepoint.xxebenchmark.domain.Result(testId, testName, parser, configuration, vulnerable);
            com.defensepoint.xxebenchmark.domain.Result.results.add(result);
            logger.info("Result: " + result.toString());
        }
    }
}

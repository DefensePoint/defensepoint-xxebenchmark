package com.defensepoint.xxebenchmark.testcase.SAXTransformerFactory;

import com.defensepoint.xxebenchmark.domain.Parser;
import com.defensepoint.xxebenchmark.domain.Vulnerability;
import com.defensepoint.xxebenchmark.util.OSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.StringWriter;
import java.util.Objects;

@Component
public class Xml_stf_015 {
    private static final Logger logger = LoggerFactory.getLogger(Xml_stf_015.class);

    //@PostConstruct
    public void parse() {

        logger.info("Xml_stf_015");

        String testId = "xml-stf-" + OSUtil.getOS() + "-" + System.getProperty("java.version") + "-015";
        String testName = "Local XSLT Transformation / default configuration";
        Parser parser = Parser.TransformerFactory;
        String configuration = "";
        Vulnerability vulnerable = Vulnerability.YES; // Initial value, vulnerable payload

        ClassLoader classLoader = getClass().getClassLoader();
        File xsltFile = new File(Objects.requireNonNull(classLoader.getResource("xml/foo.xslt")).getFile());
        File xmlFile = new File(Objects.requireNonNull(classLoader.getResource("xml/foo.xml")).getFile());
        Source xslt = new StreamSource(xsltFile);
        Source xml  = new StreamSource(xmlFile);

        StringWriter writer = new StringWriter();
        StreamResult target = new StreamResult(writer);

        String content = "";

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Transformer transformer = transformerFactory.newTransformer(xslt);
            transformer.transform(xml, target);

            content = writer.toString();
            logger.info(writer.toString());
        } catch (TransformerConfigurationException e) {
            logger.error("TransformerConfigurationException was thrown. " + e.getMessage());
        } catch (TransformerException e) {
            logger.error("TransformerException was thrown. " + e.getMessage());
        } finally {
            vulnerable = content.contains("XXE") ? Vulnerability.YES : Vulnerability.NO;

            com.defensepoint.xxebenchmark.domain.Result result = new com.defensepoint.xxebenchmark.domain.Result(testId, testName, parser, configuration, vulnerable);
            com.defensepoint.xxebenchmark.domain.Result.results.add(result);
            logger.info("Result: " + result.toString());
        }
    }
}

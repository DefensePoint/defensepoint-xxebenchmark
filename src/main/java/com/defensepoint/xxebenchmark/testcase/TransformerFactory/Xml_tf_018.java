package com.defensepoint.xxebenchmark.testcase.TransformerFactory;

import com.defensepoint.xxebenchmark.domain.Parser;
import com.defensepoint.xxebenchmark.domain.Vulnerability;
import com.defensepoint.xxebenchmark.util.OSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.XMLConstants;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Objects;

//@Component
public class Xml_tf_018 {
    private static final Logger logger = LoggerFactory.getLogger(Xml_tf_018.class);

    //@PostConstruct
    public void parse() {

        logger.info("Xml_tf_018");

        String testId = "xml-tf-" + OSUtil.getOS() + "-" + System.getProperty("java.version") + "-018";
        String testName = "Remote XSLT Transformation / FEATURE_SECURE_PROCESSING is enabled";
        Parser parser = Parser.TransformerFactory;
        String configuration = "transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true)";
        Vulnerability vulnerable = Vulnerability.YES; // Initial value, vulnerable payload

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream xsltFile = classLoader.getResourceAsStream("xml/fooRemote.xslt");
        InputStream xmlFile = classLoader.getResourceAsStream("xml/foo.xml");
        Source xslt = new StreamSource(xsltFile);
        Source xml  = new StreamSource(xmlFile);

        StringWriter writer = new StringWriter();
        StreamResult target = new StreamResult(writer);

        String content = "";

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            Transformer transformer = transformerFactory.newTransformer(xslt);
            transformer.transform(xml, target);

            content = writer.toString();

            logger.info(content);
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

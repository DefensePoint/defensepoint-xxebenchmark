package com.defensepoint.xxebenchmark.testcase.DocumentBuilderFactory;

import com.defensepoint.xxebenchmark.domain.Parser;
import com.defensepoint.xxebenchmark.domain.Result;
import com.defensepoint.xxebenchmark.domain.Vulnerability;
import com.defensepoint.xxebenchmark.util.OSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Component
public class Xml_dbf_009 {
    private static final Logger logger = LoggerFactory.getLogger(Xml_dbf_009.class);

    //@PostConstruct
    public void parse() {

        logger.info("Xml_dbf_009");

        String testId = "xml-dbf-" + OSUtil.getOS() + "-" + System.getProperty("java.version") + "-009";
        String testName = "Remote Schema / default configuration";
        Parser parser = Parser.DocumentBuilderFactory;
        String configuration = "";
        Vulnerability vulnerable = Vulnerability.YES; // Default value

        ClassLoader classLoader = getClass().getClassLoader();
        File xmlFile = new File(Objects.requireNonNull(classLoader.getResource("xml/remoteSchema.xml")).getFile());

        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //API to obtain DOM Document instance
        DocumentBuilder builder;

        try {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object
            Document doc = builder.parse(xmlFile);

            //Normalize the XML Structure
            doc.getDocumentElement().normalize();

            Element element = doc.getDocumentElement();

            String foo = element.getTextContent();

            logger.info(foo);

        } catch (ParserConfigurationException e) {
            logger.error("ParserConfigurationException was thrown: " + e.getMessage());
        } catch (SAXException e) {
            logger.error("SAXException was thrown: " + e.getMessage());
            vulnerable = Vulnerability.NO;
        } catch (IOException e) {
            logger.error("IOException was thrown. IOException occurred, XXE may still possible: " + e.getMessage());
        } finally {
            Result result = new Result(testId, testName, parser, configuration, vulnerable);
            Result.results.add(result);
            logger.info("Result: " + result.toString());
        }
    }
}

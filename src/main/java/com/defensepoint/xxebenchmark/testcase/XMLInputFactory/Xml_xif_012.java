package com.defensepoint.xxebenchmark.testcase.XMLInputFactory;

import com.defensepoint.xxebenchmark.domain.Parser;
import com.defensepoint.xxebenchmark.domain.Result;
import com.defensepoint.xxebenchmark.domain.Vulnerability;
import com.defensepoint.xxebenchmark.util.FileUtil;
import com.defensepoint.xxebenchmark.util.OSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

//@Component
public class Xml_xif_012 {
    private static final Logger logger = LoggerFactory.getLogger(Xml_xif_012.class);

    //@PostConstruct
    public void parse() {

        logger.info("Xml_xif_012");

        String testId = "xml-xif-" + OSUtil.getOS() + "-" + System.getProperty("java.version") + "-012";
        String testName = "Local File Inclusion / External entities and DTDs are disabled";
        Parser parser = Parser.XMLInputFactory;
        String configuration = "factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, \"\") " +
                "factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, \"\") " +
                "factory.setProperty(\"javax.xml.stream.isReplacingEntityReferences\", false)";
        Vulnerability vulnerable = Vulnerability.YES; // Initial value. Vulnerable payload.

        String foo = "";

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("xml/localFileInclusion.xml");
            String xmlString = FileUtil.readFromInputStream(inputStream);

            XMLInputFactory factory = XMLInputFactory.newInstance();
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            factory.setProperty("javax.xml.stream.isReplacingEntityReferences", false);

            XMLStreamReader streamReader = factory.createXMLStreamReader(new StringReader(xmlString));

            if (streamReader != null) {
                while (streamReader.hasNext()) {
                    //Move to next event
                    streamReader.next();

                    if (streamReader.isStartElement()) {
                        //Read foo data
                        if (streamReader.getLocalName().equalsIgnoreCase("foo")) {
                            foo = streamReader.getElementText();
                            logger.info(foo);
                        }
                    }
                }
            }
        } catch (XMLStreamException e) {
            logger.error("XMLStreamException was thrown: " + e.getMessage());
        } catch (IOException e) {
            logger.error("IOException was thrown. IOException occurred, XXE may still possible: " + e.getMessage());
        } finally {
            vulnerable = foo.equalsIgnoreCase("XXE") ? Vulnerability.YES : Vulnerability.NO;

            Result result = new Result(testId, testName, parser, configuration, vulnerable);
            Result.results.add(result);
            logger.info("Result: " + result.toString());
        }
    }
}

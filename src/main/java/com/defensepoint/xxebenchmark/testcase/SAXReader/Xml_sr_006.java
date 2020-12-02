package com.defensepoint.xxebenchmark.testcase.SAXReader;

import com.defensepoint.xxebenchmark.domain.Parser;
import com.defensepoint.xxebenchmark.domain.Result;
import com.defensepoint.xxebenchmark.domain.Vulnerability;
import com.defensepoint.xxebenchmark.util.FileUtil;
import com.defensepoint.xxebenchmark.util.OSUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

//@Component
public class Xml_sr_006 {
    private static final Logger logger = LoggerFactory.getLogger(Xml_sr_006.class);

    //@PostConstruct
    public void parse() {

        logger.info("Xml_sr_006");

        String testId = "xml-sr-" + OSUtil.getOS() + "-" + System.getProperty("java.version") + "-006";
        String testName = "File Disclosure / DTDs (doctypes) are disallowed";
        Parser parser = Parser.SAXReader;
        String configuration = "xmlReader.setFeature(\"http://apache.org/xml/features/disallow-doctype-decl\", true)";
        Vulnerability vulnerable = Vulnerability.YES; // Initial value, vulnerable payload

        String foo = "";

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("xml/fileDisclosure.xml");
            String xmlString = FileUtil.readFromInputStream(inputStream);

            SAXReader xmlReader = new SAXReader();
            xmlReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

            Document document = xmlReader.read(new InputSource(new StringReader(xmlString)));

            foo = document.getRootElement().getText();

            logger.info(foo);

        } catch (DocumentException e) {
            logger.error("DocumentException was thrown: " + e.getMessage());
        } catch (IOException e) {
            logger.error("IOException was thrown: " + e.getMessage());
        } catch (SAXException e) {
            logger.error("SAXException was thrown: " + e.getMessage());
        } finally {
            vulnerable = foo.equalsIgnoreCase("XXE") ? Vulnerability.YES : Vulnerability.NO;

            Result result = new Result(testId, testName, parser, configuration, vulnerable);
            Result.results.add(result);
            logger.info(result.toString());
        }
    }
}

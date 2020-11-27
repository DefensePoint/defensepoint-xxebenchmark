package com.defensepoint.xxebenchmark.testcase.XMLReader;

import com.defensepoint.xxebenchmark.domain.Parser;
import com.defensepoint.xxebenchmark.domain.Result;
import com.defensepoint.xxebenchmark.domain.Vulnerability;
import com.defensepoint.xxebenchmark.util.OSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.*;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@Component
public class Xml_xr_014 {
    private static final Logger logger = LoggerFactory.getLogger(Xml_xr_014.class);

    //@PostConstruct
    public void parse() {

        logger.info("Xml_xr_014");

        String testId = "xml-xr-" + OSUtil.getOS() + "-" + System.getProperty("java.version") + "-014";
        String testName = "Remote File Inclusion - SSRF (Server Side Request Forgery) / DTDs (doctypes) are disallowed";
        Parser parser = Parser.XMLReader;
        String configuration = "reader.setFeature(\"http://apache.org/xml/features/disallow-doctype-decl\", true)";
        Vulnerability vulnerable = Vulnerability.YES; // Initial value. Vulnerable payload.

        String foo = "";

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File xmlFile = new File(Objects.requireNonNull(classLoader.getResource("xml/remoteFileInclusion.xml")).getFile());
            String xmlString = new String ( Files.readAllBytes( Paths.get(xmlFile.getAbsolutePath()) ) );

            FooReaderHandler handler = new FooReaderHandler();
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            reader.setContentHandler(handler);

            reader.parse(new InputSource(new StringReader(xmlString)));

            foo = handler.getFoo();
            logger.info(foo);

        } catch (IOException e) {
            logger.error("IOException was thrown: " + e.getMessage());
        } catch (SAXNotRecognizedException e) {
            logger.error("SAXNotRecognizedException was thrown: " + e.getMessage());
        } catch (SAXNotSupportedException e) {
            logger.error("SAXNotSupportedException was thrown: " + e.getMessage());
        } catch (SAXException e) {
            logger.error("SAXException was thrown: " + e.getMessage());
        } finally {
            vulnerable = foo.equalsIgnoreCase("XXE") ? Vulnerability.YES : Vulnerability.NO;

            Result result = new Result(testId, testName, parser, configuration, vulnerable);
            Result.results.add(result);
            logger.info("Result: " + result.toString());
        }
    }
}

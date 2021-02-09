package com.defensepoint.xxebenchmark.testcase.JAXBUnmarshaller;

import com.defensepoint.xxebenchmark.domain.*;
import com.defensepoint.xxebenchmark.util.OSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXParseException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;

//@Component
public class Xml_jaxu_023 {
    private static final Logger logger = LoggerFactory.getLogger(Xml_jaxu_023.class);

    //@PostConstruct
    public void parse() {

        logger.info("Xml_jaxu_023");

        String testId = "xml-jaxu-" + OSUtil.getOS() + "-" + System.getProperty("java.version") + "-023";
        String testName = "File Disclosure / default configuration / Unmarshalling from  a org.w3c.dom.Node";
        Parser parser = Parser.JAXBUnmarshaller;
        String configuration = "";
        Vulnerability vulnerable = Vulnerability.YES; // Initial value. Vulnerable payload.

        String foo = "";

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("xml/fileDisclosure.xml");

            JAXBContext jaxbContext = JAXBContext.newInstance(Note.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputStream);
            doc.getDocumentElement().normalize();

            Note noteObject = (Note) jaxbUnmarshaller.unmarshal(doc);
            foo = noteObject.getFoo();

            logger.info(foo);

        } catch (UnmarshalException e) {
            logger.error("UnmarshalException was thrown: " + e.getMessage());
        } catch (JAXBException e) {
            logger.error("JAXBException was thrown: " + e.getMessage());
        } catch (SAXParseException e) {
            logger.error("SAXParseException was thrown: " + e.getMessage());
        } catch (Exception e) {
            logger.error(e.getClass().getName() + " was thrown: " + e.getMessage());
        } finally {
            vulnerable = foo.equalsIgnoreCase("XXE") ? Vulnerability.YES : Vulnerability.NO;

            Result result = new Result(testId, testName, parser, configuration, vulnerable);
            Result.results.add(result);
            logger.info(result.toString());
        }
    }
}
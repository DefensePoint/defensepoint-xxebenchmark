package com.defensepoint.xxebenchmark.testcase.JAXBUnmarshaller;

import com.defensepoint.xxebenchmark.domain.*;
import com.defensepoint.xxebenchmark.util.FileUtil;
import com.defensepoint.xxebenchmark.util.OSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import java.io.*;

//@Component
public class Xml_jaxu_016 {
    private static final Logger logger = LoggerFactory.getLogger(Xml_jaxu_016.class);

    //@PostConstruct
    public void parse() {

        logger.info("Xml_jaxu_016");

        String testId = "xml-jaxu-" + OSUtil.getOS() + "-" + System.getProperty("java.version") + "-016";
        String testName = "File Disclosure / DTDs (doctypes) are disallowed / Unmarshalling from a SAXSource";
        Parser parser = Parser.JAXBUnmarshaller;
        String configuration = "spf.setFeature(\"http://xml.org/sax/features/external-general-entities\", false) " +
                                "spf.setFeature(\"http://xml.org/sax/features/external-parameter-entities\", false) " +
                                "spf.setFeature(\"http://apache.org/xml/features/nonvalidating/load-external-dtd\", false)";
        Vulnerability vulnerable = Vulnerability.YES; // Initial value. Vulnerable payload.

        String foo = "";

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("xml/fileDisclosureNote.xml");
            String xmlString = FileUtil.readFromInputStream(inputStream);


            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setFeature("http://xml.org/sax/features/external-general-entities", false);
            spf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            Source xmlSource = new SAXSource(spf.newSAXParser().getXMLReader(), new InputSource(new StringReader(xmlString)));

            JAXBContext jaxbContext = JAXBContext.newInstance(Note.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            Note noteObject = (Note) jaxbUnmarshaller.unmarshal(xmlSource);
            foo = noteObject.getFoo();

            logger.info(foo);

        } catch (UnmarshalException e) {
            logger.error("UnmarshalException was thrown: " + e.getMessage());
        } catch (JAXBException e) {
            logger.error("JAXBException was thrown: " + e.getMessage());
        } catch (IOException e) {
            logger.error("IOException was thrown: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Exception was thrown: " + e.getMessage());
        } finally {
            vulnerable = foo.equalsIgnoreCase("XXE") ? Vulnerability.YES : Vulnerability.NO;

            Result result = new Result(testId, testName, parser, configuration, vulnerable);
            Result.results.add(result);
            logger.info(result.toString());
        }
    }
}

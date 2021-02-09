package com.defensepoint.xxebenchmark.testcase.JAXBUnmarshaller;

import com.defensepoint.xxebenchmark.domain.*;
import com.defensepoint.xxebenchmark.util.OSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import java.io.*;

//@Component
public class Xml_jaxu_017 {
    private static final Logger logger = LoggerFactory.getLogger(Xml_jaxu_017.class);

    //@PostConstruct
    public void parse() {

        logger.info("Xml_jaxu_017");

        String testId = "xml-jaxu-" + OSUtil.getOS() + "-" + System.getProperty("java.version") + "-017";
        String testName = "File Disclosure / default configuration / Unmarshalling from an InputStream";
        Parser parser = Parser.JAXBUnmarshaller;
        String configuration = "";
        Vulnerability vulnerable = Vulnerability.YES; // Initial value. Vulnerable payload.

        String foo = "";

        try {
            InputStream is = new FileInputStream("/XXEBenchmarkFiles/fileDisclosureNote.xml");

            JAXBContext jaxbContext = JAXBContext.newInstance(Note.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            Note noteObject = (Note) jaxbUnmarshaller.unmarshal(is);
            foo = noteObject.getFoo();

            logger.info(foo);

        } catch (UnmarshalException e) {
            logger.error("UnmarshalException was thrown: " + e.getMessage());
        } catch (JAXBException e) {
            logger.error("JAXBException was thrown: " + e.getMessage());
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

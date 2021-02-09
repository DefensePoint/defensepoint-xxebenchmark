package com.defensepoint.xxebenchmark.testcase.JAXBUnmarshaller;

import com.defensepoint.xxebenchmark.domain.*;
import com.defensepoint.xxebenchmark.util.FileUtil;
import com.defensepoint.xxebenchmark.util.OSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.io.StringReader;

//@Component
public class Xml_jaxu_019 {
    private static final Logger logger = LoggerFactory.getLogger(Xml_jaxu_019.class);

    //@PostConstruct
    public void parse() {

        logger.info("Xml_jaxu_019");

        String testId = "xml-jaxu-" + OSUtil.getOS() + "-" + System.getProperty("java.version") + "-019";
        String testName = "File Disclosure / default configuration / Unmarshalling from a StringBuffer using StreamSource";
        Parser parser = Parser.JAXBUnmarshaller;
        String configuration = "";
        Vulnerability vulnerable = Vulnerability.YES; // Initial value. Vulnerable payload.

        String foo = "";

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("xml/remoteFileInclusionNote.xml");
            String xmlString = FileUtil.readFromInputStream(inputStream);
            StringBuffer xmlStr = new StringBuffer(xmlString);

            JAXBContext jaxbContext = JAXBContext.newInstance(Note.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            Note noteObject = (Note) jaxbUnmarshaller.unmarshal(new StreamSource(new StringReader(xmlStr.toString())));
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

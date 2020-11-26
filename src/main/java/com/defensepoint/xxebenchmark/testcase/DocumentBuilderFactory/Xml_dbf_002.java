package com.defensepoint.xxebenchmark.testcase.DocumentBuilderFactory;

import com.defensepoint.xxebenchmark.domain.Constants;
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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Component
public class Xml_dbf_002 {
    private static final Logger logger = LoggerFactory.getLogger(Xml_dbf_002.class);

    @PostConstruct
    public void parse() {

        logger.info("Xml_dbf_002");

        Thread th = new Thread ( new Xml_dbf_002_thread() , "Xml_dbf_002_thread");
        th.start();

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        logger.info("Stop thread: " + th.getName());
                        th.stop();
                    }
                },
                Constants.DoS_THREAD_DURATION
        );
    }
}

class Xml_dbf_002_thread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Xml_dbf_002_thread.class);

    @Override
    public void run() {
        logger.info("Start thread: " + Thread.currentThread().getName());

        String testId = "xml-dbf-" + OSUtil.getOS() + "-" + System.getProperty("java.version") + "-002";
        String testName = "Denial-of-Service - Billion Laughs / DTDs (doctypes) are disallowed";
        Parser parser = Parser.DocumentBuilderFactory;
        String configuration = "factory.setFeature(\"http://apache.org/xml/features/disallow-doctype-decl\", true)";
        Vulnerability vulnerable = Vulnerability.YES; // Initial value. Vulnerable payload.

        ClassLoader classLoader = getClass().getClassLoader();
        File xmlFile = new File(Objects.requireNonNull(classLoader.getResource("xml/dos.xml")).getFile());

        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //API to obtain DOM Document instance
        DocumentBuilder builder;

        LocalDateTime nowStart = null;
        LocalDateTime nowEnd = null;

        try {
            //Disallow dtd
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object
            nowStart = LocalDateTime.now();
            Document doc = builder.parse(xmlFile);
            nowEnd = LocalDateTime.now();

            //Normalize the XML Structure
            doc.getDocumentElement().normalize();

            Element element = doc.getDocumentElement();
            String foo = element.getTextContent();

            logger.info(foo);

        } catch (ParserConfigurationException e) {
            logger.error("ParserConfigurationException was thrown: " + e.getMessage());
        } catch (SAXException e) {
            logger.error("SAXException was thrown: " +  e.getMessage());
            vulnerable = Vulnerability.NO;
        } catch (IOException e) {
            logger.error("IOException was thrown. IOException occurred, XXE may still possible: " + e.getMessage());
        } finally {
            nowEnd = nowEnd == null ? LocalDateTime.now() : nowEnd;
            assert nowStart != null;
            long diff = ChronoUnit.MILLIS.between(nowStart, nowEnd);

            logger.info(String.format("XML parsing took %d milliseconds.", diff));
            if(diff > Constants.DoS_THRESHOLD) {
                vulnerable = Vulnerability.YES;
                logger.error(String.format("XML parsing takes more than %d (%d) milliseconds.", Constants.DoS_THRESHOLD, diff));
            }

            Result result = new Result(testId, testName, parser, configuration, vulnerable);
            Result.results.add(result);
            logger.info(result.toString());
        }
    }
}
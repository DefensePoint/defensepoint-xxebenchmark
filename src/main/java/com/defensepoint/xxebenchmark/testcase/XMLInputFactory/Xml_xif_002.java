package com.defensepoint.xxebenchmark.testcase.XMLInputFactory;

import com.defensepoint.xxebenchmark.domain.Constants;
import com.defensepoint.xxebenchmark.domain.Parser;
import com.defensepoint.xxebenchmark.domain.Result;
import com.defensepoint.xxebenchmark.domain.Vulnerability;
import com.defensepoint.xxebenchmark.service.TimeOutTask;
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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Timer;

//@Component
public class Xml_xif_002 {
    private static final Logger logger = LoggerFactory.getLogger(Xml_xif_002.class);

    //@PostConstruct
    public void parse() {

        logger.info("Xml_xif_002");

        Thread t = new Thread(new Xml_xif_002_thread());
        Timer timer = new Timer();
        timer.schedule(new TimeOutTask(t, timer), Constants.DoS_THREAD_DURATION);
        t.start();
    }
}

class Xml_xif_002_thread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Xml_xif_002_thread.class);

    @Override
    public void run() {
        logger.info("Start thread: " + Thread.currentThread().getName());

        String testId = "xml-xif-" + OSUtil.getOS() + "-" + System.getProperty("java.version") + "-002";
        String testName = "Denial-of-Service - Billion Laughs / External entities and DTDs are disabled";
        Parser parser = Parser.XMLInputFactory;
        String configuration = "factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, \"\") " +
                                "factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, \"\") " +
                                "factory.setProperty(\"javax.xml.stream.isReplacingEntityReferences\", false)";
        Vulnerability vulnerable = Vulnerability.YES; // Initial value. Vulnerable payload.

        LocalDateTime nowStart = null;
        LocalDateTime nowEnd = null;

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("xml/dos.xml");
            String xmlString = FileUtil.readFromInputStream(inputStream);

            XMLInputFactory factory = XMLInputFactory.newInstance();
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            factory.setProperty("javax.xml.stream.isReplacingEntityReferences", false);

            nowStart = LocalDateTime.now();
            XMLStreamReader streamReader = factory.createXMLStreamReader(new StringReader(xmlString));

            if (streamReader != null) {
                while (streamReader.hasNext()) {
                    //Move to next event
                    streamReader.next();

                    if (streamReader.isStartElement()) {
                        //Read foo data
                        if (streamReader.getLocalName().equalsIgnoreCase("foo")) {
                            String foo = streamReader.getElementText();
                            logger.info(foo);
                        }
                    }
                }
            }
            nowEnd = LocalDateTime.now();

        } catch (XMLStreamException e) {
            logger.error("XMLStreamException was thrown: " + e.getMessage());
        } catch (IOException e) {
            logger.error("IOException was thrown. IOException occurred, XXE may still possible: " + e.getMessage());
        } finally {
            nowEnd = nowEnd == null ? LocalDateTime.now() : nowEnd;
            assert nowStart != null;
            long diff = ChronoUnit.MILLIS.between(nowStart, nowEnd);

            if(diff > Constants.DoS_THRESHOLD) {
                vulnerable = Vulnerability.YES;
                logger.error(String.format("XML parsing takes more than %d (%d) milliseconds.", Constants.DoS_THRESHOLD, diff));
            } else {
                logger.info(String.format("XML parsing takes less than %d (%d) milliseconds.", Constants.DoS_THRESHOLD, diff));
                vulnerable = Vulnerability.NO;
            }

            Result result = new Result(testId, testName, parser, configuration, vulnerable);
            Result.results.add(result);
            logger.info(result.toString());
        }
    }
}
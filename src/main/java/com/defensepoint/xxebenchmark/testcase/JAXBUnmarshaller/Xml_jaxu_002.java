package com.defensepoint.xxebenchmark.testcase.JAXBUnmarshaller;

import com.defensepoint.xxebenchmark.domain.*;
import com.defensepoint.xxebenchmark.util.OSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Component
public class Xml_jaxu_002 {
    private static final Logger logger = LoggerFactory.getLogger(Xml_jaxu_002.class);

    //@PostConstruct
    public void parse() {

        logger.info("Xml_jaxu_002");

        Thread th = new Thread ( new Xml_jaxu_002_thread() , "Xml_jaxu_002_thread");
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

class Xml_jaxu_002_thread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Xml_jaxu_002_thread.class);

    @Override
    public void run() {
        logger.info("Start thread: " + Thread.currentThread().getName());

        String testId = "xml-jaxu-" + OSUtil.getOS() + "-" + System.getProperty("java.version") + "-002";
        String testName = "Denial-of-Service - Billion Laughs / default configuration";
        Parser parser = Parser.JAXBUnmarshaller;
        String configuration = "";
        Vulnerability vulnerable = Vulnerability.YES; // Initial value. Vulnerable payload.

        LocalDateTime nowStart = null;
        LocalDateTime nowEnd = null;

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File xmlFile = new File(Objects.requireNonNull(classLoader.getResource("xml/dosNote.xml")).getFile());
            String xmlString = new String ( Files.readAllBytes( Paths.get(xmlFile.getAbsolutePath()) ) );

            JAXBContext jaxbContext = JAXBContext.newInstance(Note.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            StringReader stringReader = new StringReader(xmlString);
            nowStart = LocalDateTime.now();
            Note noteObject = (Note) jaxbUnmarshaller.unmarshal(stringReader);
            nowStart = LocalDateTime.now();

            String foo = noteObject.getFoo();

            logger.info(foo);

        } catch (UnmarshalException e) {
            logger.error("UnmarshalException was thrown: " + e.getMessage());
            vulnerable = Vulnerability.NO;
        } catch (JAXBException e) {
            logger.error("JAXBException was thrown: " + e.getMessage());
            vulnerable = Vulnerability.NO;
        } catch (IOException e) {
            logger.error("IOException was thrown: " + e.getMessage());
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
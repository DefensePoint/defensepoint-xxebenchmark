package com.defensepoint.xxebenchmark.testcase.SAXTransformerFactory;

import com.defensepoint.xxebenchmark.domain.Constants;
import com.defensepoint.xxebenchmark.domain.Parser;
import com.defensepoint.xxebenchmark.domain.Result;
import com.defensepoint.xxebenchmark.domain.Vulnerability;
import com.defensepoint.xxebenchmark.util.OSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Component
public class Xml_stf_001 {
    private static final Logger logger = LoggerFactory.getLogger(Xml_stf_001.class);

    //@PostConstruct
    public void parse() {

        logger.info("Xml_stf_001");

        Thread th = new Thread ( new Xml_stf_001_thread() , "Xml_stf_001_thread");
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

class Xml_stf_001_thread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Xml_stf_001_thread.class);

    @Override
    public void run() {
        logger.info("Start thread: " + Thread.currentThread().getName());

        String testId = "xml-stf-" + OSUtil.getOS() + "-" + System.getProperty("java.version") + "-001";
        String testName = "Denial-of-Service - Billion Laughs / default configuration";
        Parser parser = Parser.TransformerFactory;
        String configuration = "";
        Vulnerability vulnerable = Vulnerability.YES; // Initial value, vulnerable payload

        LocalDateTime nowStart = null;
        LocalDateTime nowEnd = null;

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File xmlFile = new File(Objects.requireNonNull(classLoader.getResource("xml/dos.xml")).getFile());
            String xmlString = new String ( Files.readAllBytes( Paths.get(xmlFile.getAbsolutePath()) ) );

            SAXTransformerFactory transformerFactory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();

            Transformer transformer = transformerFactory.newTransformer();
            StreamSource source = new StreamSource(new StringReader(xmlString));
            StringWriter writer = new StringWriter();
            StreamResult target = new StreamResult(writer);

            nowStart = LocalDateTime.now();
            transformer.transform(source, target);
            nowEnd = LocalDateTime.now();

            logger.info(writer.toString());

        } catch (TransformerConfigurationException e) {
            logger.error("TransformerConfigurationException: " + e.getMessageAndLocation());
        } catch (TransformerException e) {
            logger.error("TransformerException: " + e.getMessageAndLocation());
            if(e.getMessage().contains("accessExternalDTD")){
                vulnerable = Vulnerability.NO;
            }
        } catch (IOException e) {
            logger.error("IOException: " + e.getMessage());
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
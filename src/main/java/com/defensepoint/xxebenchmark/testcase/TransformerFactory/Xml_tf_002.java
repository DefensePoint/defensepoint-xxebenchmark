package com.defensepoint.xxebenchmark.testcase.TransformerFactory;

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
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Timer;

//@Component
public class Xml_tf_002 {
    private static final Logger logger = LoggerFactory.getLogger(Xml_tf_002.class);

    //@PostConstruct
    public void parse() {

        logger.info("Xml_tf_002");

        Thread t = new Thread(new Xml_tf_002_thread());
        Timer timer = new Timer();
        timer.schedule(new TimeOutTask(t, timer), Constants.DoS_THREAD_DURATION);
        t.start();
    }
}

class Xml_tf_002_thread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Xml_tf_002_thread.class);

    @Override
    public void run() {
        logger.info("Start thread: " + Thread.currentThread().getName());

        String testId = "xml-tf-" + OSUtil.getOS() + "-" + System.getProperty("java.version") + "-002";
        String testName = "Denial-of-Service - Billion Laughs / FEATURE_SECURE_PROCESSING is enabled";
        Parser parser = Parser.TransformerFactory;
        String configuration = "transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true)";
        Vulnerability vulnerable = Vulnerability.YES; // Initial value, vulnerable payload

        LocalDateTime nowStart = null;
        LocalDateTime nowEnd = null;

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("xml/dos.xml");
            String xmlString = FileUtil.readFromInputStream(inputStream);

            TransformerFactory transformerFactory = javax.xml.transform.TransformerFactory.newInstance();
            transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

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
        } catch (IOException e) {
            logger.error("IOException: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage());
        } finally {
            nowEnd = nowEnd == null ? LocalDateTime.now() : nowEnd;
            assert nowStart != null;
            long diff = ChronoUnit.MILLIS.between(nowStart, nowEnd);

            logger.info(String.format("XML parsing took %d milliseconds.", diff));
            if(diff > Constants.DoS_THRESHOLD) {
                vulnerable = Vulnerability.YES;
                logger.error(String.format("XML parsing takes more than %d (%d) milliseconds.", Constants.DoS_THRESHOLD, diff));
            } else {
                vulnerable = Vulnerability.NO;
            }

            Result result = new Result(testId, testName, parser, configuration, vulnerable);
            Result.results.add(result);
            logger.info(result.toString());
        }
    }
}

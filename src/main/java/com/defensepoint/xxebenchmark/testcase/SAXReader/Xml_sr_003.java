package com.defensepoint.xxebenchmark.testcase.SAXReader;

import com.defensepoint.xxebenchmark.domain.Constants;
import com.defensepoint.xxebenchmark.domain.Parser;
import com.defensepoint.xxebenchmark.domain.Result;
import com.defensepoint.xxebenchmark.domain.Vulnerability;
import com.defensepoint.xxebenchmark.util.OSUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Component
public class Xml_sr_003 {
    private static final Logger logger = LoggerFactory.getLogger(Xml_sr_003.class);

    //@PostConstruct
    public void parse() {

        logger.info("Xml_sr_003");

        Thread th = new Thread ( new Xml_sr_003_thread() , "Xml_sr_003_thread");
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

class Xml_sr_003_thread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Xml_sr_003_thread.class);

    @Override
    public void run() {
        logger.info("Start thread: " + Thread.currentThread().getName());

        String testId = "xml-sr-" + OSUtil.getOS() + "-" + System.getProperty("java.version") + "-003";
        String testName = "Denial-of-Service - Quadratic Blowup / default configuration";
        Parser parser = Parser.SAXReader;
        String configuration = "";
        Vulnerability vulnerable = Vulnerability.YES; // Initial value, vulnerable payload

        LocalDateTime nowStart = null;
        LocalDateTime nowEnd = null;

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File xmlFile = new File(Objects.requireNonNull(classLoader.getResource("xml/quadraticBlowup.xml")).getFile());
            String xmlString = new String ( Files.readAllBytes( Paths.get(xmlFile.getAbsolutePath()) ) );

            SAXReader xmlReader = new SAXReader();

            nowStart = LocalDateTime.now();
            Document document = xmlReader.read(new InputSource(new StringReader(xmlString)));
            nowEnd = LocalDateTime.now();

            Element root = document.getRootElement();

            logger.info(root.getText());


        } catch (DocumentException e) {
            logger.error("DocumentException was thrown: " + e.getMessage());
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
package com.defensepoint.xxebenchmark.testcase.XMLReader;

import com.defensepoint.xxebenchmark.domain.Constants;
import com.defensepoint.xxebenchmark.domain.Parser;
import com.defensepoint.xxebenchmark.domain.Result;
import com.defensepoint.xxebenchmark.domain.Vulnerability;
import com.defensepoint.xxebenchmark.service.TimeOutTask;
import com.defensepoint.xxebenchmark.util.OSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.*;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Timer;

//@Component
public class Xml_xr_004 {
    private static final Logger logger = LoggerFactory.getLogger(Xml_xr_004.class);

    //@PostConstruct
    public void parse() {

        logger.info("Xml_xr_004");

        Thread t = new Thread(new Xml_xr_004_thread());
        Timer timer = new Timer();
        timer.schedule(new TimeOutTask(t, timer), Constants.DoS_THREAD_DURATION);
        t.start();
    }
}

class Xml_xr_004_thread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Xml_xr_004_thread.class);

    @Override
    public void run() {
        logger.info("Start thread: " + Thread.currentThread().getName());

        String testId = "xml-xr-" + OSUtil.getOS() + "-" + System.getProperty("java.version") + "-004";
        String testName = "Denial-of-Service - Quadratic Blowup / DTDs (doctypes) are disallowed";
        Parser parser = Parser.XMLReader;
        String configuration = "reader.setFeature(\"http://apache.org/xml/features/disallow-doctype-decl\", true)";
        Vulnerability vulnerable = Vulnerability.YES; // Initial value. Vulnerable payload.

        LocalDateTime nowStart = null;
        LocalDateTime nowEnd = null;

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File xmlFile = new File(Objects.requireNonNull(classLoader.getResource("xml/quadraticBlowup.xml")).getFile());
            String xmlString = new String ( Files.readAllBytes( Paths.get(xmlFile.getAbsolutePath()) ) );

            FooReaderHandler handler = new FooReaderHandler();
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            reader.setContentHandler(handler);

            StringReader stringReader = new StringReader(xmlString);
            InputSource inputSource = new InputSource(stringReader);
            nowStart = LocalDateTime.now();
            reader.parse(inputSource);
            nowEnd = LocalDateTime.now();

            logger.info(handler.getFoo());

        } catch (IOException e) {
            logger.error("IOException was thrown: " + e.getMessage());
        } catch (SAXNotRecognizedException e) {
            logger.error("SAXNotRecognizedException was thrown: " + e.getMessage());
        } catch (SAXNotSupportedException e) {
            logger.error("SAXNotSupportedException was thrown: " + e.getMessage());
        } catch (SAXException e) {
            logger.error("SAXException was thrown: " + e.getMessage());
            vulnerable = Vulnerability.NO;
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
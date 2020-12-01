package com.defensepoint.xxebenchmark.testcase.SchemaFactory;

import com.defensepoint.xxebenchmark.domain.Constants;
import com.defensepoint.xxebenchmark.domain.Parser;
import com.defensepoint.xxebenchmark.domain.Result;
import com.defensepoint.xxebenchmark.domain.Vulnerability;
import com.defensepoint.xxebenchmark.service.TimeOutTask;
import com.defensepoint.xxebenchmark.util.OSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Timer;

//@Component
public class Xml_sf_004 {
    private static final Logger logger = LoggerFactory.getLogger(Xml_sf_004.class);

    //@PostConstruct
    public void parse() {

        logger.info("Xml_sf_004");

        Thread t = new Thread(new Xml_sf_004_thread());
        Timer timer = new Timer();
        timer.schedule(new TimeOutTask(t, timer), Constants.DoS_THREAD_DURATION);
        t.start();
    }
}

class Xml_sf_004_thread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Xml_sf_004_thread.class);

    @Override
    public void run() {
        logger.info("Start thread: " + Thread.currentThread().getName());

        String testId = "xml-sf-" + OSUtil.getOS() + "-" + System.getProperty("java.version") + "-004";
        String testName = "Denial-of-Service - Quadratic Blowup / FEATURE_SECURE_PROCESSING is enabled";
        Parser parser = Parser.SchemaFactory;
        String configuration = "factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true)";
        Vulnerability vulnerable = Vulnerability.YES; // Initial value, vulnerable payload

        LocalDateTime nowStart = null;
        LocalDateTime nowEnd = null;

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File xsdFile = new File(Objects.requireNonNull(classLoader.getResource("xml/user.xsd")).getFile());
            File xmlFile = new File(Objects.requireNonNull(classLoader.getResource("xml/userWithQuadraticBlowupDtd.xml")).getFile());
            StreamSource source = new StreamSource(xmlFile);

            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            Schema schema = factory.newSchema(xsdFile);

            Validator validator = schema.newValidator();

            nowStart = LocalDateTime.now();
            validator.validate(source);
            nowEnd = LocalDateTime.now();
        } catch (SAXException e) {
            logger.error("SAXException was thrown: " + e.getMessage());
            if(e.getMessage().contains("accessExternalDTD")){
                vulnerable = Vulnerability.NO;
            }
        } catch (IOException e) {
            logger.error("IOException was thrown. Exception occurred, XXE may still possible: " + e.getMessage());
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
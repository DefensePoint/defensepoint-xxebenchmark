package com.defensepoint.xxebenchmark.testcase.Validator;

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
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Timer;

//@Component
public class Xml_vld_002 {
    private static final Logger logger = LoggerFactory.getLogger(Xml_vld_002.class);

    //@PostConstruct
    public void parse() {

        logger.info("Xml_vld_002");

        Thread t = new Thread(new Xml_vld_002_thread());
        Timer timer = new Timer();
        timer.schedule(new TimeOutTask(t, timer), Constants.DoS_THREAD_DURATION);
        t.start();
    }
}

class Xml_vld_002_thread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Xml_vld_002_thread.class);

    @Override
    public void run() {
        logger.info("Start thread: " + Thread.currentThread().getName());

        String testId = "xml-vld-" + OSUtil.getOS() + "-" + System.getProperty("java.version") + "-002";
        String testName = "Denial-of-Service - Billion Laughs / FEATURE_SECURE_PROCESSING is enabled";
        Parser parser = Parser.Validator;
        String configuration = "validator.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true)";
        Vulnerability vulnerable = Vulnerability.YES; // Initial value, vulnerable payload

        LocalDateTime nowStart = null;
        LocalDateTime nowEnd = null;

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream xsdFile = classLoader.getResourceAsStream("xml/user.xsd");
            InputStream xmlFile = classLoader.getResourceAsStream("xml/userWithBillionLaughsDtd.xml");
            StreamSource xsd = new StreamSource(xsdFile);
            StreamSource xml = new StreamSource(xmlFile);

            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(xsd);

            Validator validator = schema.newValidator();
            validator.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            nowStart = LocalDateTime.now();
            validator.validate(xml);
            nowEnd = LocalDateTime.now();
        } catch (SAXException e) {
            logger.error("SAXException was thrown: " + e.getMessage());
        } catch (IOException e) {
            logger.error("IOException was thrown. Exception occurred, XXE may still possible: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Exception was thrown. Exception occurred, XXE may still possible: " + e.getMessage());
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

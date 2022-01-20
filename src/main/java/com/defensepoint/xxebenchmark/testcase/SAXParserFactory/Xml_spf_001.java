package com.defensepoint.xxebenchmark.testcase.SAXParserFactory;

import com.defensepoint.xxebenchmark.domain.Constants;
import com.defensepoint.xxebenchmark.domain.Parser;
import com.defensepoint.xxebenchmark.domain.Vulnerability;
import com.defensepoint.xxebenchmark.service.TimeOutTask;
import com.defensepoint.xxebenchmark.util.OSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Timer;

//@Component
public class Xml_spf_001 {
    private static final Logger logger = LoggerFactory.getLogger(Xml_spf_001.class);

    //@PostConstruct
    public void parse() {

        logger.info("Xml_spf_001");

        Thread t = new Thread(new Xml_spf_001_thread());
        Timer timer = new Timer();
        timer.schedule(new TimeOutTask(t, timer), Constants.DoS_THREAD_DURATION);
        t.start();
    }
}

class Xml_spf_001_thread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Xml_spf_001_thread.class);

    @Override
    public void run() {
        logger.info("Start thread: " + Thread.currentThread().getName());

        String testId = "xml-spf-" + OSUtil.getOS() + "-" + System.getProperty("java.version") + "-001";
        String testName = "Denial-of-Service - Billion Laughs / default configuration";
        Parser parser = Parser.SAXParserFactory;
        String configuration = "";
        Vulnerability vulnerable = Vulnerability.YES; // Initial value. Vulnerable payload.

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("xml/dos.xml");

        SAXParserFactoryUtil.parse(logger, true, false, inputStream, "", testId, testName, parser, configuration, vulnerable);
    }
}

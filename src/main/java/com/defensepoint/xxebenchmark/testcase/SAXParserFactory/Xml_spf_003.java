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
public class Xml_spf_003 {
    private static final Logger logger = LoggerFactory.getLogger(Xml_spf_003.class);

    //@PostConstruct
    public void parse() {

        logger.info("Xml_spf_003");

        Thread t = new Thread(new Xml_spf_003_thread());
        Timer timer = new Timer();
        timer.schedule(new TimeOutTask(t, timer), Constants.DoS_THREAD_DURATION);
        t.start();
    }
}

class Xml_spf_003_thread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Xml_spf_003_thread.class);

    @Override
    public void run() {
        logger.info("Start thread: " + Thread.currentThread().getName());

        String testId = "xml-spf-" + OSUtil.getOS() + "-" + System.getProperty("java.version") + "-003";
        String testName = "Denial-of-Service - Quadratic Blowup / default configuration";
        Parser parser = Parser.SAXParserFactory;
        String configuration = "";
        Vulnerability vulnerable = Vulnerability.YES; // Initial value. Vulnerable payload.

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("xml/quadraticBlowup.xml");

        SAXParserFactoryUtil.parse(logger, true, false, inputStream, "", testId, testName, parser, configuration, vulnerable);
    }
}

package com.defensepoint.xxebenchmark.testcase.SAXParserFactory;

import com.defensepoint.xxebenchmark.domain.Parser;
import com.defensepoint.xxebenchmark.domain.Vulnerability;
import com.defensepoint.xxebenchmark.util.OSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

//@Component
public class Xml_spf_014 {
    private static final Logger logger = LoggerFactory.getLogger(Xml_spf_014.class);

    //@PostConstruct
    public void parse() {

        logger.info("Xml_spf_014");

        String testId = "xml-spf-" + OSUtil.getOS() + "-" + System.getProperty("java.version") + "-014";
        String testName = "Remote File Inclusion - SSRF (Server Side Request Forgery) / sanitized";
        Parser parser = Parser.SAXParserFactory;
        String configuration = "";
        Vulnerability vulnerable = Vulnerability.YES; // Initial value. Vulnerable payload.

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("xml/remoteFileInclusion.xml");

        SAXParserFactoryUtil.parse(logger, false, true, inputStream, "XXE", testId, testName, parser, configuration, vulnerable);
    }


}

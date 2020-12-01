package com.defensepoint.xxebenchmark.testcase.SchemaFactory;

import com.defensepoint.xxebenchmark.domain.Parser;
import com.defensepoint.xxebenchmark.domain.Result;
import com.defensepoint.xxebenchmark.domain.Vulnerability;
import com.defensepoint.xxebenchmark.util.JavaUtil;
import com.defensepoint.xxebenchmark.util.OSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.xml.XMLConstants;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.util.Objects;

//@Component
public class Xml_sf_018 {
    private static final Logger logger = LoggerFactory.getLogger(Xml_sf_018.class);

    //@PostConstruct
    public void parse() {

        logger.info("Xml_sf_018");

        String testId = "xml-sf-" + OSUtil.getOS() + "-" + JavaUtil.getJavaVersion() + "-018";
        String testName = "Remote File Inclusion / FEATURE_SECURE_PROCESSING is enabled";
        Parser parser = Parser.SchemaFactory;
        String configuration = "factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true)";
        Vulnerability vulnerable = Vulnerability.YES;

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File xsdFile = new File(Objects.requireNonNull(classLoader.getResource("xml/CompanyRemote.xsd")).getFile());

            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.newSchema(xsdFile);

        } catch (SAXException e) {
            logger.error("SAXException was thrown: " + e.getMessage());
            if(e.getMessage().contains("accessExternalSchema")){
                vulnerable = Vulnerability.NO;
            }
        } finally {
            Result result = new Result(testId, testName, parser, configuration, vulnerable);
            Result.results.add(result);
            logger.info(result.toString());
        }
    }
}

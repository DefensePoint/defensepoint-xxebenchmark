package com.defensepoint.xxebenchmark.testcase.Validator;

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
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

//@Component
public class Xml_vld_015 {
    private static final Logger logger = LoggerFactory.getLogger(Xml_vld_015.class);

    //@PostConstruct
    public void parse() {

        logger.info("Xml_vld_015");

        String testId = "xml-vld-" + OSUtil.getOS() + "-" + JavaUtil.getJavaVersion() + "-015";
        String testName = "Local File Inclusion / OWASP Configuration";
        Parser parser = Parser.Validator;
        String configuration = "validator.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, \"\") " +
                "validator.setProperty(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, \"\")";
        Vulnerability vulnerable = Vulnerability.YES;

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream xsdFile = classLoader.getResourceAsStream("xml/user.xsd");
            InputStream xmlFile = classLoader.getResourceAsStream("xml/userLocalFileInclusion.xml");
            StreamSource xsd = new StreamSource(xsdFile);
            StreamSource xml = new StreamSource(xmlFile);

            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(xsd);

            Validator validator = schema.newValidator();
            validator.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            validator.setProperty(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");

            validator.validate(xml);
        } catch (SAXException e) {
            logger.error("SAXException was thrown: " + e.getMessage());
            if(e.getMessage().contains("accessExternalStylesheet")){
                vulnerable = Vulnerability.NO;
            }
        } catch (IOException e) {
            logger.error("IOException was thrown. Exception occurred, XXE may still possible: " + e.getMessage());
        } finally {
            Result result = new Result(testId, testName, parser, configuration, vulnerable);
            Result.results.add(result);
            logger.info(result.toString());
        }
    }
}

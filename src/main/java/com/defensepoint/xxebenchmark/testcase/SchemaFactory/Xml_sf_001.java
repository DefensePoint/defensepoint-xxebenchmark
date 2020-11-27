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
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Component
public class Xml_sf_001 {
    private static final Logger logger = LoggerFactory.getLogger(Xml_sf_001.class);

    //@PostConstruct
    public void validate() {

        logger.info("Xml_sf_001");

        String testId = "xml-sf-" + OSUtil.getOS() + "-" + JavaUtil.getJavaVersion() + "-001";
        String testName = "Validate Schema";
        Parser parser = Parser.SchemaFactory;
        String configuration = "";
        Vulnerability vulnerable = Vulnerability.NO;

        Result result = new Result(testId, testName, parser, configuration, vulnerable);
        Result.results.add(result);

        ClassLoader classLoader = getClass().getClassLoader();
        File xsdFile = new File(Objects.requireNonNull(classLoader.getResource("xml/user.xsd")).getFile());
        File xmlFile = new File(Objects.requireNonNull(classLoader.getResource("xml/user.xml")).getFile());

        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
//            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            Schema schema = factory.newSchema(xsdFile);
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xmlFile));
        } catch (SAXException e) {
            logger.error("SAXException was thrown: " + e.getMessage());
        } catch (IOException e) {
            logger.error("Exception was thrown. Exception occurred, XXE may still possible: " + e.getMessage());
        }
    }
}

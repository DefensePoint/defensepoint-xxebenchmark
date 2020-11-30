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
public class Xml_sf_009 {
    private static final Logger logger = LoggerFactory.getLogger(Xml_sf_009.class);

    //@PostConstruct
    public void validate() {

        logger.info("Xml_sf_009");

        String testId = "xml-sf-" + OSUtil.getOS() + "-" + JavaUtil.getJavaVersion() + "-009";
        String testName = "Remote Schema / default configuration";
        Parser parser = Parser.SchemaFactory;
        String configuration = "";
        Vulnerability vulnerable = Vulnerability.YES;

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File xsdFile = new File(Objects.requireNonNull(classLoader.getResource("xml/user.xsd")).getFile());
            File xmlFile = new File(Objects.requireNonNull(classLoader.getResource("xml/userWithRemoteSchemaDtd.xml")).getFile());
            StreamSource source = new StreamSource(xmlFile);

            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(xsdFile);

            Validator validator = schema.newValidator();
            validator.validate(source);
        } catch (SAXException e) {
            logger.error("SAXException was thrown: " + e.getMessage());
            if(e.getMessage().contains("accessExternalDTD")){
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

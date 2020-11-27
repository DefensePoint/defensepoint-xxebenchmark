package com.defensepoint.xxebenchmark.testcase.SchemaFactory;

import com.defensepoint.xxebenchmark.domain.Parser;
import com.defensepoint.xxebenchmark.domain.Result;
import com.defensepoint.xxebenchmark.domain.Vulnerability;
import com.defensepoint.xxebenchmark.util.JavaUtil;
import com.defensepoint.xxebenchmark.util.OSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.XMLConstants;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.StringWriter;
import java.util.Objects;

@Component
public class Xml_sf_002 {
    private static final Logger logger = LoggerFactory.getLogger(Xml_sf_002.class);

    //@PostConstruct
    public void validate() {

        logger.info("Xml_sf_002");

        String testId = "xml-sf-" + OSUtil.getOS() + "-" + JavaUtil.getJavaVersion() + "-002";
        String testName = "Xslt Transform";
        Parser parser = Parser.SchemaFactory;
        String configuration = "";
        Vulnerability vulnerable = Vulnerability.NO;

        Result result = new Result(testId, testName, parser, configuration, vulnerable);
        Result.results.add(result);

        ClassLoader classLoader = getClass().getClassLoader();
        File xsltFile = new File(Objects.requireNonNull(classLoader.getResource("xml/foo.xslt")).getFile());
        File xmlFile = new File(Objects.requireNonNull(classLoader.getResource("xml/foo.xml")).getFile());
        Source xslt = new StreamSource(xsltFile);
        Source xml  = new StreamSource(xmlFile);

        StringWriter writer = new StringWriter();
        StreamResult target = new StreamResult(writer);

        try {
            TransformerFactory factory = TransformerFactory.newInstance();
//            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            Transformer transformer = factory.newTransformer(xslt);
            transformer.transform(xml, target);

            logger.info("-" + writer.toString());
        } catch (TransformerConfigurationException e) {
            logger.error("TransformerConfigurationException was thrown. " + e.getMessage());
        } catch (TransformerException e) {
            logger.error("TransformerException was thrown. " + e.getMessage());
        }
    }
}

package com.defensepoint.xxebenchmark.testcase.TransformerFactory;

import com.defensepoint.xxebenchmark.domain.Parser;
import com.defensepoint.xxebenchmark.domain.Result;
import com.defensepoint.xxebenchmark.domain.Vulnerability;
import com.defensepoint.xxebenchmark.util.OSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@Component
public class Xml_tf_005 {
    private static final Logger logger = LoggerFactory.getLogger(Xml_tf_005.class);

    //@PostConstruct
    public void parse() {

        logger.info("Xml_tf_005");

        String testId = "xml-tf-" + OSUtil.getOS() + "-" + System.getProperty("java.version") + "-005";
        String testName = "File Disclosure / default configuration";
        Parser parser = Parser.TransformerFactory;
        String configuration = "";
        Vulnerability vulnerable = Vulnerability.YES; // Initial value, vulnerable payload

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File xmlFile = new File(Objects.requireNonNull(classLoader.getResource("xml/fileDisclosure.xml")).getFile());
            String xmlString = new String ( Files.readAllBytes( Paths.get(xmlFile.getAbsolutePath()) ) );

            TransformerFactory transformerFactory = javax.xml.transform.TransformerFactory.newInstance();

            Transformer transformer = transformerFactory.newTransformer();
            StreamSource source = new StreamSource(new StringReader(xmlString));
            StringWriter writer = new StringWriter();
            StreamResult target = new StreamResult(writer);

            transformer.transform(source, target);

            logger.info(writer.toString());

        } catch (TransformerConfigurationException e) {
            logger.error("TransformerConfigurationException: " + e.getMessageAndLocation());
        } catch (TransformerException e) {
            logger.error("TransformerException: " + e.getMessageAndLocation());
            vulnerable = Vulnerability.NO;
        } catch (IOException e) {
            logger.error("IOException: " + e.getMessage());
        } finally {
            Result result = new Result(testId, testName, parser, configuration, vulnerable);
            Result.results.add(result);
            logger.info("Result: " + result.toString());
        }
    }
}

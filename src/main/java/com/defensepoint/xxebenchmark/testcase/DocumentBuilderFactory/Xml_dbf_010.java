package com.defensepoint.xxebenchmark.testcase.DocumentBuilderFactory;

import com.defensepoint.xxebenchmark.domain.Parser;
import com.defensepoint.xxebenchmark.domain.Result;
import com.defensepoint.xxebenchmark.domain.Vulnerability;
import com.defensepoint.xxebenchmark.util.OSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Component
public class Xml_dbf_010 {
    private static final Logger logger = LoggerFactory.getLogger(Xml_dbf_010.class);

    @PostConstruct
    public void parse() {

        logger.info("Xml_dbf_010");

        String testId = "xml-dbf-" + OSUtil.getOS() + "-" + System.getProperty("java.version") + "-010";
        String testName = "Remote Schema / DTDs (doctypes) are disallowed";
        Parser parser = Parser.DocumentBuilderFactory;
        String configuration = "factory.setFeature(\"http://apache.org/xml/features/disallow-doctype-decl\", true)";
        final Vulnerability[] vulnerable = {Vulnerability.NO};

        ClassLoader classLoader = getClass().getClassLoader();
        File xmlFile = new File(Objects.requireNonNull(classLoader.getResource("xml/remoteWrongSchema.xml")).getFile());

        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);

        //API to obtain DOM Document instance
        DocumentBuilder builder;

        try {
            //Disallow dtd
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            builder.setErrorHandler(new ErrorHandler()
            {
                @Override
                public void fatalError(SAXParseException exception)
                {
                    logger.error("SAXParseException was thrown: " + exception.getMessage());
                }

                @Override
                public void error(SAXParseException exception)
                {
                    vulnerable[0] = Vulnerability.YES;
                    logger.error("SAXParseException was thrown: " + exception.getMessage());
                }

                @Override
                public void warning(SAXParseException exception)
                {
                    logger.error("SAXParseException was thrown: " + exception.getMessage());
                }
            });

            //Parse the content to Document object
            builder.parse(xmlFile);

        } catch (ParserConfigurationException e) {
            logger.error("ParserConfigurationException was thrown: " + e.getMessage());
        } catch (SAXException e) {
            logger.error("SAXException was thrown: " + e.getMessage());
        } catch (IOException e) {
            logger.error("IOException was thrown. IOException occurred, XXE may still possible: " + e.getMessage());
        } finally {
            Result result = new Result(testId, testName, parser, configuration, vulnerable[0]);
            Result.results.add(result);
            logger.info("Result: " + result.toString());
        }
    }
}

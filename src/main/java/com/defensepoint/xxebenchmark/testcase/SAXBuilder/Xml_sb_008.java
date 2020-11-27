package com.defensepoint.xxebenchmark.testcase.SAXBuilder;

import com.defensepoint.xxebenchmark.domain.Parser;
import com.defensepoint.xxebenchmark.domain.Result;
import com.defensepoint.xxebenchmark.domain.Vulnerability;
import com.defensepoint.xxebenchmark.util.OSUtil;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@Component
public class Xml_sb_008 {
    private static final Logger logger = LoggerFactory.getLogger(Xml_sb_008.class);

    //@PostConstruct
    public void parse() {

        logger.info("Xml_sb_008");

        String testId = "xml-sb-" + OSUtil.getOS() + "-" + System.getProperty("java.version") + "-008";
        String testName = "Local Schema / DTDs (doctypes) are disallowed";
        Parser parser = Parser.SAXBuilder;
        String configuration = "builder.setFeature(\"http://apache.org/xml/features/disallow-doctype-decl\", true)";
        Vulnerability vulnerable = Vulnerability.YES; // Initial value. Vulnerable payload.

        String foo = "";

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File xmlFile = new File(Objects.requireNonNull(classLoader.getResource("xml/localSchema.xml")).getFile());
            String xmlString = new String ( Files.readAllBytes( Paths.get(xmlFile.getAbsolutePath()) ) );

            SAXBuilder builder = new SAXBuilder();
            builder.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            builder.setValidation(true);

            StringReader stringReader = new StringReader(xmlString);
            InputSource inputSource = new InputSource(stringReader);
            Document document = builder.build(inputSource);

            foo = document.getRootElement().getText();

            logger.info(foo);

        } catch (IOException e) {
            logger.error("IOException was thrown. IOException occurred, XXE may still possible: " + e.getMessage());
        } catch (JDOMException e) {
            logger.error("JDOMException was thrown. " + e.getMessage());
        } finally {
            vulnerable = foo.equalsIgnoreCase("hello") ? Vulnerability.YES : Vulnerability.NO;

            Result result = new Result(testId, testName, parser, configuration, vulnerable);
            Result.results.add(result);
            logger.info(result.toString());
        }
    }
}

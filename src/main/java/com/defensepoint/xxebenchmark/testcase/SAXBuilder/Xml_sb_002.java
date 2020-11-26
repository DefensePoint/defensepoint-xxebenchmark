package com.defensepoint.xxebenchmark.testcase.SAXBuilder;

import com.defensepoint.xxebenchmark.domain.Constants;
import com.defensepoint.xxebenchmark.domain.Parser;
import com.defensepoint.xxebenchmark.domain.Result;
import com.defensepoint.xxebenchmark.domain.Vulnerability;
import com.defensepoint.xxebenchmark.util.OSUtil;
import org.jdom2.JDOMException;
import org.jdom2.Document;
import org.jdom2.Element;
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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static com.defensepoint.xxebenchmark.domain.Parser.SAXBuilder;

@Component
public class Xml_sb_002 {
    private static final Logger logger = LoggerFactory.getLogger(Xml_sb_002.class);

    //@PostConstruct
    public void parse() {

        logger.info("Xml_sb_002");

        String testId = "xml-sb-" + OSUtil.getOS() + "-" + System.getProperty("java.version") + "-002";
        String testName = "Denial-of-Service - Billion Laughs / DTDs (doctypes) are disallowed";
        Parser parser = SAXBuilder;
        String configuration = "builder.setFeature(\"http://apache.org/xml/features/disallow-doctype-decl\",true)";
        Vulnerability vulnerable = Vulnerability.YES; // Initial value. Vulnerable payload.

        LocalDateTime nowStart = null;
        LocalDateTime nowEnd = null;

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File xmlFile = new File(Objects.requireNonNull(classLoader.getResource("xml/dos.xml")).getFile());
            String xmlString = new String ( Files.readAllBytes( Paths.get(xmlFile.getAbsolutePath()) ) );

            SAXBuilder builder = new SAXBuilder();
            builder.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);

            nowStart = LocalDateTime.now();
            Document document = builder.build(new InputSource(new StringReader(xmlString)));
            nowEnd = LocalDateTime.now();

            Element root = document.getRootElement();

            logger.info(root.getText());

        } catch (IOException e) {
            logger.error("IOException was thrown. IOException occurred, XXE may still possible: " + e.getMessage());
        } catch (JDOMException e) {
            logger.error("JDOMException was thrown. " + e.getMessage());
            vulnerable = Vulnerability.NO;
        } finally {
            nowEnd = nowEnd == null ? LocalDateTime.now() : nowEnd;
            assert nowStart != null;
            long diff = ChronoUnit.MILLIS.between(nowStart, nowEnd);

            logger.info(String.format("XML parsing took %d milliseconds.", diff));
            if(diff > Constants.DoS_THRESHOLD) {
                vulnerable = Vulnerability.YES;
                logger.error(String.format("XML parsing takes more than %d (%d) milliseconds.", Constants.DoS_THRESHOLD, diff));
            }

            Result result = new Result(testId, testName, parser, configuration, vulnerable);
            Result.results.add(result);
            logger.info(result.toString());
        }
    }
}

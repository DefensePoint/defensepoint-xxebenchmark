package com.defensepoint.xxebenchmark.testcase.SAXParserFactory;

import com.defensepoint.xxebenchmark.domain.Constants;
import com.defensepoint.xxebenchmark.domain.Parser;
import com.defensepoint.xxebenchmark.domain.Result;
import com.defensepoint.xxebenchmark.domain.Vulnerability;
import com.defensepoint.xxebenchmark.util.FileUtil;
import org.slf4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class SAXParserFactoryUtil {

    public SAXParserFactoryUtil(){}

    public static void parse(Logger logger, boolean isDos, boolean sanitize, InputStream inputStream, String xmlOutput, String testId, String testName, Parser parser, String configuration, Vulnerability vulnerable) {
        String foo = "";
        String xmlString;

        LocalDateTime nowStart = null;
        LocalDateTime nowEnd = null;

        try {
            xmlString = FileUtil.readFromInputStream(inputStream);
        } catch (IOException e) {
            logger.error("IOException was thrown: " + e.getMessage());
            return;
        }

        try {

            FooHandler handler = new FooHandler();

            SAXParserFactory factory = SAXParserFactory.newInstance();
            if(sanitize) {
                factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);
                configuration = "factory.setFeature(\"http://apache.org/xml/features/disallow-doctype-decl\",true) ";
            }

            SAXParser saxParser = factory.newSAXParser();
            if(sanitize) {
//                saxParser.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
//                saxParser.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
//                saxParser.setProperty(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");

                // This gives the same result as ACCESS_EXTERNAL_STYLESHEET even if it's a different sanitizer.
                //saxParser.setProperty("javax.xml.stream.isReplacingEntityReferences", false);

                // Lower the "default entity expansion" and "default entity size" limits.
                // The default entity expansion limit is 64K
                // The default entity size limit is 5M
                // saxParser.setProperty("http://www.oracle.com/xml/jaxp/properties/entityExpansionLimit", 2000);
                // saxParser.setProperty("http://www.oracle.com/xml/jaxp/properties/totalEntitySizeLimit", 100000);

                // Set limits on a system property. You need to set these properties in the main method
                // System.setProperty("jdk.xml.entityExpansionLimit", "2000");
                // System.setProperty("jdk.xml.totalEntitySizeLimit", "100000");
            }

            nowStart = LocalDateTime.now();
            saxParser.parse(new InputSource(new StringReader(xmlString)), handler);
            nowEnd = LocalDateTime.now();

            foo = handler.getFoo();
            logger.info(foo);

        } catch (SAXNotRecognizedException e) {
            logger.error("SAXNotRecognizedException was thrown: " + e.getMessage());
        } catch (SAXNotSupportedException e) {
            logger.error("SAXNotSupportedException was thrown: " + e.getMessage());
        } catch (SAXException e) {
            logger.error("SAXException was thrown: " + e.getMessage());
            vulnerable = Vulnerability.NO;
        } catch (Exception e) {
            logger.error("Exception occurred: " + e.getMessage());
        } finally {

            if(isDos) {
                nowEnd = nowEnd == null ? LocalDateTime.now() : nowEnd;
                assert nowStart != null;
                long diff = ChronoUnit.MILLIS.between(nowStart, nowEnd);

                logger.info(String.format("XML parsing took %d milliseconds.", diff));
                if(diff > Constants.DoS_THRESHOLD) {
                    vulnerable = Vulnerability.YES;
                    logger.error(String.format("XML parsing takes more than %d (%d) milliseconds.", Constants.DoS_THRESHOLD, diff));
                }
            } else {
                if(foo != null) {
                    vulnerable = foo.equalsIgnoreCase(xmlOutput) ? Vulnerability.YES : Vulnerability.NO;
                } else {
                    vulnerable = Vulnerability.NO;
                }
            }

            Result result = new Result(testId, testName, parser, configuration, vulnerable);
            Result.results.add(result);
            logger.info("Result: " + result.toString());
        }
    }
}

package com.defensepoint.xxebenchmark.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Order(0)
public class SystemProperties {
    private static final Logger logger = LoggerFactory.getLogger(SystemProperties.class);

    //@PostConstruct
    private void setSystemProperties(){
        logger.info("Set system properties");

        System.setProperty("jdk.xml.entityExpansionLimit", "6400000");
        System.setProperty("jdk.xml.elementAttributeLimit", "1002");
        System.setProperty("jdk.xml.maxGeneralEntitySizeLimit", "1003");
        System.setProperty("jdk.xml.totalEntitySizeLimit", "500000000");
        System.setProperty("jdk.xml.maxXMLNameLimit", "1005");
        System.setProperty("jdk.xml.maxElementDepth", "1006");
        System.setProperty("jdk.xml.entityReplacementLimit", "30000000");
        System.setProperty("jdk.xml.maxOccurLimit", "1008");
        System.setProperty("jdk.xml.maxParameterEntitySizeLimit", "1009");
        System.setProperty("jdk.xml.entityReplacementLimit", "1010");
    }
}

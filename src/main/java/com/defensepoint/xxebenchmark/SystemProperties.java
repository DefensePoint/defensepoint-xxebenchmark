package com.defensepoint.xxebenchmark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Order(0)
public class SystemProperties {
    private static final Logger logger = LoggerFactory.getLogger(SystemProperties.class);

    @PostConstruct
    private void setSystemProperties(){
        logger.info("Set system properties");

//        System.setProperty("jdk.xml.entityExpansionLimit", "1001");
//        System.setProperty("jdk.xml.elementAttributeLimit", "1002");
//        System.setProperty("jdk.xml.maxGeneralEntitySizeLimit", "1003");
//        System.setProperty("jdk.xml.totalEntitySizeLimit", "1004");
//        System.setProperty("jdk.xml.maxXMLNameLimit", "1005");
//        System.setProperty("jdk.xml.maxElementDepth", "1006");
//        System.setProperty("jdk.xml.entityReplacementLimit", "1007");
//        System.setProperty("jdk.xml.maxOccurLimit", "1008");
//        System.setProperty("jdk.xml.maxParameterEntitySizeLimit", "1009");
//        System.setProperty("jdk.xml.entityReplacementLimit", "1010");
    }
}

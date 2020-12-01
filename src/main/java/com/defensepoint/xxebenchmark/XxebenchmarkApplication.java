package com.defensepoint.xxebenchmark;

import com.defensepoint.xxebenchmark.testcase.TransformerFactory.*;
import com.defensepoint.xxebenchmark.testcase.DocumentBuilderFactory.*;
import com.defensepoint.xxebenchmark.testcase.JAXBUnmarshaller.*;
import com.defensepoint.xxebenchmark.testcase.SAXBuilder.*;
import com.defensepoint.xxebenchmark.testcase.SAXReader.*;
import com.defensepoint.xxebenchmark.testcase.SAXTransformerFactory.*;
import com.defensepoint.xxebenchmark.testcase.SchemaFactory.*;
import com.defensepoint.xxebenchmark.testcase.Validator.*;
import com.defensepoint.xxebenchmark.testcase.XMLInputFactory.*;
import com.defensepoint.xxebenchmark.testcase.XMLReader.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class XxebenchmarkApplication {

    public static void main(String[] args) {
        SpringApplication.run(XxebenchmarkApplication.class, args);

        TestDocumentBuilderFactory();
        TestJAXBUnmarshaller();
        TestSAXBuilder();
        TestSAXReader();
        TestSAXTransformerFactory();
        TestSchemaFactory();
        TestTransformerFactory();
        TestValidation();
        TestXMLInputFactory();
        TestXMLReader();
    }

    private static void TestDocumentBuilderFactory() {
        new Xml_dbf_001().parse();
        new Xml_dbf_002().parse();
        new Xml_dbf_003().parse();
        new Xml_dbf_004().parse();
        new Xml_dbf_005().parse();
        new Xml_dbf_006().parse();
        new Xml_dbf_007().parse();
        new Xml_dbf_008().parse();
        new Xml_dbf_009().parse();
        new Xml_dbf_010().parse();
        new Xml_dbf_011().parse();
        new Xml_dbf_012().parse();
        new Xml_dbf_013().parse();
        new Xml_dbf_014().parse();
    }

    private static void TestJAXBUnmarshaller() {
        new Xml_jaxu_002().parse();
        new Xml_jaxu_004().parse();
        new Xml_jaxu_006().parse();
        new Xml_jaxu_008().parse();
        new Xml_jaxu_010().parse();
        new Xml_jaxu_012().parse();
        new Xml_jaxu_014().parse();
    }

    private static void TestSAXBuilder() {
        new Xml_sb_001().parse();
        new Xml_sb_002().parse();
        new Xml_sb_003().parse();
        new Xml_sb_004().parse();
        new Xml_sb_005().parse();
        new Xml_sb_006().parse();
        new Xml_sb_007().parse();
        new Xml_sb_008().parse();
        new Xml_sb_009().parse();
        new Xml_sb_010().parse();
        new Xml_sb_011().parse();
        new Xml_sb_012().parse();
        new Xml_sb_013().parse();
        new Xml_sb_014().parse();
    }

    private static void TestSAXReader() {
        new Xml_sr_001().parse();
        new Xml_sr_002().parse();
        new Xml_sr_003().parse();
        new Xml_sr_004().parse();
        new Xml_sr_005().parse();
        new Xml_sr_006().parse();
        new Xml_sr_007().parse();
        new Xml_sr_008().parse();
        new Xml_sr_009().parse();
        new Xml_sr_010().parse();
        new Xml_sr_011().parse();
        new Xml_sr_012().parse();
        new Xml_sr_013().parse();
        new Xml_sr_014().parse();
    }

    private static void TestSAXTransformerFactory() {
        new Xml_stf_001().parse();
        new Xml_stf_002().parse();
        new Xml_stf_003().parse();
        new Xml_stf_004().parse();
        new Xml_stf_005().parse();
        new Xml_stf_006().parse();
        new Xml_stf_007().parse();
        new Xml_stf_008().parse();
        new Xml_stf_009().parse();
        new Xml_stf_010().parse();
        new Xml_stf_011().parse();
        new Xml_stf_012().parse();
        new Xml_stf_013().parse();
        new Xml_stf_014().parse();
        new Xml_stf_015().parse();
        new Xml_stf_016().parse();
        new Xml_stf_017().parse();
        new Xml_stf_018().parse();
    }

    private static void TestSchemaFactory() {
        new Xml_sf_001().parse();
        new Xml_sf_002().parse();
        new Xml_sf_003().parse();
        new Xml_sf_004().parse();
        new Xml_sf_005().parse();
        new Xml_sf_006().parse();
        new Xml_sf_007().parse();
        new Xml_sf_008().parse();
        new Xml_sf_009().parse();
        new Xml_sf_010().parse();
        new Xml_sf_011().parse();
        new Xml_sf_012().parse();
        new Xml_sf_013().parse();
        new Xml_sf_014().parse();
        new Xml_sf_015().parse();
        new Xml_sf_016().parse();
        new Xml_sf_017().parse();
        new Xml_sf_018().parse();
    }

    private static void TestTransformerFactory() {
        new Xml_tf_001().parse();
        new Xml_tf_002().parse();
        new Xml_tf_003().parse();
        new Xml_tf_004().parse();
        new Xml_tf_005().parse();
        new Xml_tf_006().parse();
        new Xml_tf_007().parse();
        new Xml_tf_008().parse();
        new Xml_tf_009().parse();
        new Xml_tf_010().parse();
        new Xml_tf_011().parse();
        new Xml_tf_012().parse();
        new Xml_tf_013().parse();
        new Xml_tf_014().parse();
        new Xml_tf_015().parse();
        new Xml_tf_016().parse();
        new Xml_tf_017().parse();
        new Xml_tf_018().parse();
    }

    private static void TestValidation() {
        new Xml_vld_001().parse();
        new Xml_vld_002().parse();
        new Xml_vld_003().parse();
        new Xml_vld_004().parse();
        new Xml_vld_005().parse();
        new Xml_vld_006().parse();
        new Xml_vld_007().parse();
        new Xml_vld_008().parse();
        new Xml_vld_009().parse();
        new Xml_vld_010().parse();
        new Xml_vld_011().parse();
        new Xml_vld_012().parse();
        new Xml_vld_013().parse();
        new Xml_vld_014().parse();
    }

    private static void TestXMLInputFactory() {
        new Xml_xif_001().parse();
        new Xml_xif_002().parse();
        new Xml_xif_003().parse();
        new Xml_xif_004().parse();
        new Xml_xif_005().parse();
        new Xml_xif_006().parse();
        new Xml_xif_007().parse();
        new Xml_xif_008().parse();
        new Xml_xif_009().parse();
        new Xml_xif_010().parse();
        new Xml_xif_011().parse();
        new Xml_xif_012().parse();
        new Xml_xif_013().parse();
        new Xml_xif_014().parse();
    }

    private static void TestXMLReader() {
        new Xml_xr_001().parse();
        new Xml_xr_002().parse();
        new Xml_xr_003().parse();
        new Xml_xr_004().parse();
        new Xml_xr_005().parse();
        new Xml_xr_006().parse();
        new Xml_xr_007().parse();
        new Xml_xr_008().parse();
        new Xml_xr_009().parse();
        new Xml_xr_010().parse();
        new Xml_xr_011().parse();
        new Xml_xr_012().parse();
        new Xml_xr_013().parse();
        new Xml_xr_014().parse();
    }

}

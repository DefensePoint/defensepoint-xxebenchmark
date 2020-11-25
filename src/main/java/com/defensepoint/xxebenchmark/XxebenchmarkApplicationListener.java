package com.defensepoint.xxebenchmark;

import com.defensepoint.xxebenchmark.domain.Result;
import com.defensepoint.xxebenchmark.testcase.XMLInputFactory.Xml_xif_003;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.Writer;

@Component
public class XxebenchmarkApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(XxebenchmarkApplicationListener.class);
    private static final String CSV_LOCATION = "output.csv";

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        logger.info("ApplicationListener#onApplicationEvent()");

        createResultsCsv();
    }

    private void createResultsCsv() {
        try {
            Writer writer  = new FileWriter(CSV_LOCATION);

            // Create Mapping Strategy to arrange the
            // column name in order
            ColumnPositionMappingStrategy mappingStrategy = new ColumnPositionMappingStrategy();
            mappingStrategy.setType(Result.class);

            // Arrange column name as provided in below array.
            String[] columns = new String[]{ "testId", "testName", "parser", "configuration", "vulnerable" };
            mappingStrategy.setColumnMapping(columns);

            StatefulBeanToCsv sbc = new StatefulBeanToCsvBuilder(writer)
                    .withSeparator('|')
                    .withMappingStrategy(mappingStrategy)
                    .build();

            sbc.write(Result.results);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
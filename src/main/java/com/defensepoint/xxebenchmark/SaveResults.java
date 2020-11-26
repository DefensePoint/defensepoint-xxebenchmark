package com.defensepoint.xxebenchmark;

import com.defensepoint.xxebenchmark.domain.Result;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.FileWriter;
import java.io.Writer;

@Component
public class SaveResults {

    private static final Logger logger = LoggerFactory.getLogger(SaveResults.class);
    private static final String CSV_LOCATION = "output.csv";

    @PreDestroy
    public void saveResultsToCsvFile() {

        logger.info("Save results to csv file");

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
        System.out.println("Bye Bye");
    }
}

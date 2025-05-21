package com.GAssociatesWeb.GAssociates.Service.SequenceServices;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceConstants.MAX_VALUE;
import static com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceConstants.MIN_VALUE;

@Component
public class DatabaseSequenceInitializer {

//    @Autowired
//    private SequenceService sequenceService;
//
//
//    @PostConstruct
//    public void initSequences() {
//
//        // List of tables and their respective ID column names
//        Map<String, Long> tableToValueMap = Map.of(
//                "buildstatus_master", MIN_VALUE
//                // Add more mappings as needed
//        );
//
//        // Ensure sequences exist for each table
//        tableToValueMap.forEach((tableName, Value) -> {
//            String sequenceName = tableName + "_id_seq"; // Assuming naming convention
//            String idColumnName = tableName + ".id"; // This needs to be adjusted if idColumnName is not used for SQL command
//            try {
//                sequenceService.ensureSequenceExists(sequenceName, idColumnName, Value);
//            }catch (Exception e){
//                System.out.println(e);
//            }
//        });
//    }
@Autowired
private SequenceService sequenceService;

    private static class TableInfo {
        String idColumnName;
        Long Value;

        public TableInfo(String idColumnName, Long Value) {
            this.idColumnName = idColumnName;
            this.Value = Value;
        }
    }

    @PostConstruct
    public void initSequences() {
        Map<String, TableInfo> tableToInfoMap = new HashMap<>();

        // Add entries to the map with specific ID column names and initial values
        tableToInfoMap.put("buildstatus_master", new TableInfo("id", MIN_VALUE));
        tableToInfoMap.put("ownertype_master", new TableInfo("ownertype_id", MIN_VALUE));
        tableToInfoMap.put("unitfloorno_master", new TableInfo("id", MIN_VALUE));
        tableToInfoMap.put("property_olddetails", new TableInfo("pod_refno_vc", MAX_VALUE));
       // tableToInfoMap.put("constructionclass_master", new TableInfo("ccm_conclassid_vc", MAX_VALUE));
        tableToInfoMap.put("property_details", new TableInfo("pd_newpropertyno_vc", MAX_VALUE));
        tableToInfoMap.put("unit_details", new TableInfo("id", MAX_VALUE));
        tableToInfoMap.put("unit_builtup", new TableInfo("id", MAX_VALUE));
        //tableToInfoMap.put("zone_master", new TableInfo("id", MIN_VALUE));
        //tableToInfoMap.put("unitno_master", new TableInfo("id", MIN_VALUE));
        // Add more mappings as needed

        // Ensure sequences exist for each table
        tableToInfoMap.forEach((tableName, tableInfo) -> {
            String sequenceName = tableName + "_"+tableInfo.idColumnName+"_seq"; // Assuming naming convention
            try {
                System.out.println("Ensuring sequence: " + sequenceName + " for " + tableName + "." + tableInfo.idColumnName + " with value " + tableInfo.Value);
                sequenceService.ensureSequenceExists(sequenceName, tableName + "." + tableInfo.idColumnName, tableInfo.Value);
                System.out.println("Successfully ensured sequence: " + sequenceName);
            } catch (Exception e) {
                System.err.println("Error creating sequence for " + tableName + ": " + e.getMessage());
                e.printStackTrace(); // Print stack trace for detailed debugging
            }
        });
    }


}

package com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PropertyOldDetails_Dto {
    private String podBuiltUpAreaFl;
    private String podZoneI;
    private String podWardI;
    private String podOldPropNoVc;
    private String podNewPropertyNoVc;
    private String podOwnerNameVc;
    private String podOccupierNameVc;
    private String podPropertyAddressVc;
    private String podConstClassVc;
    private String podPropertyTypeI;
    private String podPropertySubTypeI;
    private String podUsageTypeI;
    private String podNoofrooms;
    private String podPlotvalue;
    private String podTotalPropertyvalue;
    private String podOccupancyStatusVc;
    private String podBuildingvalue;
    private String podLastAssDtDt;
    private String podCurrentAssDt;
    private String podTotalTaxFl;
    private String podPropertyTaxFl;
    private String podEduCessFl;
    private String podEgcFl;
    private String podTreeTaxFl;
    private String podEnvTaxFl;
    private String podFireTaxFl;
    private Integer podRefNoVc;
    private String pdNewPropertyNoVc;
    private String pdConstructionYearD;
    private String user_id;
    private Boolean oldpresent = false;
    private Boolean error = false;
    private Integer updateFlag=0;
    private String podTotalAssessmentArea;
    private String podTotalRatableValue;
    private String podTotalAssessmentAreaFt;
    private Integer id;

    private List<UnitOldDetails_Dto> unitDetails;
}

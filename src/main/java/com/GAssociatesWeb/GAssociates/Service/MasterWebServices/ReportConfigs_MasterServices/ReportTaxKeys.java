package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.ReportConfigs_MasterServices;

public final class ReportTaxKeys {

    private ReportTaxKeys() {
    }

    // ---- Parent keys ----
    public static final Long PT_PARENT     = 1001L; // Parent for Property Tax I + II
    public static final Long EDUC_PARENT   = 1002L; // Parent for Education Cess (Residential + Commercial)

    // ---- Standard taxes (direct keys to ConsolidatedTaxes_MasterEntity) ----
    public static final Long PT1           = 1L;   // Property Tax I
    public static final Long PT2           = 2L;   // Property Tax II
    public static final Long EDUC_RES      = 25L;  // Education Tax - Residential
    public static final Long EDUC_COMM     = 26L;  // Education Tax - Commercial
    public static final Long EGC           = 27L;  // Employment Guarantee Cess
    public static final Long TREE_TAX      = 3L;
    public static final Long ENV_TAX       = 4L;
    public static final Long CLEAN_TAX     = 5L;
    public static final Long LIGHT_TAX     = 6L;
    public static final Long FIRE_TAX      = 7L;
    public static final Long WATER_TAX     = 8L;
    public static final Long SEWERAGE_TAX  = 9L;
    public static final Long SEWERAGE_BEN  = 10L;
    public static final Long WATER_BEN     = 11L;
    public static final Long STREET_TAX    = 12L;
    public static final Long SPEC_CONS     = 13L;
    public static final Long MUNICIPAL_EDU = 14L;
    public static final Long SPECIAL_EDU   = 15L;
    public static final Long SERVICE_CHG   = 16L;
    public static final Long MISC_CHG      = 17L;
    public static final Long USER_CHG      = 46L;

    // ---- Reserved Flexible Taxes ----
    public static final Long TAX1          = 18L;
    public static final Long TAX2          = 19L;
    public static final Long TAX3          = 20L;
    public static final Long TAX4          = 21L;
    public static final Long TAX5          = 22L;
    public static final Long TAX6          = 23L;
    public static final Long TAX7          = 24L;
    public static final Long TAX8          = 28L;
    public static final Long TAX9          = 29L;
    public static final Long TAX10         = 30L;
    public static final Long TAX11         = 31L;
    public static final Long TAX12         = 32L;
    public static final Long TAX13         = 33L;
    public static final Long TAX14         = 34L;
    public static final Long TAX15         = 35L;
    public static final Long TAX16         = 36L;
    public static final Long TAX17         = 37L;
    public static final Long TAX18         = 38L;
    public static final Long TAX19         = 39L;
    public static final Long TAX20         = 40L;
    public static final Long TAX21         = 41L;
    public static final Long TAX22         = 42L;
    public static final Long TAX23         = 43L;
    public static final Long TAX24         = 44L;
    public static final Long TAX25         = 45L;
    public static final long TOTAL_TAX = 9999L;
}
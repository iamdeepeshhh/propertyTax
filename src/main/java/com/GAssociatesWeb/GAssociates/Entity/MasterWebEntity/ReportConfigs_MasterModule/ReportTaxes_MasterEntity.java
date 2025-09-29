package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.ReportConfigs_MasterModule;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "report_taxes_config",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_report_taxes_config_taxkey_template",
                        columnNames = {"taxkey_l", "template_vc"}
                )
        },
        indexes = {
                @Index(name = "idx_report_taxes_config_parent_seq", columnList = "parent_taxkey_l, sequence_i")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportTaxes_MasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "standardname_vc", nullable = false, length = 255)
    private String standardNameVc;

    @Column(name = "localname_vc", length = 255)
    private String localNameVc;

    @Column(name = "sequence_i", nullable = false)
    private Integer sequenceI;

    @Column(name = "taxkey_l", nullable = false)
    private Long taxKeyL;

    @Column(name = "parent_taxkey_l")
    private Long parentTaxKeyL;

    @Column(name = "show_total_b", nullable = false)
    private Boolean showTotalB = false;

    @Column(name = "visible_b", nullable = false)
    private Boolean visibleB = true;

    @Column(name = "template_vc", nullable = false)
    private String templateVc;
}

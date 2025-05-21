package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.BuildingTypes_MasterEntity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Table(name = "buildingsubtype_master")
@Data
public class BuildingSubType_MasterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer buildingsubtypeid;

    @Column(name = "bst_buildingsubtypeeng_vc", nullable = false, length = 50)
    private String bstBuildingsubtypeengVc;

    @Column(name = "bst_buildingsubtypell_vc", nullable = false, length = 50)
    private String bstBuildingsubtypellVc;

    @ManyToOne(fetch = FetchType.EAGER)//add lazy after testing
    @JoinColumn(name = "buildingtypeid", nullable = false)
    private BuildingType_MasterEntity buildingTypeMaster; // Adjust based on your BuildingType Master entity name

    @Column(name = "bt_buildingtypell_vc", nullable = false, length = 50)
    private String btBuildingtypellVc;

    @Column(name = "bst_created_dt")
    private Timestamp bstCreatedDt;

    @Column(name = "bst_updated_dt")
    private Timestamp bstUpdatedDt;

    @Column(name = "bst_remarks_vc", length = 150)
    private String bstRemarksVc;

}

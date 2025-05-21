package com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.BuildingTypes_MasterEntity;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyTypes_MasterEntity.PropClassification_MasterEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Table(name = "buildingtype_master")
@Data
public class BuildingType_MasterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer buildingtypeid;

    @Column(name = "bt_buildingtypeeng_vc", nullable = false, length = 50)
    private String btBuildingtypeengVc;

    @ManyToOne(fetch = FetchType.EAGER)//add lazy in place of eager after testing*******************
    @JoinColumn(name = "pcm_classid_i", nullable = false, foreignKey = @ForeignKey(name = "pcm_fk"))
    private PropClassification_MasterEntity propertyClassificationMaster;// Adjusted to represent the foreign key relationship

    @Column(name = "pcm_proptypell_vc", nullable = false, length = 50)
    private String pcmProptypellVc;

    @Column(name = "bt_buildingtypell_vc", nullable = false, length = 50)
    private String btBuildingtypellVc;

    @Column(name = "bt_created_dt")
    private Timestamp btCreatedDt;

    @Column(name = "bt_updated_dt")
    private Timestamp btUpdatedDt;

    @Column(name = "bt_remarks_vc", length = 150)
    private String btRemarksVc;

    // Assuming the 'id' field is an additional identifier; if not used, consider removing or adjusting its purpose.
    @Column(name = "id")
    private Integer id;

}

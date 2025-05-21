package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.WardTypes_MasterServices;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.WardTypes_MasterEntity.Ward_MasterEntity;


import java.util.List;

public interface Ward_MasterService {
    default List<Ward_MasterEntity> getAllWards() {
        return null;
    }

    void saveWard(Ward_MasterEntity ward);
    void addWards(int wardCount);

}

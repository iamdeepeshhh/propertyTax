package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.WardTypes_MasterServices;

import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.WardTypes_MasterEntity.Ward_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.WardTypes_MasterRepository.Ward_MasterRepository;
import com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class Ward_MasterServiceImpl implements Ward_MasterService {

    private final Ward_MasterRepository ward_masterRepository;

    @Autowired
    private SequenceService sequenceService;
    @Autowired
    public Ward_MasterServiceImpl(Ward_MasterRepository ward_masterRepository) {
        this.ward_masterRepository = ward_masterRepository;
    }
    @Override
    public void addWards(int wardCount) {
        System.out.println("ward adding initialized" + "=" + wardCount);
        // Process the input and save it to the database
        for (int i = 1; i <= wardCount; i++) {
            Ward_MasterEntity ward_masterEntity = new Ward_MasterEntity();
            ward_masterEntity.setCountNo(String.valueOf(i)); // Assuming you have a constructor/setter for Ward_MasterEntity
            // Set any other properties of the Ward_MasterEntity object if needed
            sequenceService.resetSequenceIfTableIsEmpty("ward_master", "ward_master_ward_no_seq");
            ward_masterRepository.save(ward_masterEntity);
            System.out.println(ward_masterEntity);
        }
        System.out.println("wards added");
    }

    @Override
    public List<Ward_MasterEntity> getAllWards() {
        return ward_masterRepository.findAll();
    }

    @Override
    public void saveWard(Ward_MasterEntity ward) {
        ward_masterRepository.save(ward);
    }


}

package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.PropertyDetails_MasterServices.BuildStatus_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyDetails_MasterDto.BuildStatus_MasterDto;
import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.PropertyDetails_MasterDto.OwnerType_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.PropertyDetails_MasterEntity.BuildStatus_MasterEntity;
//import com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceService;
import com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceConstants;
import com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.PropertyDetails_MasterRepository.BuildStatus_MasterRepository;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@Service
public class BuildStatus_MasterServiceImpl implements BuildStatus_MasterService {

    private final BuildStatus_MasterRepository buildStatus_masterRepository;
    @Autowired
    private final SequenceService sequenceService;

    @Autowired
    public BuildStatus_MasterServiceImpl(BuildStatus_MasterRepository buildStatusMasterRepository,SequenceService sequenceService) {
        this.buildStatus_masterRepository = buildStatusMasterRepository;
        this.sequenceService = sequenceService;

    }

    @Override
    public BuildStatus_MasterDto saveBuildStatus(BuildStatus_MasterDto buildStatusDto) throws Exception {
        BuildStatus_MasterEntity buildStatusEntity = new BuildStatus_MasterEntity();
        buildStatusEntity.setBuildStatus(buildStatusDto.getBuildStatus());

        // Sequence handling
        sequenceService.resetSequenceIfTableIsEmpty("buildstatus_master", "buildstatus_master_id_seq");
        if (buildStatusEntity.getId() == null) {
            long value = SequenceConstants.MIN_VALUE;
            sequenceService.ensureSequenceExists("buildstatus_master_id_seq", "buildstatus_master.id", value);
            Integer nextId = sequenceService.getNextSequenceValue("buildstatus_master_id_seq");
            buildStatusEntity.setId(nextId);
        }

        BuildStatus_MasterEntity savedEntity = buildStatus_masterRepository.save(buildStatusEntity);
        BuildStatus_MasterDto savedDto = new BuildStatus_MasterDto();
        savedDto.setId(savedEntity.getId());
        savedDto.setBuildStatus(savedEntity.getBuildStatus());
        return savedDto;
    }

    @Override
    public List<BuildStatus_MasterDto> findAllBuildStatuses() {
        List<BuildStatus_MasterEntity> entities = buildStatus_masterRepository.findAll();
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private BuildStatus_MasterDto convertToDto(BuildStatus_MasterEntity entity) {
        BuildStatus_MasterDto dto = new BuildStatus_MasterDto();
        dto.setId(entity.getId());
        dto.setBuildStatus(entity.getBuildStatus());
        return dto;
    }

    private BuildStatus_MasterEntity convertToEntity(BuildStatus_MasterDto dto) {
        BuildStatus_MasterEntity entity = new BuildStatus_MasterEntity();
        entity.setId(dto.getId()); // Be cautious with setting the ID for new entities
        entity.setBuildStatus(dto.getBuildStatus());
        return entity;
    }
}

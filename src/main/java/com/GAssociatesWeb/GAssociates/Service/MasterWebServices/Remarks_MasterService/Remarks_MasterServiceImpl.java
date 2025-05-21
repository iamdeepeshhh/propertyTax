package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.Remarks_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.Remarks_MasterDto.Remarks_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.Remarks_MasterEntity.Remarks_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.Remarks_MasterRepository.Remarks_MasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class Remarks_MasterServiceImpl implements Remarks_MasterService {

    @Autowired
    private Remarks_MasterRepository remarksMasterRepository;

    @Override
    public Remarks_MasterDto createRemark(Remarks_MasterDto remarksMasterDto) {
        Remarks_MasterEntity remarksMasterEntity = new Remarks_MasterEntity();
        remarksMasterEntity.setRemark(remarksMasterDto.getRemark());

        remarksMasterEntity = remarksMasterRepository.save(remarksMasterEntity);

        remarksMasterDto.setId(remarksMasterEntity.getId());
        return remarksMasterDto;
    }

    @Override
    public List<Remarks_MasterDto> getAllRemarks() {
        return remarksMasterRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private Remarks_MasterDto convertToDto(Remarks_MasterEntity remarksMasterEntity) {
        Remarks_MasterDto remarksMasterDto = new Remarks_MasterDto();
        remarksMasterDto.setId(remarksMasterEntity.getId());
        remarksMasterDto.setRemark(remarksMasterEntity.getRemark());
        return remarksMasterDto;
    }

}
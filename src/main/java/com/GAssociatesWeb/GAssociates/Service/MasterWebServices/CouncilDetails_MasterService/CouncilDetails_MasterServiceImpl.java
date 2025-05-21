package com.GAssociatesWeb.GAssociates.Service.MasterWebServices.CouncilDetails_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.MasterWebDto.CouncilDetails_MasterDto;
import com.GAssociatesWeb.GAssociates.Entity.MasterWebEntity.CouncilDetails_MasterEntity.CouncilDetails_MasterEntity;
import com.GAssociatesWeb.GAssociates.Repository.MasterWebRepository.CouncilDetails_MasterRepository.CouncilDetails_MasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CouncilDetails_MasterServiceImpl implements CouncilDetails_MasterService {

    @Autowired
    private CouncilDetails_MasterRepository councilDetailsRepository;
    public void saveCouncilDetails(CouncilDetails_MasterDto dto) {
        CouncilDetails_MasterEntity entity = new CouncilDetails_MasterEntity();
        entity.setId(1L);
        entity.setStandardName(dto.getStandardName());
        entity.setLocalName(dto.getLocalName());
        //Field added by himanshu for standardization and localization of sites and district in entity
        entity.setStandardSiteNameVC(dto.getStandardSiteNameVC());
        entity.setLocalSiteNameVC(dto.getLocalSiteNameVC());
        entity.setStandardDistrictNameVC(dto.getStandardDistrictNameVC());
        entity.setLocalDistrictNameVC(dto.getLocalDistrictNameVC());

        // Decode Base64 image and save it (assuming you store it as a string or binary)
        entity.setImageBase64(dto.getImageBase64());
        councilDetailsRepository.save(entity);
    }

    public CouncilDetails_MasterDto getSingleCouncilDetails() {
        List<CouncilDetails_MasterEntity> entityList = councilDetailsRepository.findAll();
        if (!entityList.isEmpty()) {
            CouncilDetails_MasterEntity entity = entityList.get(0); // Assuming only one record
            CouncilDetails_MasterDto dto = new CouncilDetails_MasterDto();
            dto.setStandardName(entity.getStandardName());
            dto.setLocalName(entity.getLocalName());
            //Field added by himanshu for standardization and localization of sites and district in dto
            dto.setStandardSiteNameVC(entity.getStandardSiteNameVC());
            dto.setLocalSiteNameVC(entity.getLocalSiteNameVC());
            dto.setStandardDistrictNameVC(entity.getStandardDistrictNameVC());
            dto.setLocalDistrictNameVC(entity.getLocalDistrictNameVC());
            dto.setImageBase64(entity.getImageBase64()); // Base64 string
            dto.setId(entity.getId());
            return dto;
        } else {
            return null;
        }
    }

    @Transactional
    public boolean deleteCouncilDetailById(Long id) {
        if (councilDetailsRepository.existsById(id)) {
            councilDetailsRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

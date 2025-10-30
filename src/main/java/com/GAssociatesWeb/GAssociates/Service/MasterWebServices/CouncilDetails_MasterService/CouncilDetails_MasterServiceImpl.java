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
        CouncilDetails_MasterEntity entity;
        // Ensure single-record upsert (ID=1). Merge with existing values when fields are blank.
        List<CouncilDetails_MasterEntity> list = councilDetailsRepository.findAll();
        if (!list.isEmpty()) {
            entity = list.get(0);
        } else {
            entity = new CouncilDetails_MasterEntity();
            entity.setId(1L);
        }

        if (dto.getStandardName() != null && !dto.getStandardName().isBlank())
            entity.setStandardName(dto.getStandardName());
        if (dto.getLocalName() != null && !dto.getLocalName().isBlank())
            entity.setLocalName(dto.getLocalName());
        // Field added by himanshu for standardization and localization of sites and district in entity
        if (dto.getStandardSiteNameVC() != null && !dto.getStandardSiteNameVC().isBlank())
            entity.setStandardSiteNameVC(dto.getStandardSiteNameVC());
        if (dto.getLocalSiteNameVC() != null && !dto.getLocalSiteNameVC().isBlank())
            entity.setLocalSiteNameVC(dto.getLocalSiteNameVC());
        if (dto.getStandardDistrictNameVC() != null && !dto.getStandardDistrictNameVC().isBlank())
            entity.setStandardDistrictNameVC(dto.getStandardDistrictNameVC());
        if (dto.getLocalDistrictNameVC() != null && !dto.getLocalDistrictNameVC().isBlank())
            entity.setLocalDistrictNameVC(dto.getLocalDistrictNameVC());

        // Images: only overwrite when provided
        if (dto.getImageBase64() != null && !dto.getImageBase64().isBlank())
            entity.setImageBase64(dto.getImageBase64());
        if (dto.getChiefOfficerSignBase64() != null && !dto.getChiefOfficerSignBase64().isBlank())
            entity.setChiefOfficerSignBase64(dto.getChiefOfficerSignBase64());
        if (dto.getCompanySignBase64() != null && !dto.getCompanySignBase64().isBlank())
            entity.setCompanySignBase64(dto.getCompanySignBase64());

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
            dto.setChiefOfficerSignBase64(entity.getChiefOfficerSignBase64());
            dto.setCompanySignBase64(entity.getCompanySignBase64());
            return dto;
        } else {
            return null;
        }
    }

    @Override
    public void updateCouncilDetailsPartial(CouncilDetails_MasterDto dto) {
        CouncilDetails_MasterEntity entity;
        List<CouncilDetails_MasterEntity> list = councilDetailsRepository.findAll();
        if (!list.isEmpty()) {
            entity = list.get(0);
        } else {
            entity = new CouncilDetails_MasterEntity();
            entity.setId(1L);
        }

        if (dto.getStandardName() != null && !dto.getStandardName().isBlank())
            entity.setStandardName(dto.getStandardName());
        if (dto.getLocalName() != null && !dto.getLocalName().isBlank())
            entity.setLocalName(dto.getLocalName());
        if (dto.getStandardSiteNameVC() != null && !dto.getStandardSiteNameVC().isBlank())
            entity.setStandardSiteNameVC(dto.getStandardSiteNameVC());
        if (dto.getLocalSiteNameVC() != null && !dto.getLocalSiteNameVC().isBlank())
            entity.setLocalSiteNameVC(dto.getLocalSiteNameVC());
        if (dto.getStandardDistrictNameVC() != null && !dto.getStandardDistrictNameVC().isBlank())
            entity.setStandardDistrictNameVC(dto.getStandardDistrictNameVC());
        if (dto.getLocalDistrictNameVC() != null && !dto.getLocalDistrictNameVC().isBlank())
            entity.setLocalDistrictNameVC(dto.getLocalDistrictNameVC());
        if (dto.getImageBase64() != null && !dto.getImageBase64().isBlank())
            entity.setImageBase64(dto.getImageBase64());
        if (dto.getChiefOfficerSignBase64() != null && !dto.getChiefOfficerSignBase64().isBlank())
            entity.setChiefOfficerSignBase64(dto.getChiefOfficerSignBase64());
        if (dto.getCompanySignBase64() != null && !dto.getCompanySignBase64().isBlank())
            entity.setCompanySignBase64(dto.getCompanySignBase64());

        councilDetailsRepository.save(entity);
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

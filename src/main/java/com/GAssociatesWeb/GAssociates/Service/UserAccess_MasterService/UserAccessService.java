package com.GAssociatesWeb.GAssociates.Service.UserAccess_MasterService;

import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.PropertyDetails_Dto;
import com.GAssociatesWeb.GAssociates.DTO.PropertySurveyDto.PropertyOldDetails_Dto;
import com.GAssociatesWeb.GAssociates.DTO.UserAccessDto.UserAccess_Dto;
import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.PropertyDetails_Entity.PropertyDetails_Entity;
import com.GAssociatesWeb.GAssociates.Entity.PropertySurveyEntity.CompletePropertySurvey_Entity.PropertyOldDetails_Entity.PropertyOldDetails_Entity;
import com.GAssociatesWeb.GAssociates.Entity.UserAccess_Entity.UserAccess;
import com.GAssociatesWeb.GAssociates.Repository.PropertySurveyRepository.UserAccessRepository;
import com.GAssociatesWeb.GAssociates.Service.SequenceServices.SequenceService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class UserAccessService {
    @Autowired
    private UserAccessRepository userAccessRepository;
    @Autowired
    private SequenceService sequenceService;
    public boolean authenticate(String username, String password) {
        UserAccess userAccess = userAccessRepository.findByUsername(username);
        // Check if user exists, the password matches, the user is active (assuming "active" indicates an active user)
        boolean isAuthenticated = userAccess != null
                && userAccess.getPassword().equals(password)
                && "Active".equals(userAccess.getStatus());
        return isAuthenticated;
    }
    public Map<Boolean, String> authenticateMaster(String username, String password) {
        Map<Boolean, String> result = new HashMap<>();
        UserAccess userAccess = userAccessRepository.findByUsername(username);
        List<String> allowedProfiles = Arrays.asList("ITL", "IT", "ITA");

        if (userAccess == null) {
            result.put(false, "User not found");
        } else if (!userAccess.getPassword().equals(password)) {
            result.put(false, "Invalid password");
        } else if (!"Active".equalsIgnoreCase(userAccess.getStatus())) {
            result.put(false, "User is not active");
        } else if (!allowedProfiles.contains(userAccess.getProfile())) {
            result.put(false, "User role not allowed");
        } else {
            result.put(true, userAccess.getProfile()); // Return the role
        }

        return result;
    }
    public UserAccess_Dto signup(UserAccess_Dto dto){
        UserAccess entity = convertToEntity(dto);
//        entity.setPassword(dto.getFirstname()+currentSecond);
        UserAccess savedentity = userAccessRepository.save(entity);
        return convertToDto(savedentity);
    }

    public UserAccess_Dto getUserByUsername(String username) {
        UserAccess userAccess = userAccessRepository.findByUsername(username);
        if (userAccess == null) {
            throw new EntityNotFoundException("User not found with username: " + username);
        }
        return convertToDto(userAccess);
    }
    public List<UserAccess_Dto> getAllUsers() {
        List<UserAccess> users = userAccessRepository.findAll();
        List<UserAccess_Dto> userDtos = new ArrayList<>();
        for (UserAccess user : users) {
            UserAccess_Dto dto = new UserAccess_Dto();
            dto.setFirstname(user.getFirstname());
            dto.setLastname(user.getLastname());
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            dto.setStatus(user.getStatus());
            dto.setProfile(user.getProfile());
            dto.setPassword(user.getPassword());
            userDtos.add(dto);
        }
        return userDtos;
    }

    public UserAccess_Dto updateUser(UserAccess_Dto userDto) {
        UserAccess user = userAccessRepository.findByUsername(userDto.getUsername());
        if (user == null) {
            throw new EntityNotFoundException("User not found with username: " + userDto.getUsername());
        }
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setProfile(userDto.getProfile());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setStatus(userDto.getStatus());
        UserAccess updatedUser = userAccessRepository.save(user);
        return convertToDto(updatedUser);
    }
    public boolean deleteUserByUsername(String username) {
        UserAccess userAccess = userAccessRepository.findByUsername(username);
        if (userAccess != null) {
            userAccessRepository.delete(userAccess);
            return true;
        } else {
            throw new EntityNotFoundException("User not found with username: " + username);
           }
    }

    private UserAccess_Dto convertToDto(UserAccess user){
        UserAccess_Dto dto = new UserAccess_Dto();
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setStatus(user.getStatus());
        dto.setProfile(user.getProfile());
        dto.setPassword(user.getPassword());
        return dto;
    }
    private UserAccess convertToEntity(UserAccess_Dto dto){
        UserAccess userAccess = new UserAccess();
        String currentSecond = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ss"));
        String firstname = dto.getFirstname().toLowerCase().replace(" ", "");
        String lastname = dto.getLastname().replace(" ", "");
        userAccess.setFirstname(firstname);
        userAccess.setLastname(lastname);
        int randomNumber = sequenceService.getNextSequenceValue("user_master_id_seq");
        userAccess.setUsername(firstname+randomNumber+"@3g");
        userAccess.setPassword(firstname+currentSecond);
        userAccess.setStatus("Inactive");
        userAccess.setProfile(dto.getProfile());
        userAccess.setPhone(dto.getPhone());
        userAccess.setEmail(dto.getEmail());
        return userAccess;
    }
}

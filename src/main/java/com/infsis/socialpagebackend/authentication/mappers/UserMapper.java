package com.infsis.socialpagebackend.authentication.mappers;

import com.infsis.socialpagebackend.authentication.dtos.UserDetailDTO;
import com.infsis.socialpagebackend.authentication.models.Users;
import com.infsis.socialpagebackend.configuration.AppUrlProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    @Autowired
    private AppUrlProperties appUrlProperties;

    public UserDetailDTO toDTO(Users user) {
        UserDetailDTO userDetailDTO = new UserDetailDTO();

        userDetailDTO.setUuid(user.getUuid());
        userDetailDTO.setName(user.getName());
        userDetailDTO.setLastName(user.getLastName());
        userDetailDTO.setEmail(user.getEmail());
        userDetailDTO.setPassword(user.getPassword());
        userDetailDTO.setPhone(user.getPhone());
        if (user.getPhotoProfileFile() != null) {
            userDetailDTO.setPhotoProfileFileUuid(user.getPhotoProfileFile().getUuid());
            userDetailDTO.setPhoto_profile_path(buildUrlIfPresent(user.getPhotoProfileFile().getUrlResource()));
        } else {
            userDetailDTO.setPhoto_profile_path(buildUrlIfPresent(user.getPhoto_profile_path()));
        }

        if (user.getPhotoCoverFile() != null) {
            userDetailDTO.setPhotoCoverFileUuid(user.getPhotoCoverFile().getUuid());
            userDetailDTO.setPhoto_cover_path(buildUrlIfPresent(user.getPhotoCoverFile().getUrlResource()));
        } else {
            userDetailDTO.setPhoto_cover_path(buildUrlIfPresent(user.getPhoto_cover_path()));
        }
        userDetailDTO.setInstitutionId(user.getInstitutionId());

        return userDetailDTO;
    }

    public Users getUser(UserDetailDTO userDetailDTO) {
        Users user = new Users();

        user.setEmail(userDetailDTO.getEmail());
        user.setName(userDetailDTO.getName());
        user.setLastName(userDetailDTO.getLastName());
        user.setPassword(userDetailDTO.getPassword());
        user.setPhone(userDetailDTO.getPhone());
        user.setPhoto_profile_path(userDetailDTO.getPhoto_profile_path());
        user.setPhoto_cover_path(userDetailDTO.getPhoto_cover_path());

        return user;
    }

    private String buildUrlIfPresent(String path) {
        if (path == null || path.isBlank()) {
            return path;
        }
        return appUrlProperties.buildResourceUrl(path);
    }
}

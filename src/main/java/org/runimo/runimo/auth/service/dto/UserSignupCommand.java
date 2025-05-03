package org.runimo.runimo.auth.service.dto;


import org.runimo.runimo.user.domain.Gender;
import org.springframework.web.multipart.MultipartFile;

public record UserSignupCommand(
    String registerToken,
    String nickname,
    MultipartFile profileImage,
    Gender gender
) {

}

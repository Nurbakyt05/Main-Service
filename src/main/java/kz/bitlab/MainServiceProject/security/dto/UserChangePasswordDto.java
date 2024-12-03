package kz.bitlab.MainServiceProject.security.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePasswordDto {
    private String username;
    private String newPassword;
}

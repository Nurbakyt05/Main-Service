package kz.bitlab.MainServiceProject.security.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignInDto {
    private String username;
    private String password;
}

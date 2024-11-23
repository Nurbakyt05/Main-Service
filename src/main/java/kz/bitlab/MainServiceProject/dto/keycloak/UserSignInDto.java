package kz.bitlab.MainServiceProject.dto.keycloak;

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

package kz.bitlab.MainServiceProject.security.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokensDto {
    private String username;
    private String password;
}

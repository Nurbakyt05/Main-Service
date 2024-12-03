package kz.bitlab.MainServiceProject.security.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {
    private String email;
    private String password;
    private String username;
    private String firstname;
    private String lastname;
}

package kz.bitlab.MainServiceProject.security.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {
    private String email;
    private String firstName;
    private String lastName;
}

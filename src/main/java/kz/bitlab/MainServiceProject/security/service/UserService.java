package kz.bitlab.MainServiceProject.security.service;

import kz.bitlab.MainServiceProject.security.dto.*;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;

public interface UserService {
    String signIn(UserSignInDto userSignInDto);
    String refreshToken(RefreshTokensDto refreshTokensDto);
    UserRepresentation createUser(UserCreateDto user);
    UserRepresentation updateUser(UserUpdateDto user);
    UserRepresentation changePassword(UserChangePasswordDto userChangePasswordDto);
    UserRepresentation setRolesToUser(UserRoleDto userRolesDto);
}

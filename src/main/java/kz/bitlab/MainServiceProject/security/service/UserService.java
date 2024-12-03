package kz.bitlab.MainServiceProject.security.service;

import kz.bitlab.MainServiceProject.security.dto.*;
import org.keycloak.representations.idm.UserRepresentation;

public interface UserService {
    String signIn(UserSignInDto userSignInDto);
    String refreshToken(RefreshTokensDto refreshTokensDto);
    UserRepresentation createUser(UserCreateDto user);
    UserRepresentation changePassword(UserChangePasswordDto userChangePasswordDto);
    UserRepresentation setRolesToUser(UserRoleDto userRolesDto);
}

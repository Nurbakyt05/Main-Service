package kz.bitlab.MainServiceProject.security.controller;

import jakarta.validation.Valid;
import kz.bitlab.MainServiceProject.security.dto.*;
import kz.bitlab.MainServiceProject.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/create")
    public UserRepresentation createUser(@RequestBody UserCreateDto userCreateDto) {
        return userService.createUser(userCreateDto);
    }
    @PutMapping(value = "/update")
    public UserRepresentation updateUser(@RequestBody UserUpdateDto userUpdateDto) {
        return userService.updateUser(userUpdateDto);
    }

    @PostMapping(value = "/sign-in")
    public String signIn(@RequestBody UserSignInDto userSignInDto) {
        return userService.signIn(userSignInDto);
    }
    @PostMapping(value = "/refresh")
    public String refreshUser(@RequestBody RefreshTokensDto refreshTokensDto) {
        return userService.refreshToken(refreshTokensDto);
    }
    @PostMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public UserRepresentation changeUserPassword(@Valid @RequestBody UserChangePasswordDto passwords) {
        return userService.changePassword(passwords);
    }
    @PutMapping(value = "/set-role")
    public UserRepresentation addRoleUser(@RequestBody UserRoleDto userRoleDto) {
        return userService.setRolesToUser(userRoleDto);
    }
}

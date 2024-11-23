package kz.bitlab.MainServiceProject.controller;

import kz.bitlab.MainServiceProject.dto.keycloak.UserCreateDto;
import kz.bitlab.MainServiceProject.dto.keycloak.UserSignInDto;
import kz.bitlab.MainServiceProject.service.keycloak.KeyCloakService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {

    private final KeyCloakService keyCloakService;

    @PostMapping(value = "/create")
    public UserRepresentation createUser(@RequestBody UserCreateDto userCreateDto) {
        return keyCloakService.createUser(userCreateDto);
    }
    @PostMapping(value = "/sign-in")
    public String signIn(@RequestBody UserSignInDto userSignInDto) {
        return keyCloakService.signIn(userSignInDto);
    }
}

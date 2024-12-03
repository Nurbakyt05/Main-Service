package kz.bitlab.MainServiceProject.security.service.impl;

import jakarta.ws.rs.core.Response;
import kz.bitlab.MainServiceProject.security.dto.*;
import kz.bitlab.MainServiceProject.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static kz.bitlab.MainServiceProject.security.util.UserUtils.getCurrentUserName;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Keycloak keycloak;
    private final RestTemplate restTemplate;

    @Value("${keycloak.url}")
    private String url;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client}")
    private String client;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    public UserRepresentation createUser(UserCreateDto user) {
        UserRepresentation newUser = new UserRepresentation();
        newUser.setEmail(user.getEmail());
        newUser.setEmailVerified(true);
        newUser.setUsername(user.getUsername());
        newUser.setFirstName(user.getFirstname());
        newUser.setLastName(user.getLastname());
        newUser.setEnabled(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(user.getPassword());
        credential.setTemporary(false);

        newUser.setCredentials(List.of(credential));

        Response response = keycloak
                .realm(realm)
                .users()
                .create(newUser);

        if (response.getStatus() != HttpStatus.CREATED.value()) { // 201 status
            log.error("Error on creating user");
            throw new RuntimeException("Failed to create user!");
        }

        List<UserRepresentation> searchUser = keycloak.realm(realm).users().search(user.getUsername());
        return searchUser.get(0);
    }

    @Override
    public UserRepresentation changePassword(UserChangePasswordDto userChangePasswordDto) {

        String currentUserName = getCurrentUserName();

        if (currentUserName == null) {
            log.error("Current user name not found");
            throw new RuntimeException("Current user name not found");
        }

        List<UserRepresentation> users = keycloak
                .realm(realm)
                .users()
                .search(userChangePasswordDto.getUsername());

        if (users.isEmpty()) {
            log.error("No users found for username {}", userChangePasswordDto.getUsername());
            throw new RuntimeException("No users found for username " + userChangePasswordDto.getUsername());
        }

        UserRepresentation userRepresentation = users.get(0);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(userChangePasswordDto.getNewPassword());
        credential.setTemporary(false);

        keycloak
                .realm(realm)
                .users()
                .get(userRepresentation.getId())
                .resetPassword(credential);
        log.info("Password changed");
        return userRepresentation;

    }

    @Override
    public UserRepresentation setRolesToUser(UserRoleDto userRolesDto) {
        try {
            List<UserRepresentation> users = keycloak
                    .realm(realm)
                    .users()
                    .search(userRolesDto.getUsername());

            if (users.isEmpty()) {
                log.error("User with username {} not found", userRolesDto.getUsername());
                return null;
            }

            UserRepresentation user = users.get(0);

            String userId = user.getId();

            List<String> rolesToAssign = userRolesDto.getRole();
            List<RoleRepresentation> roles = keycloak
                    .realm(realm)
                    .roles()
                    .list();

            List<RoleRepresentation> rolesForUser = roles.stream()
                    .filter(role -> rolesToAssign.contains(role.getName()))
                    .toList();

            if (rolesForUser.isEmpty()) {
                log.error("No matching roles found for user {}", userRolesDto.getUsername());
                return null;
            }

            keycloak
                    .realm(realm)
                    .users()
                    .get(userId)
                    .roles()
                    .realmLevel()
                    .add(rolesForUser);

            log.info("Roles {} successfully assigned to user {}", rolesToAssign, userRolesDto.getUsername());
            return user;

        } catch (Exception e) {
            log.error("Error assigning roles to user {}: {}", userRolesDto.getUsername(), e.getMessage());
            return null;
        }
    }

    public String signIn(UserSignInDto userSignInDto) {
        String tokenEndpoint = url + "/realms/" + realm + "/protocol/openid-connect/token";
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", client);
        formData.add("client_secret", clientSecret);
        formData.add("username", userSignInDto.getUsername());
        formData.add("password", userSignInDto.getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/x-www-form-urlencoded");

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenEndpoint, new HttpEntity<>(formData, headers), Map.class);
        Map<String, Object> responseBody = response.getBody();

        if (!response.getStatusCode().is2xxSuccessful() || responseBody == null) {
            log.error("Error on signIn");
            throw new RuntimeException("Failed to authenticate");
        }
        return (String) responseBody.get("access_token");
    }

    public String refreshToken(RefreshTokensDto refreshTokensDto) {
        String tokenEndpoints = url + "/realms/" + realm + "/protocol/openid-connect/token";
        MultiValueMap<String, String> formDataTwo = new LinkedMultiValueMap<>();
        formDataTwo.add("grant_type", "password");
        formDataTwo.add("client_id", client);
        formDataTwo.add("client_secret", clientSecret);
        formDataTwo.add("username", refreshTokensDto.getUsername());
        formDataTwo.add("password", refreshTokensDto.getPassword());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type","application/x-www-form-urlencoded");

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenEndpoints, new HttpEntity<>(formDataTwo, httpHeaders), Map.class);
        Map<String, Object> responseBody = response.getBody();

        if (!response.getStatusCode().is2xxSuccessful() || responseBody == null) {
            log.error("Error on refreshToken");
            throw new RuntimeException("Failed to authenticate");
        }
        return (String) responseBody.get("refresh_token");
    }
}

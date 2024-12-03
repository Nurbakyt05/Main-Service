package kz.bitlab.MainServiceProject.security.util;

import jakarta.annotation.PostConstruct;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class KeycloakRoleAndUserInitializer {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    @PostConstruct
    public void addInitialUsersAndRoles() {
        try {
            // Создаем роли в Keycloak
            createRoleIfNotExists("ROLE_ADMIN");
            createRoleIfNotExists("ROLE_TEACHER");
            createRoleIfNotExists("ROLE_USER");

            // Создаем пользователей
            createUserIfNotExists("user-admin", "nurbakit1605@gmail.com", "adminPassword", "ROLE_ADMIN");
            createUserIfNotExists("teacher", "nurbakit1605@gmail.com", "teacherPassword", "ROLE_TEACHER");
            createUserIfNotExists("user", "nurbakit1605@gmail.com", "userPassword", "ROLE_USER");

        } catch (Exception e) {
            log.error("Error during initialization: {}", e.getMessage(), e);
        }
    }

    private void createRoleIfNotExists(String roleName) {
        if (keycloak.realm(realm).roles().get(roleName).toRepresentation() == null) {
            RoleRepresentation role = new RoleRepresentation();
            role.setName(roleName);
            keycloak.realm(realm).roles().create(role);
            log.info("Created role: {}", roleName);
        }
    }

    private void createUserIfNotExists(String username, String email, String password, String roleName) {
        UserRepresentation user = keycloak.realm(realm).users().search(username).stream().findFirst().orElse(null);

        if (user == null) {
            user = new UserRepresentation();
            user.setUsername(username);
            user.setEmail(email);
            user.setEnabled(true);

            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(password);
            credential.setTemporary(false);
            user.setCredentials(Arrays.asList(credential));

            keycloak.realm(realm).users().create(user);
            log.info("Created user: {}", username);
        }

        // Назначаем роль пользователю
        keycloak.realm(realm)
                .users()
                .get(user.getId())
                .roles()
                .realmLevel()
                .add(Arrays.asList(keycloak.realm(realm).roles().get(roleName).toRepresentation()));
        log.info("Assigned role '{}' to user '{}'", roleName, username);
    }
}

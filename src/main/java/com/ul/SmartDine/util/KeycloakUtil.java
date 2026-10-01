package com.ul.SmartDine.util;

import com.ul.SmartDine.config.KeycloakConfig;
import com.ul.SmartDine.entity.enums.AuthProvider;
import com.ul.SmartDine.exceptions.KeyCloakIntegrationException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class KeycloakUtil {
    private final RestClient keycloakRestClient;
    private final KeycloakConfig.KeycloakProperties properties;

    public String createUser(String email, String password, String firstName, String lastName) {
        String adminToken = getAdminToken();
        Map<String, Object> userPresentation = Map.of("username", email, "email", email, "firstName", firstName, "lastName", lastName, "enabled", true, "emailVerified", false, "credentials", List.of(Map.of("type", "password", "value", password, "temporary", false)));
        var response = keycloakRestClient.post().uri("/admin/realms/{realm}/users", properties.getRealm()).header("Authorization", "Bearer " + adminToken).contentType(MediaType.APPLICATION_JSON).body(userPresentation).retrieve().toBodilessEntity();
        String location = response.getHeaders().getFirst("Location");
        if (location == null) {
            throw new KeyCloakIntegrationException("Location not found");
        }
        return location.substring(location.lastIndexOf("/") + 1);
    }

    public String createSocialUser(String email, String firstName, String lastName, AuthProvider provider) {
        String adminToken = getAdminToken();
        Map<String, Object> userPresentation = Map.of("username", email, "email", email, "firstName", firstName, "lastName", lastName, "enabled", true, "emailVerified", true, "attributes", Map.of("provider", List.of(provider.name())));
        var response = keycloakRestClient.post().uri("/admin/realms/{realm}/users", properties.getRealm()).header("Authorization", "Bearer " + adminToken).contentType(MediaType.APPLICATION_JSON).body(userPresentation).retrieve().toBodilessEntity();
        String location = response.getHeaders().getFirst("Location");
        if (location == null) {
            throw new KeyCloakIntegrationException("Location not found");
        }
        return location.substring(location.lastIndexOf("/") + 1);
    }

    public Map<String, Object> authenticateUser(String email, String password) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", properties.getClientId());
        form.add("client_secret", properties.getSecretId());
        form.add("username", email);
        form.add("password", password);
        return keycloakRestClient.post().uri("/realms/{realm}/protocol/openid-connect/token", properties.getRealm()).contentType(MediaType.APPLICATION_FORM_URLENCODED).body(form).retrieve().body(new ParameterizedTypeReference<>() {
        });
    }

    public void logout(String refreshToken) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id", properties.getClientId());
        form.add("client_secret", properties.getSecretId());
        form.add("refresh_token", refreshToken);

        keycloakRestClient.post().uri("/realms/{realm}/protocol/openid-connect/logout", properties.getRealm()).contentType(MediaType.APPLICATION_FORM_URLENCODED).body(form).retrieve().toBodilessEntity();
    }

    public Map<String, Object> impersonateUser(String keycloakId) {
        String adminToken = getAdminToken();

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "urn:ietf:params:oauth:grant-type:token-exchange");
        form.add("client_id", properties.getClientId());
        form.add("client_secret", properties.getSecretId());
        form.add("requested_subject", keycloakId);
        form.add("subject_token", adminToken);

        return keycloakRestClient.post().uri("/realms/{realm}/protocol/openid-connect/token", properties.getRealm()).contentType(MediaType.APPLICATION_FORM_URLENCODED).body(form).retrieve().body(new ParameterizedTypeReference<>() {
        });
    }

    public Map<String, Object> exchangeGoogleToken(String idToken) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "urn:ietf:params:oauth:grant-type:token-exchange");
        form.add("client_id", properties.getClientId());
        form.add("client_secret", properties.getSecretId());
        form.add("subject_token", idToken);
        form.add("subject_token_type", "urn:ietf:params:oauth:token-type:id_token");
        form.add("subject_issuer", "google");

        return keycloakRestClient.post().uri("/realms/{realm}/protocol/openid-connect/token", properties.getRealm()).contentType(MediaType.APPLICATION_FORM_URLENCODED).body(form).retrieve().body(new ParameterizedTypeReference<>() {
        });
    }

    public Map<String, Object> exchangeGitHubCode(String code) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("client_id", properties.getClientId());
        form.add("client_secret", properties.getSecretId());
        form.add("code", code);
        form.add("redirect_uri", "postmessage");

        return keycloakRestClient.post().uri("/realms/{realm}/protocol/openid-connect/token", properties.getRealm()).contentType(MediaType.APPLICATION_FORM_URLENCODED).body(form).retrieve().body(new ParameterizedTypeReference<>() {
        });
    }

    public Map<String, Object> refreshToken(String refreshToken) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "refresh_token");
        form.add("client_id", properties.getClientId());
        form.add("client_secret", properties.getSecretId());
        form.add("refresh_token", refreshToken);

        return keycloakRestClient.post().uri("/realms/{realm}/protocol/openid-connect/token", properties.getRealm()).contentType(MediaType.APPLICATION_FORM_URLENCODED).body(form).retrieve().body(new ParameterizedTypeReference<>() {
        });
    }

    public void updateUserProfile(String keycloakId, String firstName, String lastName) {
        String adminToken = getAdminToken();

        Map<String, Object> update = Map.of("firstName", firstName, "lastName", lastName);

        keycloakRestClient.put().uri("/admin/realms/{realm}/users/{id}", properties.getRealm(), keycloakId).header("Authorization", "Bearer " + adminToken).contentType(MediaType.APPLICATION_JSON).body(update).retrieve().toBodilessEntity();
    }

    public void resetPassword(String keycloakId, String newPassword) {
        String adminToken = getAdminToken();

        Map<String, Object> credential = Map.of("type", "password", "value", newPassword, "temporary", false);

        keycloakRestClient.put().uri("/admin/realms/{realm}/users/{id}/reset-password", properties.getRealm(), keycloakId).header("Authorization", "Bearer " + adminToken).contentType(MediaType.APPLICATION_JSON).body(credential).retrieve().toBodilessEntity();
    }

    public void markEmailVerified(String keycloakId) {
        String adminToken = getAdminToken();

        Map<String, Object> update = Map.of("emailVerified", true);

        keycloakRestClient.put().uri("/admin/realms/{realm}/users/{id}", properties.getRealm(), keycloakId).header("Authorization", "Bearer " + adminToken).contentType(MediaType.APPLICATION_JSON).body(update).retrieve().toBodilessEntity();
    }

    public void deleteUser(String keycloakId) {
        String adminToken = getAdminToken();

        keycloakRestClient.delete().uri("/admin/realms/{realm}/users/{id}", properties.getRealm(), keycloakId).header("Authorization", "Bearer " + adminToken).retrieve().toBodilessEntity();
    }

    private String getAdminToken() {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", "admin-cli");
        form.add("username", properties.getAdmin().getUsername());
        form.add("password", properties.getAdmin().getPassword());
        Map<String, Object> response = keycloakRestClient.post().uri("/realms/master/protocol/openid-connect/token").contentType(MediaType.APPLICATION_FORM_URLENCODED).body(form).retrieve().body(new ParameterizedTypeReference<>() {
        });
        return (String) response.get("access_token");
    }
}

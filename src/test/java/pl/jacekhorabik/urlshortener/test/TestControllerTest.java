package pl.jacekhorabik.urlshortener.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.c4_soft.springaddons.security.oauth2.test.annotations.OpenIdClaims;
import com.c4_soft.springaddons.security.oauth2.test.annotations.WithOAuth2Login;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.jacekhorabik.urlshortener.security.SecurityConfig;

@WebMvcTest(TestController.class)
@Import({SecurityConfig.class})
@ActiveProfiles({"test"})
class TestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithOAuth2Login(
            authorities = "ADMIN",
            claims = @OpenIdClaims(
                    sub = "admin-user-123",
                    preferredUsername = "admin",
                    email = "admin@example.com"
            )
    )
    void adminUserShouldAccessAdminEndpoint() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("_admin"));
    }

    @Test
    @WithOAuth2Login(
            authorities = "USER",
            claims = @OpenIdClaims(
                    sub = "user-456",
                    preferredUsername = "regularuser",
                    email = "user@example.com"
            )
    )
    void regularUserShouldAccessUserEndpoint() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(view().name("_user"));
    }

    @Test
    @WithOAuth2Login(
            authorities = "USER",
            claims = @OpenIdClaims(
                    sub = "user-789",
                    preferredUsername = "limiteduser",
                    email = "limited@example.com"
            )
    )
    void userWithoutAdminRoleShouldNotAccessAdminEndpoint() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithOAuth2Login(
            authorities = "SOME_OTHER_ROLE",
            claims = @OpenIdClaims(
                    sub = "guest-101",
                    preferredUsername = "guest",
                    email = "guest@example.com"
            )
    )
    void userWithoutUserRoleShouldNotAccessUserEndpoint() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithOAuth2Login(
            authorities = {"USER", "ADMIN"},
            claims = @OpenIdClaims(
                    sub = "super-user-303",
                    preferredUsername = "superuser",
                    email = "super@example.com"
            )
    )
    void userWithUserAndAdminRolesShouldAccessBothEndpoints() throws Exception {
        // Can access admin endpoint
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("_admin"));

        // Can also access user endpoint
        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(view().name("_user"));
    }

    @Test
    void unauthenticatedUserShouldBeRedirectedForProtectedEndpoints() throws Exception {
        // Admin endpoint - should redirect to OAuth2 authorization
        mockMvc.perform(get("/admin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/oauth2/authorization/**"));

        // User endpoint - should redirect to OAuth2 authorization  
        mockMvc.perform(get("/user"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/oauth2/authorization/**"));
    }


    // Test edge case with empty authorities
    @Test
    @WithOAuth2Login(
            authorities = {},
            claims = @OpenIdClaims(
                    sub = "no-auth-909",
                    preferredUsername = "noauth",
                    email = "noauth@example.com"
            )
    )
    void userWithNoAuthoritiesShouldNotAccessProtectedEndpoints() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/user"))
                .andExpect(status().isForbidden());
    }
    
}

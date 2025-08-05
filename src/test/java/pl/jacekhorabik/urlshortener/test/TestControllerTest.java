package pl.jacekhorabik.urlshortener.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.jacekhorabik.urlshortener.security.SecurityConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TestController.class)
@Import({SecurityConfig.class})
@ActiveProfiles({"local"})
class TestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(authorities = {"USER"}, username = "user123")
    void userAuthorityShouldNotHaveAccessToAdminSite() throws Exception {
        this.mockMvc
                .perform(get("/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"USER"}, username = "user123")
    void userAuthorityShouldHaveAccessToUserSite() throws Exception {
        this.mockMvc
                .perform(get("/user"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"}, username = "user123")
    void adminAuthorityShouldHaveAccessToAdminSite() throws Exception {
        this.mockMvc
                .perform(get("/admin"))
                .andExpect(status().isOk());
    }

}

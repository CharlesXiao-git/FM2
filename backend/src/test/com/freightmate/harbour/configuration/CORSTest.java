package com.freightmate.harbour.configuration;

import com.freightmate.harbour.model.LoginRequest;
import com.freightmate.harbour.model.Unit;
import com.freightmate.harbour.model.User;
import com.freightmate.harbour.model.UserRole;
import com.freightmate.harbour.service.AuthService;
import com.freightmate.harbour.service.UserDetailsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CORSTest {

    @RunWith(SpringRunner.class)
    @SpringBootTest

    public static class ControllerTests {

        @Autowired
        private WebApplicationContext wac;

        UserDetailsService userServiceMock = Mockito.mock(UserDetailsService.class);

        // not too sure how to easily mock middleware, so this has to match the secret in application.yaml
        AuthService service = new AuthService("THISSHOULDNOTMAKETOPROUCTION" , 3600, userServiceMock);
        String token;

        public MockMvc mockMvc;

        @Before
        public void setup() {
            DefaultMockMvcBuilder builder = MockMvcBuilders
                    .webAppContextSetup(this.wac)
                    .apply(SecurityMockMvcConfigurers.springSecurity())
                    .dispatchOptions(true);
            this.mockMvc = builder.build();

            when(userServiceMock.loadUserByUsername(Mockito.anyString()))
                    .thenReturn(
                            User.builder()
                                    .password("$2a$10$759Q/obZ.3cKX2a4.Y4j8.SgmqnLGBVSIgzhr0sgFUBBD2FF04aqm") //TestPassword
                                    .username("TestUser")
                                    .email("test@test.com")
                                    .userRole(UserRole.CLIENT)
                                    .preferredUnit(Unit.M)
                                    .id(1)
                                    .build()
                    );

            this.token = this.service.attemptLogin(
                    new LoginRequest("TestUser", "TestPassword", "0.0.0.0")
            ).getToken();
        }


        @Test
        public void testCors() throws Exception {
            this.mockMvc
                    .perform(options("/test-cors")
                            .header("Access-Control-Request-Method", "POST")
                            .header("Origin", "localhost:8080")
                            .header("Authorization", "Bearer " + this.token))
                            .andDo(print())
                            .andExpect(status().isOk());

            this.mockMvc
                    .perform(post("/test-cors")
                            .header("Origin", "localhost:8080")
                            .header("Content-Type", "application/json")
                            .header("Authorization", "Bearer " + this.token)
                    .content("{}"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string("whatever"));
        }

        @SpringBootApplication(scanBasePackages = {"com.freightmate.harbour"})
        @Controller
        static class TestApplication {

            public static void main(String[] args) {
                SpringApplication.run(TestApplication.class, args);
            }

            @RequestMapping(value = {"test-cors"},  method = RequestMethod.POST)
            @ResponseStatus(HttpStatus.OK)
            public @ResponseBody
            String testCors() {
                return "whatever";
            }
        }
    }
}

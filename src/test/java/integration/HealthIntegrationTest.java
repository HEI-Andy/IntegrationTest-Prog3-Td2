package integration;

import app.foot.FootApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FootApi.class)
@AutoConfigureMockMvc
public class HealthIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void pong() throws Exception {
        RequestBuilder builder = get("/ping");
        MockHttpServletResponse response = mockMvc
                .perform(builder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8)))
                .andReturn()
                .getResponse();

        String expected = "pong";
        String actual = response.getContentAsString();

        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(expected, actual);
    }
}

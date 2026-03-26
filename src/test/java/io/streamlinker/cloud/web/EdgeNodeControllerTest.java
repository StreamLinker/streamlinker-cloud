package io.streamlinker.cloud.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EdgeNodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRegisterHeartbeatAndReportStatus() throws Exception {
        mockMvc.perform(post("/api/edge/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "edgeId":"edge-1",
                                  "name":"Edge One",
                                  "version":"0.1.0"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.edgeId").value("edge-1"))
                .andExpect(jsonPath("$.status").value("ONLINE"));

        mockMvc.perform(post("/api/edge/heartbeat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "edgeId":"edge-1",
                                  "version":"0.1.1"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.version").value("0.1.1"));

        mockMvc.perform(post("/api/edge/streams/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "edgeId":"edge-1",
                                  "streams":[
                                    {
                                      "streamId":"stream-1",
                                      "name":"Camera 1",
                                      "accessMode":"PROXY",
                                      "cloudApp":"live",
                                      "cloudStream":"cam-1",
                                      "enabled":1,
                                      "state":"RUNNING",
                                      "localOnline":1,
                                      "cloudOnline":1
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.updatedCount").value(1));

        mockMvc.perform(get("/api/edge"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].edgeId").value("edge-1"));
    }
}

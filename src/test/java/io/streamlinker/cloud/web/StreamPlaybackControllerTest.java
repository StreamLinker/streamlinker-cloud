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
class StreamPlaybackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldListStreamAndExposePlayInfo() throws Exception {
        mockMvc.perform(post("/api/edge/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "edgeId":"edge-2",
                                  "name":"Edge Two"
                                }
                                """))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/edge/streams/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "edgeId":"edge-2",
                                  "streams":[
                                    {
                                      "streamId":"stream-2",
                                      "name":"Camera 2",
                                      "accessMode":"FFMPEG",
                                      "cloudApp":"live",
                                      "cloudStream":"cam-2",
                                      "enabled":1,
                                      "state":"RUNNING",
                                      "localOnline":1,
                                      "cloudOnline":0
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/streams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.streamId=='stream-2')].name").value("Camera 2"));

        mockMvc.perform(get("/api/streams/stream-2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cloudStream").value("cam-2"));

        mockMvc.perform(get("/api/streams/stream-2/play-info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.webrtcUrl").exists())
                .andExpect(jsonPath("$.httpFlvUrl").exists())
                .andExpect(jsonPath("$.hlsUrl").exists());
    }
}
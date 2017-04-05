package pl.lodz.p.edu.grs.controller;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class GameControllerWebIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testListAll() throws IOException {
        ResponseEntity<String> forEntity = restTemplate.getForEntity("/api/v1/games", String.class);
        Assertions.assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}

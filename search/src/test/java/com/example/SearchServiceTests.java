package com.example;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(SearchApplication.class)
public class SearchServiceTests {

    public static final String INPUT = "omega";
    public static final String OUTPUT = "point";

    @InjectMocks
    SearchService searchService;

    @Mock
    RestTemplate restTemplate;

    @Before
    public void setup() {
        restTemplate = new RestTemplate();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_get_expected_result() {

        Mockito.when(restTemplate.getForEntity(SearchService.SEARCH_URL + INPUT, String.class))
                .thenReturn(new ResponseEntity<>(OUTPUT, HttpStatus.OK));

        assertThat(searchService.callApi(INPUT)).contains(OUTPUT);

    }
}

package com.example;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import rx.Observable;

import java.util.concurrent.CompletableFuture;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(SearchApplication.class)
public class SearchControllerTests {

    private static final String INPUT = "omega";
    private static final String OUTPUT = "point";

    private MockMvc mockMvc;

    @Mock
    SearchService searchService;

    @InjectMocks
    SearchController searchControllerUnderTest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(searchControllerUnderTest).build();
    }

    @Test
    public void endpoint_1_should_get_expected_content() throws Exception {
        Mockito.when(searchService.callApi(INPUT))
                .thenReturn(OUTPUT);

        mockMvc.perform(get("/1/" + INPUT))
                .andExpect(status().isOk())
                .andExpect(content().string(OUTPUT));
    }

    @Test
    public void endpoint_2_should_get_expected_content() throws Exception {
        Mockito.when(searchService.callFutureApi(INPUT))
                .thenReturn(CompletableFuture.supplyAsync(() -> OUTPUT));

        MvcResult mvcResult = mockMvc.perform(get("/2/" + INPUT))
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(notNullValue()))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().string(OUTPUT));
    }

    @Test
    public void endpoint_3_should_get_expected_content() throws Exception {
        Mockito.when(searchService.callObservableApi(INPUT))
                .thenReturn(Observable.create(subscriber -> subscriber.onNext(OUTPUT)));

        MvcResult mvcResult = mockMvc.perform(get("/3/" + INPUT))
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(notNullValue()))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().string(OUTPUT));
    }
}

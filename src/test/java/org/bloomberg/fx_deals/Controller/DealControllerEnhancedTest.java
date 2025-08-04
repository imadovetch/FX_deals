package org.bloomberg.fx_deals.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bloomberg.fx_deals.Context.DuplicateDealsContext;
import org.bloomberg.fx_deals.Service.DealService;
import org.bloomberg.fx_deals.Helpers.ControllerHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(DealController.class)
@ActiveProfiles("test")
class DealControllerEnhancedTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DealService dealService;

    @MockBean
    private ControllerHelper controllerHelper;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    @AfterEach
    void clearContext() {
        DuplicateDealsContext.clear();
    }



}
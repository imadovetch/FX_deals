package org.bloomberg.fx_deals.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bloomberg.fx_deals.Model.DTO.DealDto;
import org.bloomberg.fx_deals.Model.DTO.ImportResultDto;
import org.bloomberg.fx_deals.context.DuplicateDealsContext;
import org.bloomberg.fx_deals.service.DealService;
import org.bloomberg.fx_deals.Helpers.ControllerHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
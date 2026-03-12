package com.example.walletapi;


import com.example.walletapi.controllers.WalletController;
import com.example.walletapi.dto.WalletOperationRequestDTO;
import com.example.walletapi.dto.WalletResponseDTO;
import com.example.walletapi.entity.OperationType;
import com.example.walletapi.entity.Wallet;
import com.example.walletapi.exception.InsufficientFundsException;
import com.example.walletapi.service.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ActiveProfiles("test")


@WebMvcTest(controllers = WalletController.class)
public class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    WalletService walletService;

    private UUID walletId;
    private WalletOperationRequestDTO request;
    private WalletResponseDTO response;


    @BeforeEach
    void init()
    {
        walletId = UUID.randomUUID();

        request = new WalletOperationRequestDTO();
        request.setWalletId(walletId);
        request.setOperationType(OperationType.DEPOSIT);
        request.setAmount(BigDecimal.valueOf(2000));

        response = new WalletResponseDTO();
        response.setWalletId(walletId);
        response.setBalance(BigDecimal.valueOf(12000));
    }

    @Test
    void processOperation_withValidDeposit_shouldReturn200AndResponse() throws Exception {
        when(walletService.processOperation(any(WalletOperationRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/wallet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
       .andExpect(status().isOk())
       .andExpect(content().contentType(MediaType.APPLICATION_JSON))
       .andExpect(jsonPath("$.walletId").value(equalToIgnoringCase(walletId.toString())))
       .andExpect(jsonPath("$.balance").value(12000));

    }

    @Test
    void processOperation_withValidWithdraw_shouldReturn200AndResponse() throws Exception{
        request.setOperationType(OperationType.WITHDRAW);
        when(walletService.processOperation(any(WalletOperationRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.walletId").value(equalToIgnoringCase(walletId.toString())))
                .andExpect(jsonPath("$.balance").value(12000));


    }

    @Test
    void processOperation_withInsufficientFunds_shouldReturn400() throws Exception {
        request.setOperationType(OperationType.WITHDRAW);
        when(walletService.processOperation(any(WalletOperationRequestDTO.class)))
                .thenThrow(new InsufficientFundsException("Insufficient funds"));

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotModified())
                .andExpect(jsonPath("$.message").value("Insufficient funds"));

    }

}


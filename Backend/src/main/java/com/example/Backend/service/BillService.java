package com.example.Backend.service;

import com.example.Backend.dto.BillCreateRequestDTO;
import com.example.Backend.dto.BillResponseDTO;
import java.util.List;

public interface BillService {

    BillResponseDTO createBill(BillCreateRequestDTO request, Long userId);

    List<BillResponseDTO> getBillHistory(Long userId);

    BillResponseDTO getBillDetail(Long billId, Long userId);
}
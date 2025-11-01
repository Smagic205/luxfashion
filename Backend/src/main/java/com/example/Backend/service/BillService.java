package com.example.Backend.service;

import com.example.Backend.dto.BillCreateRequestDTO;
import com.example.Backend.dto.BillResponseDTO;
import java.util.List;

public interface BillService {

    /**
     * Tạo một hóa đơn mới từ các món hàng trong giỏ
     * 
     * @param request DTO chứa danh sách ID CartDetail và thông tin giao hàng
     * @param userId  ID của người dùng đang đăng nhập
     * @return DTO của hóa đơn vừa được tạo
     */
    BillResponseDTO createBill(BillCreateRequestDTO request, Long userId);

    /**
     * Lấy lịch sử đơn hàng của người dùng
     * 
     * @param userId ID của người dùng
     * @return Danh sách các hóa đơn
     */
    List<BillResponseDTO> getBillHistory(Long userId);

    /**
     * Lấy chi tiết một hóa đơn
     * 
     * @param billId ID của hóa đơn
     * @param userId ID của người dùng (để kiểm tra bảo mật)
     * @return Chi tiết hóa đơn
     */
    BillResponseDTO getBillDetail(Long billId, Long userId);
}
package com.example.Backend.controller.client;

import com.example.Backend.dto.BillCreateRequestDTO;
import com.example.Backend.dto.BillResponseDTO;
import com.example.Backend.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
// @CrossOrigin(origins = "http://localhost:3000") // Mở comment này nếu React
// báo lỗi CORS
public class BillController {

    @Autowired
    private BillService billService;

    /**
     * =======================================================
     * HÀM LẤY ID USER (MẶC ĐỊNH LÀ 1L)
     * =======================================================
     * * Comment: Do chưa có Spring Security, chúng ta mặc định
     * mọi hành động (tạo bill, xem bill) đều là của User có ID = 1.
     * Sau này khi tích hợp Security, bạn sẽ sửa lại hàm này.
     */
    private Long getCurrentUserId() {
        return 1L; // <-- ĐÃ MẶC ĐỊNH LÀ 1L
    }

    /**
     * API Tạo Hóa đơn mới (Thanh toán)
     * [POST] http://localhost:8080/api/bills
     * Body: { "cartDetailIds": [101, 102], "shippingInfo": { ... } }
     */
    @PostMapping
    public ResponseEntity<BillResponseDTO> createBill(@RequestBody BillCreateRequestDTO request) {
        // Hàm này sẽ tự động gọi getCurrentUserId() và trả về 1L
        Long userId = getCurrentUserId();

        BillResponseDTO createdBill = billService.createBill(request, userId);
        return ResponseEntity.ok(createdBill);
    }

    /**
     * API Lấy lịch sử đơn hàng
     * [GET] http://localhost:8080/api/bills
     */
    @GetMapping
    public ResponseEntity<List<BillResponseDTO>> getMyBillHistory() {
        // Sẽ lấy lịch sử của User ID = 1L
        Long userId = getCurrentUserId();
        List<BillResponseDTO> history = billService.getBillHistory(userId);
        return ResponseEntity.ok(history);
    }

    /**
     * API Lấy chi tiết 1 đơn hàng
     * [GET] http://localhost:8080/api/bills/5
     */
    @GetMapping("/{id}")
    public ResponseEntity<BillResponseDTO> getBillById(@PathVariable("id") Long billId) {
        // Sẽ kiểm tra xem billId có thuộc User ID = 1L không
        Long userId = getCurrentUserId();
        BillResponseDTO bill = billService.getBillDetail(billId, userId);
        return ResponseEntity.ok(bill);
    }
}
package com.example.Backend.controller.client;

import com.example.Backend.dto.BillCreateRequestDTO;
import com.example.Backend.dto.BillResponseDTO;
import com.example.Backend.entity.User; // <-- 1. THÊM IMPORT
import com.example.Backend.service.BillService;
import com.example.Backend.service.UserService; // <-- 2. THÊM IMPORT
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // <-- 3. THÊM IMPORT
import org.springframework.security.oauth2.core.user.OAuth2User; // <-- 4. THÊM IMPORT
import org.springframework.web.bind.annotation.*;

import java.security.Principal; // <-- 5. THÊM IMPORT
import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    @Autowired
    private BillService billService;

    // --- 6. INJECT USER SERVICE ---
    // (Dùng để tìm user trong DB từ thông tin đăng nhập)
    @Autowired
    private UserService userService;

    // --- 7. XÓA BỎ HÀM HARD-CODE CŨ ---
    // private Long getCurrentUserId() {
    // return 1L;
    // }

    /**
     * =======================================================
     * HÀM HELPER MỚI (Rất quan trọng)
     * =======================================================
     * Lấy User ID thật từ Principal (phiên đăng nhập).
     * Hàm này xử lý cả 2 trường hợp: Đăng nhập Local và Đăng nhập Google.
     */
    private Long getUserIdFromPrincipal(Principal principal) {
        if (principal == null) {
            throw new SecurityException("User not authenticated");
        }

        String email;
        String name = null;

        // Ép kiểu 'principal' về 'Authentication' để lấy thông tin chi tiết
        Authentication authentication = (Authentication) principal;
        Object principalDetails = authentication.getPrincipal();

        if (principalDetails instanceof OAuth2User) {
            // Case 1: Đăng nhập bằng Google
            OAuth2User oauthUser = (OAuth2User) principalDetails;
            email = oauthUser.getAttribute("email");
            name = oauthUser.getAttribute("name");
        } else {
            // Case 2: Đăng nhập bằng Local (hoặc các trường hợp khác)
            // principal.getName() sẽ là email (như ta cấu hình trong
            // UserDetailsServiceImpl)
            email = principal.getName();
        }

        // Gọi hàm 'findOrCreateUser'
        // (Breakpoint của bạn sẽ dừng ở đây khi user Google gọi API lần đầu)
        User user = userService.findOrCreateUser(email, name);
        return user.getId();
    }

    /**
     * =======================================================
     * SỬA LẠI CÁC HÀM API
     * =======================================================
     */

    @PostMapping
    public ResponseEntity<BillResponseDTO> createBill(
            @RequestBody BillCreateRequestDTO request,
            Principal principal) { // <-- 8. Thêm 'Principal'

        // Lấy ID người dùng thật từ phiên đăng nhập
        Long userId = getUserIdFromPrincipal(principal);

        BillResponseDTO createdBill = billService.createBill(request, userId);
        return ResponseEntity.ok(createdBill);
    }

    @GetMapping
    public ResponseEntity<List<BillResponseDTO>> getMyBillHistory(Principal principal) { // <-- 9. Thêm 'Principal'

        Long userId = getUserIdFromPrincipal(principal);
        List<BillResponseDTO> history = billService.getBillHistory(userId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillResponseDTO> getBillById(
            @PathVariable("id") Long billId,
            Principal principal) { // <-- 10. Thêm 'Principal'

        Long userId = getUserIdFromPrincipal(principal);
        BillResponseDTO bill = billService.getBillDetail(billId, userId);
        return ResponseEntity.ok(bill);
    }
}
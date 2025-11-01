package com.example.Backend.service.impl;

import com.example.Backend.dto.BillCreateRequestDTO;
import com.example.Backend.dto.BillResponseDTO;
import com.example.Backend.dto.ShippingInfoDTO;
import com.example.Backend.entity.*;
import com.example.Backend.exception.ForbiddenException;
import com.example.Backend.exception.ResourceNotFoundException;
import com.example.Backend.repository.*;
import com.example.Backend.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Rất quan trọng

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepository;
    @Autowired
    private BillDetailRepository billDetailRepository; // Mặc dù có Cascade, vẫn có thể cần
    @Autowired
    private CartDetailRepository cartDetailRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductVariantRepository productVariantRepository; // Để trừ kho

    /**
     * =======================================================
     * HÀM CHÍNH: TẠO HÓA ĐƠN
     * =======================================================
     */
    @Override
    @Transactional // Comment: Bắt buộc dùng Transactional. Nếu 1 bước lỗi, tất cả sẽ rollback.
    public BillResponseDTO createBill(BillCreateRequestDTO request, Long userId) {

        // 1. Tìm người dùng
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 2. Lấy thông tin từ Request DTO
        List<Long> cartDetailIds = request.getCartDetailIds();
        ShippingInfoDTO shippingInfo = request.getShippingInfo();

        if (cartDetailIds == null || cartDetailIds.isEmpty()) {
            throw new IllegalArgumentException("Cart item list cannot be empty");
        }
        if (shippingInfo == null) {
            throw new IllegalArgumentException("Shipping information is required");
        }

        // 3. Lấy các CartDetail từ DB
        List<CartDetail> cartDetails = cartDetailRepository.findAllById(cartDetailIds);

        // 4. Kiểm tra bảo mật và Tồn kho (QUAN TRỌNG)
        List<ProductVariant> variantsToUpdate = new ArrayList<>(); // List để cập nhật tồn kho
        double totalAmount = 0.0;

        for (CartDetail item : cartDetails) {
            // 4a. Bảo mật: Món hàng này có thuộc user này không?
            if (!item.getCart().getUser().getId().equals(userId)) {
                throw new ForbiddenException("You do not own cart item with id: " + item.getId());
            }

            ProductVariant variant = item.getProductVariant();
            int requestedQuantity = item.getQuantity();

            // 4b. Tồn kho: Kiểm tra số lượng
            if (variant.getQuantity() < requestedQuantity) {
                throw new RuntimeException("Not enough stock for variant: "
                        + variant.getProduct().getName() + " - "
                        + variant.getColor().getName() + " - "
                        + variant.getSize().getName()
                        + ". Available: " + variant.getQuantity());
            }

            // 4c. Trừ kho (tạm thời)
            variant.setQuantity(variant.getQuantity() - requestedQuantity);
            variantsToUpdate.add(variant); // Thêm vào list để lát nữa saveAll

            // 4d. Tính tổng tiền
            totalAmount += (item.getPrice() * requestedQuantity);
        }

        // 5. Tạo Hóa đơn (Bill)
        Bill newBill = new Bill();
        newBill.setUser(user);
        newBill.setCreatedAt(LocalDateTime.now());
        newBill.setStatus("PENDING"); // PENDING = Chờ xử lý
        newBill.setTotalAmount(totalAmount);

        // Lưu thông tin giao hàng
        newBill.setShippingName(shippingInfo.getName());
        newBill.setShippingAddress(shippingInfo.getAddress());
        newBill.setShippingPhone(shippingInfo.getPhone());
        newBill.setShippingNote(shippingInfo.getNote());

        // 6. Tạo danh sách Chi tiết Hóa đơn (BillDetail)
        List<BillDetail> billDetails = new ArrayList<>();
        for (CartDetail item : cartDetails) {
            BillDetail billDetail = new BillDetail(
                    newBill, // Liên kết với Bill cha
                    item.getProductVariant(), // Liên kết với Variant
                    null, // Note (nếu cần)
                    item.getQuantity(),
                    item.getPrice() // Giá tại thời điểm mua
            );
            billDetails.add(billDetail);
        }

        // Gán danh sách con vào cha
        newBill.setBillDetails(billDetails);

        // 7. LƯU VÀO DB (Do CascadeType.ALL, chỉ cần save Bill cha)
        Bill savedBill = billRepository.save(newBill);

        // 8. Cập nhật tồn kho (saveAll)
        productVariantRepository.saveAll(variantsToUpdate);

        // 9. Xóa các món hàng đã mua khỏi giỏ hàng
        cartDetailRepository.deleteAll(cartDetails);

        // 10. Trả về Response DTO
        return new BillResponseDTO(savedBill);
    }

    /**
     * =======================================================
     * CÁC HÀM PHỤ: XEM LỊCH SỬ
     * =======================================================
     */
    @Override
    @Transactional(readOnly = true)
    public List<BillResponseDTO> getBillHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Bill> bills = billRepository.findByUserOrderByCreatedAtDesc(user);

        return bills.stream()
                .map(BillResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BillResponseDTO getBillDetail(Long billId, Long userId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found"));

        // Bảo mật: Hóa đơn này có phải của user này không?
        if (!bill.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You do not have permission to view this bill");
        }

        return new BillResponseDTO(bill);
    }
}
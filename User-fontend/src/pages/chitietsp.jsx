import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import Dssplq from "../components/dssp-lq";

export default function ProductDetail({ userId }) {
  const { id } = useParams();

  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [quantity, setQuantity] = useState(1);
  const [selectedSizeId, setSelectedSizeId] = useState("");

  const formatVND = (value) =>
    value?.toLocaleString("vi-VN", { style: "currency", currency: "VND" });

  useEffect(() => {
    const fetchProduct = async () => {
      try {
        const res = await fetch(`/api/products/${id}`);
        if (!res.ok) {
          throw new Error("Không thể tải chi tiết sản phẩm");
        }
        const data = await res.json();
        // Backend trả về trực tiếp ProductResponseDTO, đôi khi được bọc trong { data }
        const productPayload = data?.data ?? data;
        if (productPayload && productPayload.id) {
          setProduct(productPayload);
        } else {
          setError("Không tìm thấy sản phẩm");
        }
      } catch (err) {
        console.error("Fetch product error:", err);
        setError(err.message || "Lỗi khi tải dữ liệu sản phẩm.");
      } finally {
        setLoading(false);
      }
    };
    fetchProduct();
  }, [id]);

  const sizes = [
    { id: 1, name: "S" },
    { id: 2, name: "M" },
    { id: 3, name: "L" },
    { id: 4, name: "XL" },
  ];

  useEffect(() => {
    setSelectedSizeId("");
    setQuantity(1);
  }, [product?.id]);

  if (loading) return <p className="text-center mt-10">Đang tải...</p>;
  if (error) return <p className="text-center text-red-500 mt-10">{error}</p>;
  if (!product) return null;

  const unitSalePrice = product.salePrice ?? product.originalPrice ?? 0;
  const unitOriginalPrice = product.originalPrice ?? unitSalePrice;
  const totalSalePrice = unitSalePrice * quantity;
  const totalOriginalPrice = unitOriginalPrice * quantity;

  const apiUrl = product.category?.id
    ? `/api/products?categoryId=${product.category.id}`
    : `/api/products`;

  const handleAddToCart = async () => {
    // Kiểm tra user đã đăng nhập chưa
    if (!userId) {
      alert("Bạn cần đăng nhập trước khi thêm vào giỏ hàng!");
      return;
    }

    if (!selectedSizeId) {
      alert("Vui lòng chọn size trước khi thêm vào giỏ hàng!");
      return;
    }

    const matchingVariant =
      product?.variants?.find(
        (variant) => variant.size?.id === Number(selectedSizeId)
      ) ?? null;

    if (!matchingVariant) {
      alert("Size bạn chọn hiện không khả dụng cho sản phẩm này!");
      return;
    }

    if (
      typeof matchingVariant.quantity === "number" &&
      quantity > matchingVariant.quantity
    ) {
      alert("Số lượng vượt quá tồn kho hiện có!");
      return;
    }

    const payload = {
      variantId: Number(matchingVariant.id),
      quantity: Number(quantity),
    };

    console.log("📦 Payload gửi đi:", payload);

    try {
      const res = await fetch("/api/cart/add", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify(payload),
      });

      console.log("📡 Response status:", res.status);

      if (!res.ok) {
        let errorMessage = "Thêm vào giỏ hàng thất bại!";
        try {
          const err = await res.json();
          console.error("❌ Server error response:", err);
          errorMessage = err.message || err.error || JSON.stringify(err);
        } catch {
          const textError = await res.text();
          console.error("❌ Server error (text):", textError);
          errorMessage = `Server error: ${res.status} - ${textError}`;
        }
        alert(errorMessage);
        return;
      }

      const result = await res.json();
      console.log("✅ Success response:", result);
      alert("Thêm sản phẩm vào giỏ hàng thành công!");
    } catch (err) {
      console.error("💥 Add to cart error:", err);
      alert("Lỗi kết nối server, vui lòng thử lại!");
    }
  };

  return (
    <>
      <div className="max-w-6xl mx-auto mt-15 grid grid-cols-1 md:grid-cols-2 gap-10 px-4">
        {/* Ảnh sản phẩm */}
        <div>
          <img
            src={product.images?.[0]}
            alt={product.name}
            className="w-full h-[420px] object-cover rounded-2xl shadow-xl"
          />
        </div>

        {/* Thông tin sản phẩm */}
        <div className="space-y-8">
          <h1 className="text-3xl font-semibold">{product.name}</h1>
          <p className="text-gray-600">
            Mã SP: {product.id} | Phân loại: {product.category?.name} | Rate: {product.averageRating ?? 0} ⭐
          </p>
          <p className="font-medium text-gray-700">
            Thương hiệu: <span className="text-blue-600">{product.supplier?.name}</span>
          </p>

          {/* Giá sản phẩm */}
          <div className="flex items-center gap-3 mt-4">
            <span className="text-3xl font-bold text-red-600">{formatVND(totalSalePrice)}</span>
            {product.originalPrice && product.salePrice && product.originalPrice > product.salePrice && (
              <>
                <span className="text-gray-400 line-through text-lg">{formatVND(totalOriginalPrice)}</span>
                <span className="text-red-500 text-lg font-semibold">-{product.discountPercentage}%</span>
              </>
            )}
          </div>

          {/* Chọn size và số lượng */}
          <div className="flex flex-col md:flex-row md:items-center gap-6 mt-10 ml-10">
            <div className="flex flex-col sm:flex-row sm:items-center gap-3">
              <label htmlFor="size" className="font-medium text-gray-700">
                Size:
              </label>
              <select
                id="size"
                value={selectedSizeId}
                onChange={(e) => setSelectedSizeId(Number(e.target.value))}
                className="border rounded-lg px-3 py-1 focus:outline-none focus:ring-2 focus:ring-blue-400"
              >
                <option value="">Chọn size</option>
                {sizes.map((size) => (
                  <option key={size.id} value={size.id}>
                    {size.name}
                  </option>
                ))}
              </select>
            </div>

            <div className="flex flex-col sm:flex-row sm:items-center gap-3">
              <span className="font-medium text-gray-700">Số lượng:</span>
              <div className="inline-flex border rounded-lg">
                <button onClick={() => setQuantity(q => Math.max(1, q - 1))} className="px-3 py-1 border-r hover:bg-gray-200">-</button>
                <span className="px-4 py-1">{quantity}</span>
                <button onClick={() => setQuantity(q => q + 1)} className="px-3 py-1 border-l hover:bg-gray-200">+</button>
              </div>
            </div>
          </div>

          {/* Nút hành động */}
          <div className="flex gap-4 mt-10">
            <button className="bg-orange-500 text-white px-6 py-2 rounded-lg hover:bg-red-600 transition" onClick={() => alert("Mua ngay - sẽ thêm logic sau")}>Mua ngay</button>
            <button className="border border-gray-400 px-6 py-2 rounded-lg hover:bg-gray-100 transition" onClick={handleAddToCart}>🛒 Thêm vào giỏ hàng</button>
          </div>

          {/* Mô tả sản phẩm */}
          <div className="mt-8">
            <h3 className="text-lg font-semibold mb-2">Mô tả sản phẩm</h3>
            <p className="text-gray-600">{product.description}</p>
          </div>
        </div>
      </div>

      {/* Sản phẩm liên quan */}
      <div className="pt-10">
        <h1 className="text-2xl font-bold mb-4 pb-5">Các sản phẩm liên quan</h1>
        <Dssplq apiUrl={apiUrl} />
      </div>
    </>
  );
}
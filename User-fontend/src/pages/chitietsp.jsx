import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

export default function ProductDetail() {
  const { id } = useParams();
  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [quantity, setQuantity] = useState(1);
  const [selectedSize, setSelectedSize] = useState("M");

  // Hàm định dạng giá tiền
  const formatVND = (value) =>
    value?.toLocaleString("vi-VN", { style: "currency", currency: "VND" });

  useEffect(() => {
    const fetchProduct = async () => {
      try {
        const res = await fetch(`/api/products/${id}`);
        const data = await res.json();
        if (data?.data) setProduct(data.data);
        else setError("Không tìm thấy sản phẩm");
      } catch  {
        setError("Lỗi khi tải dữ liệu sản phẩm.");
      } finally {
        setLoading(false);
      }
    };
    fetchProduct();
  }, [id]);

  if (loading) return <p className="text-center mt-10">Đang tải...</p>;
  if (error) return <p className="text-center text-red-500 mt-10">{error}</p>;
  if (!product) return null;

  // ✅ Giá tính theo số lượng
  const totalSalePrice = product.salePrice * quantity;
  const totalOriginalPrice = product.originalPrice * quantity;

  return (
    <div className="max-w-6xl mx-30 mt-15 grid grid-cols-1 md:grid-cols-2 gap-10 px-4">
      {/* Ảnh sản phẩm chính */}
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
          Mã SP: {product.id} | Phân loại: {product.category?.name} | Rate:{" "}
          {product.averageRating ?? 0} ⭐
        </p>
        <p className="font-medium text-gray-700">
          Thương hiệu:{" "}
          <span className="text-blue-600">{product.supplier?.name}</span>
        </p>

        {/* ✅ Giá sản phẩm */}
        <div className="flex items-center gap-3 mt-4">
          <span className="text-3xl font-bold text-red-600">
            {formatVND(totalSalePrice)}
          </span>

          {product.originalPrice > product.salePrice && (
            <>
              <span className="text-gray-400 line-through text-lg">
                {formatVND(totalOriginalPrice)}
              </span>
              <span className="text-red-500 text-lg font-semibold">
                -{product.discountPercentage}%
              </span>
            </>
          )}
        </div>
<div className="flex items-center gap-6 mt-10 ml-10">
        {/* ✅ Chọn size */}
        <div className="mt-3">
          <label htmlFor="size" className="font-medium text-gray-700 mr-3">
          Kích thước:
          </label>
          <select
              id="size"
              value={selectedSize}
              onChange={(e) => setSelectedSize(e.target.value)}
              className="border rounded-lg px-3 py-1 focus:outline-none focus:ring-2 focus:ring-blue-400"
                >
              <option value="S">S</option>
              <option option value="M">M</option>
              <option value="L">L</option>
              <option value="XL">XL</option>
          </select>
        </div>

        {/* ✅ Số lượng */}
        <div className="mt-3">
          <span className="font-medium text-gray-700 mr-3">Số lượng:</span>
          <div className="inline-flex border rounded-lg">
            <button
              onClick={() => setQuantity((q) => Math.max(1, q - 1))}
              className="px-3 py-1 border-r hover:bg-gray-200"
            >
              -
            </button>
            <span className="px-4 py-1">{quantity}</span>
            <button
              onClick={() => setQuantity((q) => q + 1)}
              className="px-3 py-1 border-l hover:bg-gray-200"
            >
              +
            </button>
          </div>
        </div>
</div>
        {/* ✅ Nút hành động */}
        <div className="flex gap-4 mt-25">
          <button
            className="bg-orange-500 text-white px-6 py-2 rounded-lg hover:bg-red-600 transition"
            onClick={() => alert("Mua ngay - sẽ thêm logic sau")}
          >
            Mua ngay
          </button>
          <button
            className="border border-gray-400 px-6 py-2 rounded-lg hover:bg-gray-100 transition"
            onClick={() =>
              alert(
                `Thêm vào giỏ: ${product.name}, size ${selectedSize}, SL ${quantity}`
              )
            }
          >
            🛒 Thêm vào giỏ hàng
          </button>
        </div>

        {/* ✅ Mô tả sản phẩm */}
        <div className="mt-8">
          <h3 className="text-lg font-semibold mb-2">Mô tả sản phẩm</h3>
          <p className="text-gray-600">{product.description}</p>
        </div>
      </div>
    </div>
  );
}

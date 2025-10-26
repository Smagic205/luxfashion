import { useEffect, useState } from "react";
import ProductGrid from "./ds-sanpham"; // Đảm bảo đường dẫn đúng

export default function ProductSection({ apiUrl }) {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const res = await fetch(apiUrl);
        const data = await res.json();

        console.log("Data nhận được:", data);

        // Dữ liệu backend của bạn là { statusCode, message, data: [...] }
        // => cần lấy mảng trong data.data
        if (data && Array.isArray(data.data)) {
          setProducts(data.data);
        } else if (Array.isArray(data.products)) {
          setProducts(data.products);
        } else {
          console.warn("Không tìm thấy danh sách sản phẩm hợp lệ trong API.");
          setProducts([]);
        }
      } catch (err) {
        console.error("Lỗi tải sản phẩm:", err);
        setError("Không thể tải sản phẩm.");
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, [apiUrl]);

  // ✅ Trạng thái hiển thị
  if (loading) {
    return <p className="text-gray-500 text-center mt-4">Đang tải sản phẩm...</p>;
  }

  if (error) {
    return <p className="text-red-500 text-center mt-4">{error}</p>;
  }

  return (
    <div>
      {products.length > 0 ? (
        <ProductGrid products={products} />
      ) : (
        <p className="text-gray-500 text-center mt-4">Không có sản phẩm nào để hiển thị.</p>
      )}
    </div>
  );
}
import { useEffect, useState } from "react";
import ProductGrid from "./ds-sanpham";

export default function ProductSection({ apiUrl }) {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const res = await fetch(apiUrl);
        const data = await res.json();

        console.log("Data nhận được từ API:", data);

        if (data && Array.isArray(data.data)) {
          const mapped = data.data.map((p) => {
           
            const imageLink = p.imageUrl ;
            

            return {
              id: p.id,
              name: p.name,
              brand: p.supplierName,
              rating: p.rating || "N/A",
              price: p.salePrice,
              sale: p.discountPercentage || 0,
              image: imageLink,
            };
          });

          setProducts(mapped);
        } else {
          console.warn("Không có danh sách sản phẩm hợp lệ");
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

  if (loading)
    return (
      <p className="text-gray-500 text-center mt-4">Đang tải sản phẩm...</p>
    );

  if (error)
    return <p className="text-red-500 text-center mt-4">{error}</p>;

  return (
    <div>
      {products.length > 0 ? (
        <ProductGrid products={products} />
      ) : (
        <p className="text-gray-500 text-center mt-4">
          Không có sản phẩm nào để hiển thị.
        </p>
      )}
    </div>
  );
}
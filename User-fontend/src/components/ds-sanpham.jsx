import { useState } from "react";
import ProductCard from "./cp-sanpham";

export default function ProductGrid({ products, itemsPerPage = 8 }) {
 const safeProducts = Array.isArray(products) ? products : [];
  const [currentPage, setCurrentPage] = useState(1);

  const totalPages = Math.ceil(safeProducts.length / itemsPerPage);
  const startIndex = (currentPage - 1) * itemsPerPage;
  const currentProducts = safeProducts.slice(startIndex, startIndex + itemsPerPage);

  return (
    <div>
      {/* Lưới sản phẩm */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        {currentProducts.map((p) => (
          <ProductCard
        key={p.id}
         id={p.id} 
        image={p.image}
        name={p.name}
        brand={p.brand}
        rating={p.rating}
        price={p.price}
        sale={p.sale}
        />
        ))}
      </div>

      {/* Phân trang */}
      {totalPages > 1 && (
        <div className="flex justify-center items-center mt-6 space-x-3">
          <button
            onClick={() => setCurrentPage((p) => Math.max(p - 1, 1))}
            disabled={currentPage === 1}
            className={`px-3 py-1 border rounded-lg ${
              currentPage === 1
                ? "text-gray-400 border-gray-200 cursor-not-allowed"
                : "hover:bg-blue-500 hover:text-white"
            }`}
          >
            ← Trước
          </button>

          <span className="font-medium">
            Trang {currentPage} / {totalPages}
          </span>

          <button
            onClick={() => setCurrentPage((p) => Math.min(p + 1, totalPages))}
            disabled={currentPage === totalPages}
            className={`px-3 py-1 border rounded-lg ${
              currentPage === totalPages
                ? "text-gray-400 border-gray-200 cursor-not-allowed"
                : "hover:bg-blue-500 hover:text-white"
            }`}
          >
            Sau →
          </button>
        </div>
      )}
    </div>
  );
}
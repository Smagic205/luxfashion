import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

export default function FilterModal({ isOpen, onClose }) {

  const navigate = useNavigate();
   // const params = new URLSearchParams();
  // --- State ---
  
  const [suppliers, setSuppliers] = useState([]);
  const [selectedBrand, setSelectedBrand] = useState(null);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [minPrice, setMinPrice] = useState("");
  const [maxPrice, setMaxPrice] = useState("");
  const [minRating, setMinRating] = useState("");
    
  // --- Gọi API lấy danh sách hãng ---
  useEffect(() => {
    const fetchSuppliers = async () => {
      try {
        const res = await fetch("/api/suppliers");
        if (!res.ok) {
          throw new Error("Không thể tải danh sách hãng");
        }
        const result = await res.json();

        console.log("Data nhận được từ API:", result);

        const list = Array.isArray(result?.data)
          ? result.data
          : Array.isArray(result)
          ? result
          : [];

        setSuppliers(list);
      } catch (err) {
        console.error("Lỗi khi gọi API /api/suppliers:", err);
        setSuppliers([]);
      }
    };
    fetchSuppliers();
  }, []);
 if (!isOpen) return null;
  // --- Danh mục tĩnh ---
  const categories = [
  { id: 1, name: "Áo thun" },
  { id: 2, name: "Áo sơ mi" },
  { id: 3, name: "Áo len" },
  { id: 4, name: "Áo khoác" },
  { id: 5, name: "Áo hoodie" },
  { id: 6, name: "Áo vest" },
  { id: 7, name: "Áo dài" },
  { id: 8, name: "Găng tay da" },
  { id: 9, name: "Găng tay len" },
  { id: 10, name: "Găng tay vải" },
  { id: 11, name: "Giày sneaker" },
  { id: 12, name: "Giày thể thao" },
  { id: 13, name: "Giày da" },
  { id: 14, name: "Bốt" },
  { id: 15, name: "Dép quai hậu" },
  { id: 16, name: "Mũ lưỡi trai" },
  { id: 17, name: "Mũ tròn" },
  { id: 18, name: "Mũ bucket" },
  { id: 19, name: "Mũ len" },
  { id: 20, name: "Quần short" },
  { id: 21, name: "Quần jean" },
  { id: 22, name: "Quần dài vải" },
  { id: 23, name: "Váy" },
  { id: 24, name: "Kính" },
  { id: 25, name: "Nơ" },
  { id: 26, name: "Cà vạt" },
  { id: 27, name: "Tất" },
  { id: 28, name: "Túi xách tay" },
  { id: 29, name: "Túi đeo ngang hông" },
  { id: 30, name: "Balo" }
];

  // --- Xử lý nhấn nút Lọc ---
  const handleApplyFilter = () => {
    const params = new URLSearchParams();

    if (selectedCategory) params.append("categoryId", selectedCategory);
    if (selectedBrand) params.append("supplierIds", selectedBrand);
    if (minPrice) params.append("minPrice", minPrice);
    if (maxPrice) params.append("maxPrice", maxPrice);
    if (minRating) params.append("minRating", minRating);

    navigate(`/sanpham?${params.toString()}`);
    onClose(); // đóng modal
  };

  return (
    <div className="fixed inset-0 backdrop-blur-sm bg-white/10 flex justify-center items-center z-40">
      <div className="bg-white p-6 rounded-2xl shadow-lg w-[500px] max-h-[90vh] overflow-y-auto relative">
        <h2 className="text-xl font-semibold mb-4 text-center">Bộ lọc sản phẩm</h2>

        {/* --- Chọn Thể loại --- */}
        <div className="mb-4">
          <h3 className="font-medium mb-2">Thể loại</h3>
          <div className="grid grid-cols-2 gap-2">
            {categories.map((c) => (
              <button
                key={c.id}
               onClick={() =>
              setSelectedCategory(selectedCategory === c.id ? null : c.id)
                    }
                className={`border rounded-lg px-3 py-1 text-sm ${
                  selectedCategory === c.id
                    ? "bg-blue-600 text-white"
                    : "bg-gray-100 hover:bg-gray-200"
                }`}
              >
                {c.name}
              </button>
            ))}
          </div>
        </div>

        {/* --- Chọn Hãng --- */}
        <div className="mb-4">
          <h3 className="font-medium mb-2">Hãng</h3>
          {suppliers.length === 0 ? (
            <p className="text-gray-500 text-sm">Đang tải danh sách hãng...</p>
          ) : (
            <div className="grid grid-cols-2 gap-2">
              {suppliers.map((b) => (
                <button
                  key={b.id}
                  onClick={() =>
                setSelectedBrand(selectedBrand === b.id ? null : b.id)
                  }
                  className={`border rounded-lg px-3 py-1 text-sm ${
                    selectedBrand === b.id
                      ? "bg-green-600 text-white"
                      : "bg-gray-100 hover:bg-gray-200"
                  }`}
                >
                  {b.name}
                </button>
              ))}
            </div>
          )}
        </div>

        {/* --- Lọc theo giá --- */}
        <div className="mb-4">
          <h3 className="font-medium mb-2">Giá</h3>
          <div className="flex gap-2">
            <input
              type="number"
              placeholder="Từ"
              value={minPrice}
              onChange={(e) => setMinPrice(e.target.value)}
              className="border rounded-lg w-1/2 px-2 py-1"
            />
            <input
              type="number"
              placeholder="Đến"
              value={maxPrice}
              onChange={(e) => setMaxPrice(e.target.value)}
              className="border rounded-lg w-1/2 px-2 py-1"
            />
          </div>
        </div>

        {/* --- Lọc theo đánh giá --- */}
        <div className="mb-4">
          <h3 className="font-medium mb-2">Đánh giá (sao tối thiểu)</h3>
          <input
            type="number"
            min="0"
            max="5"
            step="0.5"
            placeholder="VD: 3.5"
            value={minRating}
            onChange={(e) => setMinRating(e.target.value)}
            className="border rounded-lg w-full px-2 py-1"
          />
        </div>
 
        {/* --- Nút hành động --- */}
        <div className="flex justify-between mt-6">
          <button
            onClick={onClose}
            className="px-4 py-2 bg-gray-200 rounded-lg hover:bg-gray-300"
          >
            Hủy
          </button>
          <button
            onClick={handleApplyFilter}
            className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
          >
            Áp dụng lọc
          </button>
        </div>
      </div>
    </div>
  );
}
import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import HaGioHang from "../assets/logogiohang.jpeg";
import Logosv from "../assets/logosv.png";
import FilterModal from "../components/cp-loc";

function Logo() {
  return (
    <Link to="/home">
      <img src={Logosv} alt="ha" className="w-60 h-20" />
    </Link>
  );
}

function Menu() {
  const [showMenu, setShowMenu] = useState(false);
  const [brands, setBrands] = useState([]);
  const [showBrands, setShowBrands] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchBrands = async () => {
      try {
        const res = await fetch("/api/suppliers");
        if (!res.ok) {
          throw new Error("Không thể tải danh sách hãng");
        }
        const result = await res.json();
        const supplierList = Array.isArray(result?.data)
          ? result.data
          : Array.isArray(result)
          ? result
          : [];
        setBrands(supplierList);
      } catch (err) {
        console.error("Lỗi tải suppliers:", err);
        setBrands([]);
      }
    };
    fetchBrands();
  }, []);

  const categories = {
    "Áo": [
      { id: 1, name: "Áo thun" },
      { id: 2, name: "Áo sơ mi" },
      { id: 3, name: "Áo len" },
      { id: 4, name: "Áo khoác" },
      { id: 5, name: "Áo hoodie" },
      { id: 6, name: "Áo vest" },
      { id: 7, name: "Áo dài" },
    ],
    "Găng tay": [
      { id: 8, name: "Găng tay da" },
      { id: 9, name: "Găng tay len" },
      { id: 10, name: "Găng tay vải" },
    ],
    "Giày & Dép": [
      { id: 11, name: "Giày sneaker" },
      { id: 12, name: "Giày thể thao" },
      { id: 13, name: "Giày da" },
      { id: 14, name: "Bốt" },
      { id: 15, name: "Dép quai hậu" },
    ],
    "Phụ kiện": [
      { id: 16, name: "Mũ lưỡi trai" },
      { id: 17, name: "Mũ tròn" },
      { id: 18, name: "Mũ bucket" },
      { id: 19, name: "Mũ len" },
      { id: 30, name: "Balo" },
    ],
    "Quần & Váy": [
      { id: 20, name: "Quần short" },
      { id: 21, name: "Quần jean" },
      { id: 22, name: "Quần dài vải" },
      { id: 23, name: "Váy" },
    ],
    "Trang sức & Túi": [
      { id: 24, name: "Kính" },
      { id: 25, name: "Nơ" },
      { id: 26, name: "Cà vạt" },
      { id: 27, name: "Tất" },
      { id: 28, name: "Túi xách tay" },
      { id: 29, name: "Túi đeo ngang hông" },
    ],
  };

  return (
    <nav>
      <ul className="flex list-none gap-10 font-bold">
        <li>
          <Link className="text-white hover:text-red-500" to="/gioithieu">
            Giới thiệu
          </Link>
        </li>
        <li
          className="relative"
          onMouseEnter={() => setShowMenu(true)}
          onMouseLeave={() => setShowMenu(false)}
        >
          <Link className="text-white hover:text-red-500" to="/sanpham">
            Sản phẩm
          </Link>
          {showMenu && (
            <div className="absolute left-0 mt-0 w-[800px] bg-white text-black shadow-lg p-4 rounded-lg grid grid-cols-3 gap-6 z-50">
              {Object.entries(categories).map(([group, items]) => (
                <div key={group}>
                  <h4 className="font-semibold text-lg mb-2 border-b border-gray-300">{group}</h4>
                  <ul>
                    {items.map((item) => (
                      <li
                        key={item.id}
                        className="py-1 text-sm hover:text-red-500 cursor-pointer"
                        onClick={() => navigate(`/sanpham?categoryId=${item.id}`)}
                      >
                        {item.name}
                      </li>
                    ))}
                  </ul>
                </div>
              ))}
            </div>
          )}
        </li>
       
        <li>
          <Link className="text-white hover:text-red-500" to="/sale">
            Sale
          </Link>
        </li>
        <li
          className="relative"
          onMouseEnter={() => setShowBrands(true)}
          onMouseLeave={() => setShowBrands(false)}
        >
          <span className="cursor-pointer text-white hover:text-red-500">Hãng</span>
          {showBrands && (
            <div className="absolute left-0 mt-0 bg-white shadow-lg rounded-lg p-4 z-50 grid grid-cols-3 gap-4 w-[600px] max-h-[250px] overflow-y-auto">
              {brands.map((brand) => (
                <div
                  key={brand.id}
                  onClick={() => {
                    navigate(`/sanpham?supplierIds=${brand.id}`);
                    setShowBrands(false);
                  }}
                  className="text-gray-700 hover:text-red-500 cursor-pointer font-medium hover:bg-gray-100 px-2 py-1 rounded-md transition"
                >
                  {brand.name}
                </div>
              ))}
            </div>
          )}
        </li>
        <li>
          <Link className="text-white hover:text-red-500" to="/thongtinkhachhang">
            Thông tin khách hàng
          </Link>
        </li>
      </ul>
    </nav>
  );
}

function NutLoc() {
  const [open, setOpen] = useState(false);
  return (
    <>
      <input
        className="text-xs font-medium px-3 py-2 border border-gray-900 shadow-md rounded-lg bg-gray-300 hover:bg-gray-100 transition text-gray-900 cursor-pointer"
        type="button"
        value="Lọc"
        onClick={() => setOpen(true)}
      />
      <FilterModal isOpen={open} onClose={() => setOpen(false)} />
    </>
  );
}

function TimKiem() {
  return (
    <input
      type="text"
      placeholder="search"
      className="px-8 py-2 text-sm w-45 text-gray-900 border border-gray-900 shadow-md rounded-lg"
    />
  );
}

function GioHang({ userId }) {
  return (
    <Link
      className="text-white hover:text-red-500 border px-4 py-2 border-gray-900 shadow-md rounded-full"
      to={userId ? "/giohang" : "/taikhoan"}
    >
      Giỏ hàng <br />
      <i className="fa-solid fa-cart-shopping mr-2"></i>
    </Link>
  );
}

function TaiKhoan({ userId, setUserId }) {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("user");
    setUserId(null);
    navigate("/home");
  };

  return userId ? (
    <button
      onClick={handleLogout}
      className="text-white border px-5 py-3 border-gray-900 shadow-md rounded-full hover:bg-gray-700 transition"
    >
      Đăng xuất
    </button>
  ) : (
    <Link
      className="text-white hover:text-red-500 border px-5 py-3 border-gray-900 shadow-md rounded-full"
      to="/taikhoan"
    >
      Đăng nhập
    </Link>
  );
}

export default function Header({ userId, setUserId }) {
  return (
    <header className="bg-gradient-to-r from-blue-500 via-indigo-500 to-purple-500 shadow-xl sticky top-0 z-50 w-full">
      <div className="flex items-center justify-between py-4 px-6">
        <Logo />
        <Menu />
        <NutLoc />
        <TimKiem />
        <GioHang userId={userId} />
        <TaiKhoan userId={userId} setUserId={setUserId} />
      </div>
    </header>
  );
}

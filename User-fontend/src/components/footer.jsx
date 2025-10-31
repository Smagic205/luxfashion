export default function Footer() {
  return (
    <footer className="bg-gray-900 text-gray-300 py-10 mt-12">
      <div className="container mx-auto px-6 grid grid-cols-1 md:grid-cols-4 gap-8">
        {/* Cột 1: Logo và mô tả */}
        <div>
          <h2 className="text-2xl font-bold text-white mb-3">LUX FASHION</h2>
          <p className="text-sm leading-6">
            Cửa hàng thời trang cao cấp mang đến phong cách trẻ trung, hiện đại và tinh tế.
          </p>
        </div>

        {/* Cột 2: nhà phát triển */}
        <div>
          <h3 className="text-lg font-semibold text-white mb-3">Nhà phát triển</h3>
          <ul className="space-y-2 text-sm">
            <li className="hover:text-white transition"> Trần Huy Sơn </li>
            <li className="hover:text-white transition"> Trần Đức Thắng</li>
            <li className="hover:text-white transition"> Nguyễn Minh Sơn</li>
            <li className="hover:text-white transition"> Phạm Việt Trung</li>
            <li className="hover:text-white transition"> Nguyễn Văn Quốc Thái</li>
          </ul>
        </div>

        {/* Cột 3: Hỗ trợ khách hàng */}
        <div>
          <h3 className="text-lg font-semibold text-white mb-3">Hỗ trợ khách hàng</h3>
          <ul className="space-y-2 text-sm">
            <li className="hover:text-white transition">Chính sách bảo hành</li>
            <li className="hover:text-white transition"> Vận chuyển & đổi trả </li>
            <li className="hover:text-white transition">Câu hỏi thường gặp</li>
          </ul>
        </div>

        {/* Cột 4: Liên hệ */}
        <div>
          <h3 className="text-lg font-semibold text-white mb-3">Liên hệ</h3>
          <ul className="space-y-2 text-sm">
            <li> Nguyên Xá, Nam Từ Liêm, Hà Nội</li>
            <li> 0123 456 789</li>
            <li>support@luxfashion.vn</li>
          </ul>

          {/* Mạng xã hội */}
          <div className=" mt-4">
            <a href="https://www.facebook.com/share/1BwfQcHehr/" target="_blank" rel="noreferrer">
              <i className="fab fa-facebook-f text-white hover:text-blue-400 m-2"></i>
            </a>
            <a href="https://instagram.com" target="_blank" rel="noreferrer">
              <i className="fab fa-instagram text-white hover:text-pink-400 m-2"></i>
            </a>
            <a href="https://tiktok.com" target="_blank" rel="noreferrer">
              <i className="fab fa-tiktok text-white hover:text-gray-400 m-2"></i>
            </a>
          </div>
        </div>
      </div>

      {/* Dòng bản quyền */}
      <div className="border-t border-gray-700 mt-8 pt-4 text-center text-sm text-gray-400">
        © {new Date().getFullYear()} Lux Fashion. All rights reserved.
      </div>
    </footer>
  );
}
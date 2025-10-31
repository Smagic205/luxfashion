
import './App.css';
import Home from './pages/home';
import Header from './components/header';
import Footer from './components/footer';
import GioiThieu from './pages/goithieu';
import SanPham from './pages/sanpham';
import Combo from './pages/combo';
import Sale from './pages/sale';
import Hang from './pages/hang';
import ThongTinKhachHang from './pages/thongtinkhachhang';
import GioHang from "./pages/giohang";
import TaiKhoan from "./pages/taikhoan";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import ChiTietSP from "./pages/chitietsp";

function App() {
  return (
    
   <>
     <Router>
        <Header/>
      
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/home" element={<Home />} />
         <Route path="/gioithieu" element={<GioiThieu />}/>
         <Route path="/sanpham" element={<SanPham />}/>
         <Route path="/combo" element={<Combo />}/>
         <Route path="/sale" element={<Sale />}/>
         <Route path="/hang" element={<Hang />}/>
         <Route path="/thongtinkhachhang" element={<ThongTinKhachHang />}/>
         {/* Các trang từ nút riêng trong Header */}
          <Route path="/giohang" element={<GioHang />} />
          <Route path="/taikhoan" element={<TaiKhoan />} />

          <Route path="/chitietsp/:id" element={<ChiTietSP/>}/>
          
        </Routes>
        
       </Router>
        <Footer/>
    </>
  );
}

export default App

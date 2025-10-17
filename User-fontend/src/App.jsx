
import './App.css';
import Home from './pages/home';
import Header from './components/header';
import GioiThieu from './pages/goithieu';
import SanPham from './pages/sanpham';
import Combo from './pages/combo';
import Sale from './pages/sale';
import Hang from './pages/hang';
import ThongTinKhachHang from './pages/thongtinkhachhang';
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

function App() {
  return (
    
   <>
     <Router>
        <Header/>
      
        <Routes>
          <Route path="/" element={<Home />} />
         <Route path="/gioithieu" element={<GioiThieu />}/>
         <Route path="/sanpham" element={<SanPham />}/>
         <Route path="/combo" element={<Combo />}/>
         <Route path="/sale" element={<Sale />}/>
         <Route path="/hang" element={<Hang />}/>
         <Route path="/thongtinkhachhang" element={<ThongTinKhachHang />}/>
        </Routes>
       </Router>
    
    </>
  );
}

export default App

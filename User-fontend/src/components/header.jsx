import React from "react";
import { Link } from "react-router-dom";
import HaGioHang from "../assets/logogiohang.jpeg";
import LogoChinh from "../assets/LogoChinh.png"
function Logo(){
    return(
        <Link  to="/home" >
    <img src={LogoChinh} alt="ha" className=" w-80 h-20"/>
    </Link>
);
}
function Menu(){
    
    return(
    <nav>
        <ul className="flex list-none gap-10 ">
            <li  className="transition duration-300  ">
                <Link className="text-white hover:text-red-500" 
                to="/gioithieu" >Giới thiệu</Link>
            </li>
            <li className="transition duration-300">
                <Link className="text-white hover:text-red-500" 
                to="/sanpham">Sản phẩm</Link>
            </li>
            <li className="transition duration-300">
                <Link className="text-white hover:text-red-500" 
                to="/combo">Combo</Link>
            </li>
            <li className="transition duration-300">
                <Link className="text-white hover:text-red-500" 
                to="/sale">Sale</Link>
            </li>
            <li className="transition duration-300">
                <Link className="text-white hover:text-red-500" 
                to="/hang">Hãng</Link>
            </li>
            <li className="transition duration-300">
                <Link className="text-white hover:text-red-500" 
                to="/thongtinkhachhang">Thông tin khách hàng</Link>
            </li>
        </ul>
    </nav>
    );
}

function NutLoc(){
     return(
        
  <input className="
        text-xs font-medium px-3 py-2 
        border border-gray-900 shadow-md rounded-lg 
        bg-gray-300 hover:bg-gray-100 transition 
        text-gray-900"
       type="button"
       value={"Lọc"}/>
     );
}
function TimKiem(){
    return(
         <input
        type="text"
        placeholder="search"
        className="
        px-8 py-2 text-sm w-45 text-gray-900 
        border border-gray-900 shadow-md rounded-lg "/>
    );
}
function GioHang(){
    return(
  <Link className="text-white hover:text-red-500 border  px-4 py-2
       border-gray-900 shadow-md rounded-full" 
                to="/giohang">Giỏ hàng <br />
                <i className="fa-solid fa-cart-shopping mr-2"></i></Link>
    );
}

function TaiKhoan(){
     return(
  <Link className="text-white hover:text-red-500 border  px-5 py-3
       border-gray-900 shadow-md rounded-full" 
                to="/taikhoan">Account</Link>
    );
}
export default function Header() {
  return (
    <header className="bg-blue-400 shadow-xl sticky top-0 z-50 w-full">
        <div className=" flex items-center justify-between py-4 px-6">
    <Logo />
    <Menu />
    <NutLoc/>
    <TimKiem/>
    <GioHang/>
   <TaiKhoan/>
    </div>
    </header>
  );
}


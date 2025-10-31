import React from "react";
import { Link  } from "react-router-dom";

const ProductCard = ({ image, name, brand, rating, price, sale, id }) => {
  return (
    <Link to={`/chitietsp/${id}`}>
    <div className="bg-white rounded-2xl shadow-2xl p-4 hover:scale-105 transition-transform duration-300 cursor-pointer">
      {/* Ảnh sản phẩm */}
      <div className="w-full h-56 overflow-hidden rounded-xl">
        
        <img
          src={image}
          alt={name}
          className="w-full h-full object-cover hover:opacity-90"
        />
       
      </div>
    
      {/* Tên sản phẩm */}
      <h3 className="text-lg font-semibold text-gray-800 mt-3 text-center truncate">
        {name}
      </h3>

      {/* Hãng */}
      <p className="text-sm text-gray-500 text-center mb-2">{brand}</p>

      {/* Các thông tin phụ */}
      <div className="flex justify-between text-center text-sm mt-3">
        <div className="flex-1 border rounded-md mx-1 p-1 text-red-600 font-bold">
           {rating || "N/A"}⭐
        </div>
        <div className="flex-1 border rounded-md mx-1 p-1">
           {price}₫
        </div>
        <div className="flex-1 border rounded-md mx-1 p-1 text-red-500">
          {sale ? `${sale}%` : "0%"}
        </div>
      </div>
    </div>
    </Link>
  );
};

export default ProductCard;
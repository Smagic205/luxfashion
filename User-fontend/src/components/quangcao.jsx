import { useState, useEffect } from "react";

// 🖼️ import ảnh (đổi thành ảnh của bạn)
import img1 from "../assets/anhqc1.jpg";
import img2 from "../assets/anhqc2.jpg";
import img3 from "../assets/anhqc3.png";
import img4 from "../assets/anhqc4.jpg";
function Slider() {
  const images = [img1, img2, img3,img4];
  const [currentIndex, setCurrentIndex] = useState(0);

  //  Tự động đổi ảnh mỗi 2 giây
  useEffect(() => {
    const timer = setInterval(() => {
      setCurrentIndex((prev) => (prev + 1) % images.length);//% là khi đến ảnh cuối chia% là băng 0 quay lại ảnh 1
    }, 3000);

    return () => clearInterval(timer); // Dọn dẹp khi unmount
  }, [images.length]);

  // 👉 Ảnh kế tiếp
  const nextImage = () => {
    setCurrentIndex((prev) => (prev + 1) % images.length);
  };

  // 👈 Ảnh trước
  const prevImage = () => {
    setCurrentIndex((prev) => (prev - 1 + images.length) % images.length);
  };

  return (
    <div className="relative w-300 h-[400px] overflow-hidden z-0 mx-auto mt-2">
  {/* Khối chứa ảnh trượt ngang */}
  <div
    className="flex w-full h-full transition-transform duration-700 ease-in-out"
    style={{
      transform: `translateX(-${currentIndex * 100}%)`
    }}
  >
    {images.map((img, index) => (
      <img
        key={index}
        src={img}
        alt={`Slide ${index}`}
        className="w-full h-full object-cover flex-shrink-0"
      />
    ))}
  </div>

      {/* Nút trái */}
      <button
        onClick={prevImage}
        className="absolute left-3 top-1/2 -translate-y-1/2 bg-white/50 text-3xl text-black px-3 py-1 rounded-full hover:bg-white transition"
      >
        &lt;
      </button>

      {/* Nút phải */}
      <button
        onClick={nextImage}
        className="absolute right-3 top-1/2 -translate-y-1/2 bg-white/50 text-3xl text-black px-3 py-1 rounded-full hover:bg-white transition"
      >
        &gt;
      </button>
    </div>
  );
}

export default Slider;
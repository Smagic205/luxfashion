import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function Login({ setUserId }) {
  const [form, setForm] = useState({ email: "", password: "" });
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");

    try {
      const res = await fetch("/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify(form),
      });

      const data = await res.json();

      if (!res.ok) {
        return setError(data.error || "Sai email hoặc mật khẩu!");
      }

      const userData = data?.data ?? data;
      if (!userData?.id) {
        setError("Phản hồi đăng nhập không hợp lệ!");
        return;
      }

      // 🔹 Lưu user vào localStorage
      localStorage.setItem("user", JSON.stringify(userData));
      setUserId(userData.id);

      // 🔹 Chuyển về trang chủ hoặc trang trước đó
      navigate("/");
    // eslint-disable-next-line no-unused-vars
    } catch (err) {
      setError("Lỗi hệ thống, vui lòng thử lại!");
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100 px-4">
      <div className="bg-white shadow-xl rounded-2xl p-8 w-full max-w-md">
        <h2 className="text-3xl font-bold text-center mb-6 text-gray-800">
          Đăng nhập
        </h2>

        {error && (
          <div className="bg-red-100 text-red-600 p-3 rounded mb-4 text-sm">
            {error}
          </div>
        )}

        <form onSubmit={handleLogin} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700">
              Email
            </label>
            <input
              type="email"
              name="email"
              value={form.email}
              onChange={handleChange}
              className="w-full mt-1 px-4 py-2 border rounded-lg focus:ring-2 focus:ring-black"
              placeholder="Nhập email"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700">
              Mật khẩu
            </label>
            <input
              type="password"
              name="password"
              value={form.password}
              onChange={handleChange}
              className="w-full mt-1 px-4 py-2 border rounded-lg focus:ring-2 focus:ring-black"
              placeholder="Nhập mật khẩu"
            />
          </div>

          <button
            type="submit"
            className="w-full bg-black text-white py-2 rounded-lg hover:bg-gray-800 transition"
          >
            Đăng nhập
          </button>
        </form>

        <p className="text-center text-sm mt-4">
          Chưa có tài khoản?{" "}
          <a href="/dangky" className="text-blue-600 hover:underline">
            Đăng ký ngay
          </a>
        </p>
      </div>
    </div>
  );
}

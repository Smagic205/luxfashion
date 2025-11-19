import { useEffect, useState } from "react";

// Lấy dữ liệu cached từ localStorage (nếu có)
const getCachedUser = () => {
  try {
    const raw = localStorage.getItem("user");
    if (!raw) return null;
    const parsed = JSON.parse(raw);
    return parsed?.data ?? parsed ?? null;
  } catch {
    return null;
  }
};

export default function ThongTinKhachHang() {
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [warning, setWarning] = useState("");
  const [refreshing, setRefreshing] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [saving, setSaving] = useState(false);

  // Form state
  const [formData, setFormData] = useState({
    fullName: "",
    username: "",
    email: "",
    phone: "",
    address: "",
  });

  // ✅ Gọi API lấy profile với cookie
  const fetchProfile = async (options = { silent: false }) => {
    const { silent } = options;
    if (!silent) {
      setLoading(true);
      setWarning("");
      setError("");
    } else {
      setRefreshing(true);
    }

    try {
      const res = await fetch("/api/user/profile", {
        method: "GET",
        credentials: "include", // 🔥 bắt buộc gửi cookie
        headers: {
          "Content-Type": "application/json",
        },
      });

      if (!res.ok) {
        if (res.status === 401 || res.status === 403) {
          throw new Error("Bạn cần đăng nhập để xem thông tin khách hàng.");
        }
        const errText = await res.text();
        throw new Error(errText || "Không thể lấy thông tin khách hàng.");
      }

      const data = await res.json();
      const payload = data?.data ?? data;

      setProfile(payload);
      setFormData({
        fullName: payload.fullName || "",
        username: payload.username || "",
        email: payload.email || "",
        phone: payload.phone || "",
        address: payload.address || "",
      });
      setWarning("");
    } catch (err) {
      console.error("Lỗi lấy profile:", err);
      const cached = getCachedUser();
      if (cached) {
        setProfile(cached);
        setFormData({
          fullName: cached.fullName || "",
          username: cached.username || "",
          email: cached.email || "",
          phone: cached.phone || "",
          address: cached.address || "",
        });
        setWarning(
          "Không lấy được dữ liệu mới từ máy chủ. Đang hiển thị thông tin đã lưu."
        );
      } else {
        setProfile(null);
        setError(err.message || "Không thể lấy thông tin khách hàng.");
      }
    } finally {
      if (!silent) setLoading(false);
      else setRefreshing(false);
    }
  };

  // Thay đổi dữ liệu form
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  // Lưu thông tin profile
  const handleSave = async () => {
    setSaving(true);
    setError("");

    try {
      const res = await fetch("/api/user/profile", {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include", // 🔥 gửi cookie
        body: JSON.stringify(formData),
      });

      if (!res.ok) {
        const errData = await res
          .json()
          .catch(() => ({ message: "Lỗi cập nhật thông tin" }));
        throw new Error(errData.message || "Không thể cập nhật thông tin");
      }

      const data = await res.json();
      const payload = data?.data ?? data;

      setProfile(payload);
      localStorage.setItem("user", JSON.stringify({ data: payload }));
      setIsEditing(false);
      alert("Cập nhật thông tin thành công!");
    } catch (err) {
      console.error("Lỗi cập nhật:", err);
      setError(err.message || "Không thể cập nhật thông tin");
    } finally {
      setSaving(false);
    }
  };

  // Hủy chỉnh sửa
  const handleCancel = () => {
    setFormData({
      fullName: profile.fullName || "",
      username: profile.username || "",
      email: profile.email || "",
      phone: profile.phone || "",
      address: profile.address || "",
    });
    setIsEditing(false);
    setError("");
  };

  useEffect(() => {
    fetchProfile({ silent: false });
  }, []);

  if (loading) {
    return (
      <div className="max-w-2xl mx-auto mt-10">
        <p className="text-center text-gray-500">Đang tải thông tin...</p>
      </div>
    );
  }

  if (error && !profile) {
    return (
      <div className="max-w-2xl mx-auto mt-10">
        <div className="bg-red-100 text-red-600 px-4 py-3 rounded-lg text-center">
          {error}
        </div>
      </div>
    );
  }

  if (!profile) {
    return (
      <div className="max-w-2xl mx-auto mt-10">
        <p className="text-center text-gray-500">Không có thông tin khách hàng.</p>
      </div>
    );
  }

  return (
    <div className="max-w-2xl mx-auto mt-10 px-4 pb-10">
      <div className="bg-white shadow-xl rounded-2xl p-8">
        <div className="flex items-center justify-between mb-6">
          <h1 className="text-2xl font-bold text-gray-900">
            Thông tin khách hàng
          </h1>
          <button
            onClick={() => fetchProfile({ silent: true })}
            className="inline-flex items-center gap-2 px-3 py-2 rounded-lg border border-gray-300 text-sm font-medium text-gray-700 hover:bg-gray-100 transition disabled:opacity-50"
            disabled={refreshing || isEditing}
          >
            {refreshing ? (
              <>
                <span className="animate-spin h-4 w-4 border-2 border-gray-400 border-t-transparent rounded-full"></span>
                Đang cập nhật...
              </>
            ) : (
              <>
                <span className="text-lg">🔄</span>
                Làm mới
              </>
            )}
          </button>
        </div>

        {warning && (
          <div className="mb-4 bg-yellow-100 text-yellow-800 px-4 py-3 rounded-lg text-sm">
            {warning}
          </div>
        )}

        {error && (
          <div className="mb-4 bg-red-100 text-red-600 px-4 py-3 rounded-lg text-sm">
            {error}
          </div>
        )}

        <div className="space-y-4">
          <InputField
            label="Họ và tên"
            name="fullName"
            value={formData.fullName}
            onChange={handleInputChange}
            isEditing={isEditing}
          />
          <InputField
            label="Tên đăng nhập"
            name="username"
            value={formData.username}
            onChange={handleInputChange}
            isEditing={isEditing}
            disabled={true}
          />
          <InputField
            label="Email"
            name="email"
            type="email"
            value={formData.email}
            onChange={handleInputChange}
            isEditing={isEditing}
          />
          <InputField
            label="Số điện thoại"
            name="phone"
            type="tel"
            value={formData.phone}
            onChange={handleInputChange}
            isEditing={isEditing}
          />
          <InputField
            label="Địa chỉ"
            name="address"
            value={formData.address}
            onChange={handleInputChange}
            isEditing={isEditing}
            multiline={true}
          />
        </div>

        <div className="mt-8 flex gap-3">
          {!isEditing ? (
            <button
              onClick={() => setIsEditing(true)}
              className="w-full bg-blue-500 text-white px-6 py-3 rounded-lg font-medium hover:bg-blue-600 transition"
            >
              ✏️ Sửa thông tin
            </button>
          ) : (
            <>
              <button
                onClick={handleCancel}
                className="flex-1 bg-gray-300 text-gray-700 px-6 py-3 rounded-lg font-medium hover:bg-gray-400 transition"
                disabled={saving}
              >
                ❌ Hủy
              </button>
              <button
                onClick={handleSave}
                className="flex-1 bg-green-500 text-white px-6 py-3 rounded-lg font-medium hover:bg-green-600 transition disabled:opacity-50 disabled:cursor-not-allowed"
                disabled={saving}
              >
                {saving ? "Đang lưu..." : "💾 Lưu"}
              </button>
            </>
          )}
        </div>
      </div>
    </div>
  );
}

function InputField({
  label,
  name,
  value,
  onChange,
  isEditing,
  disabled,
  type = "text",
  multiline = false,
}) {
  return (
    <div className="flex flex-col">
      <label htmlFor={name} className="text-sm text-gray-600 font-medium mb-2">
        {label}
      </label>
      {multiline ? (
        <textarea
          id={name}
          name={name}
          value={value || ""}
          onChange={onChange}
          disabled={!isEditing || disabled}
          rows={3}
          className={`text-base px-4 py-3 border rounded-lg transition ${
            isEditing && !disabled
              ? "border-blue-400 bg-white focus:outline-none focus:ring-2 focus:ring-blue-500"
              : "border-gray-300 bg-gray-50 text-gray-700"
          } ${disabled ? "cursor-not-allowed opacity-60" : ""}`}
          placeholder={isEditing ? `Nhập ${label.toLowerCase()}` : "Chưa cập nhật"}
        />
      ) : (
        <input
          id={name}
          name={name}
          type={type}
          value={value || ""}
          onChange={onChange}
          disabled={!isEditing || disabled}
          className={`text-base px-4 py-3 border rounded-lg transition ${
            isEditing && !disabled
              ? "border-blue-400 bg-white focus:outline-none focus:ring-2 focus:ring-blue-500"
              : "border-gray-300 bg-gray-50 text-gray-700"
          } ${disabled ? "cursor-not-allowed opacity-60" : ""}`}
          placeholder={isEditing ? `Nhập ${label.toLowerCase()}` : "Chưa cập nhật"}
        />
      )}
    </div>
  );
}

import { useCallback, useEffect, useState } from "react";

export default function CartPage({ userId }) {
  const [cart, setCart] = useState([]);
  const [selected, setSelected] = useState([]);
  const [total, setTotal] = useState(0);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const fetchCart = useCallback(async () => {
    if (!userId) {
      setCart([]);
      setError("Bạn cần đăng nhập để xem giỏ hàng.");
      return;
    }
    setLoading(true);
    try {
      const res = await fetch("/api/cart", { credentials: "include" });
      if (!res.ok) {
        throw new Error("Không thể tải giỏ hàng.");
      }
      const data = await res.json();
      const payload = data?.data ?? data;
      const items = payload?.items ?? [];
      setCart(items);
      setSelected((prev) => {
        const stillValid = prev.filter((id) =>
          items.some((item) => item.cartDetailId === id)
        );
        if (stillValid.length > 0) return stillValid;
        return items.map((item) => item.cartDetailId);
      });
      setError("");
    } catch (err) {
      console.error("Fetch cart error:", err);
      setError(err.message || "Không thể tải giỏ hàng.");
      setCart([]);
    } finally {
      setLoading(false);
    }
  }, [userId]);

  useEffect(() => {
    fetchCart();
  }, [fetchCart]);

  useEffect(() => {
    setSelected((prev) =>
      prev.filter((id) => cart.some((item) => item.cartDetailId === id))
    );
  }, [cart]);

  const toggleSelect = (id) => {
    setSelected((prev) =>
      prev.includes(id) ? prev.filter((x) => x !== id) : [...prev, id]
    );
  };

  useEffect(() => {
    const totalPrice = cart
      .filter((item) => selected.includes(item.cartDetailId))
      .reduce((sum, i) => sum + i.price * i.quantity, 0);
    setTotal(totalPrice);
  }, [selected, cart]);

  const updateQuantity = async (id, change) => {
    const item = cart.find((x) => x.cartDetailId === id);
    if (!item) return;
    const newQty = item.quantity + change;
    if (newQty < 1) return;
    try {
      const res = await fetch(`/api/cart/update/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({ quantity: newQty }),
      });

      if (!res.ok) {
        const errText = await res.text();
        throw new Error(errText || "Cập nhật số lượng thất bại.");
      }

      await fetchCart();
      setError("");
    } catch (e) {
      setError(e.message);
    }
  };

  const deleteItem = async (id) => {
    if (!window.confirm("Bạn có chắc muốn xóa sản phẩm này khỏi giỏ hàng?"))
      return;

    try {
      const res = await fetch(`/api/cart/remove/${id}`, {
        method: "DELETE",
        credentials: "include",
      });

      if (!res.ok) {
        const errText = await res.text();
        throw new Error(errText || "Xóa sản phẩm thất bại.");
      }

      await fetchCart();
      setError("");
    } catch (e) {
      setError(e.message);
    }
  };

  return (
    <div className="p-6 max-w-6xl mx-auto">
      <h1 className="text-3xl font-bold text-red-500 mb-5">🛒 GIỎ HÀNG</h1>

      {error && <p className="text-red-600 mb-3">{error}</p>}
      {loading && <p className="text-gray-500">Đang tải giỏ hàng...</p>}

      {!userId ? (
        <p className="text-center text-gray-500 mt-10">
          Vui lòng đăng nhập để xem giỏ hàng.
        </p>
      ) : cart.length === 0 && !loading ? (
        <p className="text-gray-500 text-center mt-10">
          Giỏ hàng của bạn đang trống 😢
        </p>
      ) : (
        cart.length > 0 && (
          <>
            <div className="overflow-x-auto">
              <table className="w-full border-t border-gray-300 text-sm">
              <thead>
                <tr className="text-left font-semibold bg-gray-100">
                  <th className="p-2"></th>
                  <th className="p-2">Sản phẩm</th>
                  <th className="p-2">Đơn giá</th>
                  <th className="p-2 text-center">Số lượng</th>
                  <th className="p-2 text-right">Thành tiền</th>
                  <th className="p-2"></th>
                </tr>
              </thead>
              <tbody>
                {cart.map((item) => (
                  <tr
                    key={item.cartDetailId}
                    className="border-t hover:bg-gray-50"
                  >
                    <td className="p-2">
                      <input
                        type="checkbox"
                        checked={selected.includes(item.cartDetailId)}
                        onChange={() => toggleSelect(item.cartDetailId)}
                      />
                    </td>

                    <td className="p-2">
                      <div className="flex items-center gap-3">
                        <img
                          src={item.imageUrl}
                          alt={item.product?.name}
                          className="w-16 h-16 object-cover rounded"
                        />
                        <div>
                          <p className="font-semibold">
                            {item.product?.name || "Sản phẩm"}
                          </p>
                          <p className="text-gray-500 text-sm">
                            {item.color?.name} / {item.size?.name}
                          </p>
                        </div>
                      </div>
                    </td>

                    <td className="p-2">
                      {item.price?.toLocaleString("vi-VN")}₫
                    </td>

                    <td className="p-2 text-center">
                      <div className="inline-flex items-center border rounded">
                        <button
                          onClick={() => updateQuantity(item.cartDetailId, -1)}
                          className="px-2 py-1 hover:bg-gray-200"
                        >
                          -
                        </button>
                        <span className="px-3">{item.quantity}</span>
                        <button
                          onClick={() => updateQuantity(item.cartDetailId, 1)}
                          className="px-2 py-1 hover:bg-gray-200"
                        >
                          +
                        </button>
                      </div>
                    </td>

                    <td className="p-2 text-right">
                      {(item.price * item.quantity).toLocaleString("vi-VN")}₫
                    </td>

                    <td className="p-2 text-right">
                      <button
                        onClick={() => deleteItem(item.cartDetailId)}
                        className="text-red-500 hover:underline"
                      >
                        Xóa
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
              </table>
            </div>

            <div className="mt-6 text-right">
              <p className="text-lg font-bold">
                Tổng tiền ({selected.length} sản phẩm):{" "}
                {total.toLocaleString("vi-VN")}₫
              </p>
              <button
                className="border px-6 py-2 mt-3 bg-red-500 text-white rounded hover:bg-red-600"
                disabled={selected.length === 0}
              >
                Mua ngay
              </button>
            </div>
          </>
        )
      )}
    </div>
  );
}

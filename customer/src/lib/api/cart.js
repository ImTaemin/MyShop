import client from "./client";

export const saveItem = async (formData) => {
  return await client.post("/customer/cart", formData);
}

export const loadCartItems = () => {
  return client.get("/customer/cart");
}

export const updateCartQuantity = async (formData) => {
  return await client.put("/customer/cart", formData);
}

export const deleteCartItem = async (itemId) => {
  return await client.delete("/customer/cart", {
    data: itemId
  });
}
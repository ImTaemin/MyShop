import client from "./client";

export const saveItem = (formData) => {
  return client.post("/customer/cart", formData);
}

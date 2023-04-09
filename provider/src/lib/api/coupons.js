import client from "./client";

export const registerCoupon = (formData) => {
  return client.post("/coupon", formData, {
    headers: {
      "Content-Type": "application/json"
    }
  });
}

export const listCoupon = () => {
  return client.get("/coupon");
}

export const updateCoupon = (formData) => {
  return client.put("/coupon", formData, {
    headers: {
      "Content-Type": "application/json"
    }
  });
}

export const deleteCoupon = (couponId) => {
  return client.delete(`/coupon/${couponId}`);
}
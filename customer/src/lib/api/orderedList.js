import client from "./client";

export const loadOrderList = (page) => {
  const pageRequest = {page};

  return client.get("/customer/order", {params: pageRequest});
}
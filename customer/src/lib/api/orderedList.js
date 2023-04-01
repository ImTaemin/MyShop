import client from "./client";
import qs from "qs";

export const loadOrderList = (page) => {
  const queryString = qs.stringify({page});

  return client.get(`/customer/order?${queryString}`);
}
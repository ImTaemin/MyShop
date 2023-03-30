import client from "./client";

export const loadFavoriteItems = () => {
  return client.get(`/customer/favorite`);
}
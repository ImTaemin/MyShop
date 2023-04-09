import client from "./client";

export const loadFavoriteItems = () => {
  return client.get(`/customer/favorite`);
}

export const addFavorite = (itemId) => {
  return client.post("/customer/favorite", itemId);
}
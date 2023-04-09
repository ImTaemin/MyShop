import client from "./client";

export const readItem = (itemId) => {
  return client.get(`/item/${itemId}`);
}

export const registerItem = (formData) => {
  return client.post(`/item`, formData, {
    headers: {
      "Content-Type": "multipart/form-data"
    }
  })
}

export const updateItem = (formData) => {
  return client.put(`/item`, formData, {
    headers: {
      "Content-Type": "multipart/form-data"
    }
  })
}
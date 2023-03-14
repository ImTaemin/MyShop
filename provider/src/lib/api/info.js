import client from "./client";

export const getInfo = async () => {
  return await client.get(`/provider`);
}

export const updateInfo = (formData) => {
  return client.put("/provider", formData, {
    headers: {
      "Content-Type": "application/json"
    }
  })
}
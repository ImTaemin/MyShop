import client from "./client";

export const loadItem = async ({itemId,quantity}) => {
  return {item: await client.get(`/item/${itemId}`), quantity};
}

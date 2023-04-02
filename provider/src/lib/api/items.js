import client from "./client";

export const listItems = (page) => {
  const pageRequest = {page};

  return client.get(`/item`, {params: pageRequest});
}

export const deleteItems = (checkItemIds) => {
  return client.post(`/item/delete`, {
    itemIdList: checkItemIds
  });
}
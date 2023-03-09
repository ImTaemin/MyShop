import client from "./client";

export const listItems = ({page}) => {
  return client.get(`/item`, {
    page
  });
}

export const deleteItems = (checkItemIds) => {
  return client.post(`/item/delete`, {
    itemIdList: checkItemIds
  });
}
import client from "./client";
import qs from 'qs';

export const listCategoryItem = ({page, type}) => {
  const queryString = qs.stringify({page});

  return client.get(`/item/category/${type}?${queryString}`);
}

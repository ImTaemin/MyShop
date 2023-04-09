import client from "./client";

export const loadCategoryItems = ({type, page}) => {
  const pageRequest = {page};

  return client.get(`/item/category/${type}`, {params: pageRequest});
}

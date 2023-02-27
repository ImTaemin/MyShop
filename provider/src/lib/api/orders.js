import client from "./client";
import qs from 'qs';

export const listOrders = ({page, orderStatus}) => {
  const queryString = qs.stringify({
    page, status: orderStatus
  });

  return client.get(`/provider/order?${queryString}`);
};

export const changeOrders = (({checkOrderList: orderNoList, orderStatus}) => {
  client.put('/provider/order', {
    orderNoCntList: orderNoList,
    orderStatus: orderStatus
  });
})
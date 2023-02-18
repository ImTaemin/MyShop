import client from "./client";
import qs from 'qs';

export const listOrders = (({page, orderStatus}) => {
  if (page || orderStatus) {
    const queryString = qs.stringify({
      page, status: orderStatus
    });

    console.log(process.env.REACT_APP_API_MYSHOP + `/provider/order?${queryString}`);
    return client.get(process.env.REACT_APP_API_MYSHOP + `/provider/order?${queryString}`);
  }

  console.log(process.env.REACT_APP_API_MYSHOP + `/provider/order`);
  return client.get(process.env.REACT_APP_API_MYSHOP + "/provider/order/");
});

export const changeOrders = (({checkOrderList: orderNoList, orderStatus}) => {
  client.put(process.env.REACT_APP_API_MYSHOP + '/provider/order', {
    orderNoList: orderNoList,
    orderStatus: orderStatus
  });
})
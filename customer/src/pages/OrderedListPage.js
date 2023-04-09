import React, {useCallback, useEffect, useRef} from "react";
import "../assets/scss/order-item.scss";
import {useDispatch, useSelector} from "react-redux";
import {loadOrderedList, unLoadOrderedList} from "../modules/orderedList";
import client from "../lib/api/client";
import Loader from "../components/loader/Loader";
import OrderedItem from "../components/ordered/OrderedItem";

const OrderedListPage = () => {
  const dispatch = useDispatch();

  const {ordered, error, page, isLast, loading} = useSelector(({orderedList, loading}) => ({
    ordered: orderedList.ordered,
    error: orderedList.error,
    page: orderedList.page,
    isLast: orderedList.isLast,
    loading: loading['orderedList/LOAD_ORDERED_LIST'],
  }));

  const observerRef = useRef(null);
  const observerCallback = useCallback((entries) => {
    const target = entries[0];
    if (target.isIntersecting && !loading && !isLast) {
      dispatch(loadOrderedList(page));
    }
  }, [page, isLast, dispatch]);

  useEffect(() => {
    client.defaults.headers.common['X-AUTH-TOKEN'] = localStorage.getItem("customerAccessToken");

    if (observerRef.current) {
      const observer = new IntersectionObserver(observerCallback);
      observer.observe(observerRef.current);

      return () => {
        dispatch(unLoadOrderedList());
        observer.disconnect();
      }
    }
  }, [observerRef.current, observerCallback, page, isLast]);

  return (
    <div className="order-form-container">
      <h5 className="title">주문 목록</h5>
      <div className="order-form-wrap">
        <table
          className="order-item-table"
        >
          <thead>
            <tr>
              <th width="50%">상품 정보</th>
              <th width="10%">주문일</th>
              <th width="20%">주문번호</th>
              <th width="10%">지불금액(수량)</th>
              <th width="10%">주문상태</th>
            </tr>
          </thead>
          {loading && (<><Loader /></>)}
          {error && (<>error</>)}
          {!loading && (
            <tbody>
              {ordered && ordered.map((order, index) => (
                <OrderedItem item={order} key={index}/>
              ))}
            </tbody>
          )}
        </table>
        {!error && !loading && !isLast && (
          <div style={{width: "100%", height: "30px"}} ref={observerRef} />
        )}
      </div>
    </div>
  );
}

export default OrderedListPage;
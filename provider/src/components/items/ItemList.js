import {Table} from "react-bootstrap";
import ItemListItem from "./ItemListItem";
import {useDispatch, useSelector} from "react-redux";
import {useCallback, useEffect, useRef} from "react";
import {listItem, unLoadItems} from "../../modules/items";
import Loader from "../loader/Loader";
import client from "../../lib/api/client";

const ItemList = (props) => {

  const dispatch = useDispatch();

  const {items, error, checkItemIds, page, isLast, loading} = useSelector(({items, loading}) => ({
    items: items.items,
    error: items.error,
    checkItemIds: items.checkItemIds,
    page: items.page,
    isLast: items.isLast,
    loading: loading['items/LIST_ITEMS'],
  }));

  const observerRef = useRef(null);
  const observerCallback = useCallback((entries) => {
    const target = entries[0];
    if (target.isIntersecting && !loading && !isLast) {
      dispatch(listItem(page));
    }
  }, [page, isLast, dispatch]);

  useEffect(() => {
    client.defaults.headers.common['X-AUTH-TOKEN'] = localStorage.getItem("accessToken");

    if (observerRef.current) {
      const observer = new IntersectionObserver(observerCallback);
      observer.observe(observerRef.current);

      return () => {
        observer.disconnect();
      }
    }
  }, [observerRef.current, observerCallback, page, isLast, props.isRegModalChanged, props.isDelModalChanged]);

  useEffect(() => {
    dispatch(unLoadItems());
  }, []);

  // useEffect(() => {
  //   client.defaults.headers.common['X-AUTH-TOKEN'] = localStorage.getItem("accessToken");
  //   dispatch(listItem(page));
  //
  //   return () => {
  //     dispatch(unLoadItems());
  //
  //   }
  // }, [dispatch, page, props.isRegModalChanged, props.isDelModalChanged]);

  return (
    <>
      <Table hover="true">
        <thead>
        <tr align="center">
          <th width="5%">　</th>
          <th width="15%">이미지</th>
          <th width="20%">상품명</th>
          <th width="20%">상품 코드</th>
          <th width="15%">상품 타입</th>
          <th width="15%">가격</th>
          <th width="10%">재고</th>
        </tr>
        </thead>

        <tbody>
        {
          items && (
            items.map((item, index) => (
              <ItemListItem
                item={item}
                checked={checkItemIds.includes(item.id.toString())}
                key={index}
                handleSuccess={props.handleSuccess}
                handleError={props.handleError}
              />
          ))
          )
        }
        </tbody>
      </Table>
      {!error && !loading && !isLast && (
        <div style={{width: "100%", height: "30px"}} ref={observerRef} />
      )}
      {loading && (
        <Loader />
      )}
      {error && (
        <>{error.msg}</>
      )}
    </>
  )
}

export default ItemList;
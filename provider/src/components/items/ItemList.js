import {Table} from "react-bootstrap";
import ItemListItem from "./ItemListItem";
import {useDispatch, useSelector} from "react-redux";
import {useEffect} from "react";
import {listItems, unLoadItems} from "../../modules/items";
import Loader from "../loader/Loader";
import client from "../../lib/api/client";

const ItemList = (props) => {

  const dispatch = useDispatch();

  const {items, error, checkItemIds, page, loading} = useSelector(({items, loading}) => ({
    items: items.items,
    error: items.error,
    checkItemIds: items.checkItemIds,
    page: items.page,
    loading: loading['items/LIST_ITEMS'],
  }));

  useEffect(() => {
    // TODO: 이 코드를 넣어주지 않으면 첫 요청에 토큰을 포함하지 않는다. 왜지..?
    client.defaults.headers.common['X-AUTH-TOKEN'] = localStorage.getItem("accessToken");

    dispatch(listItems(page));

    return () => {
      dispatch(unLoadItems())
    }
  }, [dispatch, page, props.isRegModalChanged, props.isDelModalChanged]);

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
          !loading && items && (
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
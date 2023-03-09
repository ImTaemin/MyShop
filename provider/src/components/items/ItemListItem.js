import {CustomCheckBox} from "../common/StyledComponents";
import React, {useCallback, useState} from "react";
import {useDispatch} from "react-redux";
import {checkItemIds} from "../../modules/items";
import ItemUpdateModalContainer from "./ItemUpdateModalContainer";
import "../../scss/Alert.scss";


const ItemListItem = (props) => {
  const dispatch = useDispatch();
  const {item, checked, handleSuccess, handleError} = props;
  const {id, mainImage, name, code, itemType, price, quantity} = item;

  const [updateModalShow, setUpdateModalShow] = useState(false);

  const onCheckBox = useCallback((e) => {
    const itemId = e.target.value;
    dispatch(checkItemIds(itemId));
  }, [dispatch]);


  return (
    <>
      <tr align="center">
        <td>
            <CustomCheckBox value={id} type="checkbox" checked={checked} onChange={onCheckBox}/>
        </td>
        <td onClick={() => setUpdateModalShow(true)}>
          <img
            src={mainImage}
            className="rounded-3"
            width="50"
            height="50"
          />
        </td>
        <td onClick={() => setUpdateModalShow(true)}>{name}</td>
        <td onClick={() => setUpdateModalShow(true)}>{code}</td>
        <td onClick={() => setUpdateModalShow(true)}>{itemType}</td>
        <td onClick={() => setUpdateModalShow(true)}>\ {price.toLocaleString()}</td>
        <td onClick={() => setUpdateModalShow(true)}>{quantity}</td>
      </tr>

      {/* 모달 */}
      {updateModalShow && (
        <ItemUpdateModalContainer
          show={updateModalShow}
          onHide={() => setUpdateModalShow(false)}
          handleSuccess={() => handleSuccess('')}
          handleError={handleError}
          itemId={item.id}
        />
      )}
    </>
  )
}

export default ItemListItem;
import "../../scss/ItemRegister.scss"
import "../../scss/Debounce.scss";
import React, {useEffect} from "react";
import SwiperCore, {Navigation, Pagination} from 'swiper/core';
import 'swiper/css';
import 'swiper/css/navigation';
import 'swiper/css/pagination';
import {useDispatch, useSelector} from "react-redux";
import {readItem, unLoadItem} from "../../modules/item";
import ItemUpdateModal from "./ItemUpdateModal";

SwiperCore.use([Navigation, Pagination]);

const ItemUpdateModalContainer = (props) => {
  const {itemId, show, onHide, handleSuccess, handleError} = props;
  const dispatch = useDispatch();
  const {item} = useSelector(({item}) => ({
      item: item.item
    })
  );

  useEffect(() => {
    dispatch(readItem(itemId));

    return () => {
      dispatch(unLoadItem());
    }
  }, [dispatch, itemId]);


  return (
    <ItemUpdateModal
      item={item}
      show={show}
      onHide={onHide}
      handleSuccess={handleSuccess}
      handleError={handleError}
    />
  );
}

export default ItemUpdateModalContainer;
import {useParams} from "react-router";
import React, {useCallback, useEffect, useRef} from "react";
import {useDispatch, useSelector} from "react-redux";
import {loadCategoryItems, unloadCategoryItems} from "../modules/categoryItems";
import Item from "../components/Item/Item";
import Loader from "../components/loader/Loader";
import "../assets/scss/item-list.scss";
import client from "../lib/api/client";

const ItemListPage = () => {
  const { type } = useParams();
  const dispatch = useDispatch();

  const {categoryItems, error, page, isLast, loading} = useSelector(({categoryItems, loading}) => ({
    categoryItems: categoryItems.categoryItems,
    error: categoryItems.error,
    page: categoryItems.page,
    isLast: categoryItems.isLast,
    loading: loading['categoryItems/LIST_CATEGORY_ITEM'],
  }));

  const observerRef = useRef(null);
  const observerCallback = useCallback((entries) => {
    const target = entries[0];
    if (target.isIntersecting && !loading && !isLast) {
      dispatch(loadCategoryItems({type, page}));
    }
  }, [type, page, isLast, dispatch]);

  useEffect(() => {
    client.defaults.headers.common['X-AUTH-TOKEN'] = localStorage.getItem("accessToken");

    if (observerRef.current) {
      const observer = new IntersectionObserver(observerCallback);
      observer.observe(observerRef.current);

      return () => {
        observer.disconnect();
      }
    }
  }, [observerRef.current, observerCallback, type, page, isLast]);

  useEffect(() => {
    dispatch(unloadCategoryItems());
  }, [type]);

  return (
    <>
      {loading && (
        <Loader />
      )}
      {!loading && categoryItems && (
        <>
          <div className="item-list">
            {categoryItems.map((item, index) => (
              <Item item={item} key={index}/>
            ))}
          </div>
          {!error && !loading && !isLast && (
            <div style={{width: "100%", height: "30px"}} ref={observerRef} />
          )}
        </>
      )}
      {error && (
        <>{error.msg}</>
      )}
    </>
  );
}

export default ItemListPage;
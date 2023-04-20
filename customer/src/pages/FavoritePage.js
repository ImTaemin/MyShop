import Loader from "../components/loader/Loader";
import Item from "../components/Item/Item";
import {useDispatch, useSelector} from "react-redux";
import {useEffect} from "react";
import {loadFavoriteItems, unloadFavoriteItems} from "../modules/favoriteItems";
import client from "../lib/api/client";
import "../assets/scss/item-list.scss";

const FavoritePage = () => {
  const dispatch = useDispatch();

  const {favoriteItems, error, loading} = useSelector(({favoriteItems, loading}) => ({
    favoriteItems: favoriteItems.favoriteItems,
    error: favoriteItems.error,
    loading: loading['favoriteItems/LOAD_FAVORITE_ITEMS'],
  }));

  useEffect(() => {
    client.defaults.headers.common['X-AUTH-TOKEN'] = localStorage.getItem("customerAccessToken");

    dispatch(loadFavoriteItems());

    return () => {
      dispatch(unloadFavoriteItems());
    }
  }, [dispatch]);

  return (
    <>
      {favoriteItems && favoriteItems.length === 0 && (
        <div className="text-xxl-center py-3 fs-4">좋아요를 표시한 상품이 없습니다.</div>
      )}
      {error && (
        <div>{error.msg}</div>
      )}
      {loading && (
        <Loader />
      )}
      {!loading && favoriteItems && (
        <div className="item-list">
          {favoriteItems.map((item, index) => (
            <Item item={item} key={index} />
          ))}
        </div>
      )}
    </>

  );
}

export default FavoritePage;
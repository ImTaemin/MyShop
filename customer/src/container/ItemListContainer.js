import {useParams} from "react-router";
import {useEffect} from "react";
import {useDispatch, useSelector} from "react-redux";
import {listCategoryItem, unloadCategoryItems} from "../modules/categoryItems";
import Item from "../components/Item/Item";
import Loader from "../components/loader/Loader";
import "../assets/scss/item.scss";

const ItemListContainer = () => {
  const { type } = useParams();
  const dispatch = useDispatch();

  const {categoryItems, error, page, loading} = useSelector(({categoryItems, loading}) => ({
    categoryItems: categoryItems.categoryItems,
    error: categoryItems.error,
    page: categoryItems.page,
    loading: loading['categoryItems/LIST_CATEGORY_ITEM'],
  }));

  useEffect(() => {
    dispatch(listCategoryItem({page, type}));

    return () => {
      dispatch(unloadCategoryItems());
    }
  }, [dispatch, page, type]);

  return (
    <div className="item-list-wrap">
      <div className="item-list">
        {loading && (
          <Loader />
        )}
        {
          !loading && categoryItems && (
            console.log(categoryItems),
            categoryItems.map((item, index) => (
              <Item item={item}/>
            ))
          )
        }
        {error && (
          <div>{error.msg}</div>
        )}
      </div>
    </div>
  );
}

export default ItemListContainer;
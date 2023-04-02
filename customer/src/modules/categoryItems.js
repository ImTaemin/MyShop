import createRequestSaga, {createRequestActionTypes} from "../lib/createRequestSaga";
import {createAction, handleActions} from "redux-actions";
import * as itemsAPI from "../lib/api/items";
import {takeLatest} from "redux-saga/effects";

// action
const [LOAD_CATEGORY_ITEMS, LOAD_CATEGORY_ITEM_SUCCESSS, LOAD_CATEGORY_ITEM_FAILURES] =
  createRequestActionTypes('categotyItems/LOAD_CATEGORY_ITEMS');
const UNLOAD_CATEGORY_ITEM = 'categotyItems/UNLOAD_CATEGORY_ITEM';

// action creators
export const loadCategoryItems = createAction(LOAD_CATEGORY_ITEMS,({type, page}) => ({type, page}));
export const unloadCategoryItems = createAction(UNLOAD_CATEGORY_ITEM);

// redux-saga
const loadCategoryItemsSaga = createRequestSaga(LOAD_CATEGORY_ITEMS, itemsAPI.loadCategoryItems);

export function* categoryItemSaga() {
  yield takeLatest(LOAD_CATEGORY_ITEMS, loadCategoryItemsSaga);
}

// init
const initialState = {
  categoryItems: [],
  error: null,
  page: 0,
  isLast: false,
}

// reducer
const categoryItems = handleActions({
    [LOAD_CATEGORY_ITEM_SUCCESSS]: (state, {payload: categoryItems}) => ({
      ...state,
      categoryItems: state.categoryItems.concat(categoryItems.data.content),
      page: categoryItems.data.pageable.pageNumber + 1,
      isLast: categoryItems.data.last,
    }),

    [LOAD_CATEGORY_ITEM_FAILURES]: (state, {payload: error}) => ({
      ...state,
      error: error.response.data,
    }),

    [UNLOAD_CATEGORY_ITEM]: () => initialState,
  },

  initialState
);

export default categoryItems;
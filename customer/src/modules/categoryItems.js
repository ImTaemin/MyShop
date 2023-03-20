import createRequestSaga, {createRequestActionTypes} from "../lib/createRequestSaga";
import {createAction, handleActions} from "redux-actions";
import * as itemsAPI from "../lib/api/categoryItems";
import {takeLatest} from "redux-saga/effects";

// action
const [LIST_CATEGORY_ITEM, LIST_CATEGORY_ITEM_SUCCESS, LIST_CATEGORY_ITEM_FAILURE] =
  createRequestActionTypes('categotyItems/LIST_CATEGORY_ITEM');
const UNLOAD_CATEGORY_ITEM = 'categotyItems/UNLOAD_CATEGORY_ITEM';

// action creators
export const listCategoryItem = createAction(LIST_CATEGORY_ITEM,({type, page}) => ({type, page}));
export const unloadCategoryItems = createAction(UNLOAD_CATEGORY_ITEM);

// redux-saga
const listCategoryItemSaga = createRequestSaga(LIST_CATEGORY_ITEM, itemsAPI.listCategoryItem);

export function* categoryItemSaga() {
  yield takeLatest(LIST_CATEGORY_ITEM, listCategoryItemSaga);
}

// init
const initialState = {
  categoryItems: null,
  error: null,
  page: 0,
}

// reducer
const categoryItems = handleActions({
    [LIST_CATEGORY_ITEM_SUCCESS]: (state, {payload: categoryItems}) => ({
      ...state,
      categoryItems: categoryItems.data.content,
      page: categoryItems.data.pageable.pageNumber,
    }),

    [LIST_CATEGORY_ITEM_FAILURE]: (state, {payload: error}) => ({
      ...state,
      error: error.response.data,
    }),

    [UNLOAD_CATEGORY_ITEM]: () => initialState,
  },

  initialState
);

export default categoryItems;
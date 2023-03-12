import createRequestSaga, {createRequestActionTypes} from "../lib/createRequestSaga";
import {createAction, handleActions} from "redux-actions";
import {takeLatest} from "redux-saga/effects";
import * as itemsAPI from "../lib/api/items";

// action
const [LIST_ITEM, LIST_ITEM_SUCCESS, LIST_ITEM_FAILURE] =
  createRequestActionTypes('items/LIST_ITEM');
const CHECK_ITEM_IDS = 'items/CHECK_ITEM_IDS';
const UNLOAD_ITEMS = 'items/UNLOAD_ITEM';

// action creators
export const listItem = createAction(LIST_ITEM,(page) => (page));
export const checkItemIds = createAction(CHECK_ITEM_IDS, (itemId) => (itemId));
export const unLoadItems = createAction(UNLOAD_ITEMS);

// redux-saga
const listItemSaga = createRequestSaga(LIST_ITEM, itemsAPI.listItems);

export function* itemsSaga() {
  yield takeLatest(LIST_ITEM, listItemSaga);
}

// init
const initialState = {
  items: null,
  error: null,
  page: 0,
  checkItemIds: []
};

const items = handleActions({
    [LIST_ITEM_SUCCESS]: (state, {payload: items}) => ({
      ...state,
      items: items.data.content,
      page: items.data.pageable.pageNumber,
    }),

    [LIST_ITEM_FAILURE]: (state, {payload: error}) => ({
      ...state,
      error: error.response.data,
    }),

    [CHECK_ITEM_IDS]: (state, {payload: itemId}) => ({
      ...state,
      checkItemIds: state.checkItemIds.includes(itemId)
        ? state.checkItemIds.filter(checkedItemId => {
          return !(checkedItemId === itemId)
        })
        : state.checkItemIds.concat(itemId)
    }),

    [UNLOAD_ITEMS]: () => initialState,

  },
  initialState
);

export default items;
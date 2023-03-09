import createRequestSaga, {createRequestActionTypes} from "../lib/createRequestSaga";
import {createAction, handleActions} from "redux-actions";
import {takeLatest} from "redux-saga/effects";
import * as itemsAPI from "../lib/api/items";

// action
const [LIST_ITEMS, LIST_ITEMS_SUCCESS, LIST_ITEMS_FAILURE] =
  createRequestActionTypes('items/LIST_ITEMS');
const CHECK_ITEM_IDS = 'items/CHECK_ITEM_IDS';
const UNLOAD_ITEMS = 'items/UNLOAD_ITEMS';

// action creators
export const listItems = createAction(LIST_ITEMS,(page) => (page));
export const checkItemIds = createAction(CHECK_ITEM_IDS, (itemId) => (itemId));
export const unLoadItems = createAction(UNLOAD_ITEMS);

// redux-saga
const listItemsSaga = createRequestSaga(LIST_ITEMS, itemsAPI.listItems);

export function* itemsSaga() {
  yield takeLatest(LIST_ITEMS, listItemsSaga);
}

// init
const initialState = {
  items: null,
  error: null,
  page: 0,
  checkItemIds: []
};

const items = handleActions({
    [LIST_ITEMS_SUCCESS]: (state, {payload: items}) => ({
      ...state,
      items: items.data.content,
      page: items.data.pageable.pageNumber,
    }),

    [LIST_ITEMS_FAILURE]: (state, {payload: error}) => ({
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
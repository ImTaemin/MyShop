import createRequestSaga, {createRequestActionTypes} from "../lib/createRequestSaga";
import {createAction, handleActions} from "redux-actions";
import * as itemAPI from "../lib/api/item";
import {takeLatest} from "redux-saga/effects";

const [READ_ITEM, READ_ITEM_SUCCESS, READ_ITEM_FAILURE] =
  createRequestActionTypes("item/READ_ITEM");
const UNLOAD_ITEM = "item/UNLOAD_ITEM";

export const readItem = createAction(READ_ITEM, (id) => id);
export const unLoadItem = createAction(UNLOAD_ITEM);

const readItemSaga = createRequestSaga(READ_ITEM, itemAPI.readItem);
export function* itemSaga() {
  yield takeLatest(READ_ITEM, readItemSaga);
}

const initialState = {
  item: null,
  error: null,
}

const item = handleActions(
  {
    [READ_ITEM_SUCCESS]: (state, {payload: item}) => ({
      ...state,
      item: item.data.data,
    }),

    [READ_ITEM_FAILURE]: (state, {payload: error}) => ({
      ...state,
      error,
    }),

    [UNLOAD_ITEM]: () => initialState
  },
  initialState,
)

export default item;
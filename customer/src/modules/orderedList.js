import createRequestSaga, {createRequestActionTypes} from "../lib/createRequestSaga";
import {createAction, handleActions} from "redux-actions";
import {takeLatest} from "redux-saga/effects";
import * as orderListAPI from "../lib/api/orderedList";

// action
const [LOAD_ORDERED_LIST, LOAD_ORDERED_LIST_SUCCESS, LOAD_ORDERED_LIST_FAILURE] =
  createRequestActionTypes("orderedList/LOAD_ORDERED_LIST");
const UNLOAD_ORDERED_LIST = "orderedList/UNLOAD_ORDERED_LIST";

// action creators
export const loadOrderedList = createAction(LOAD_ORDERED_LIST, (page) => (page));
export const unLoadOrderedList = createAction(UNLOAD_ORDERED_LIST);

// redux-saga
const loadOrderedListSaga = createRequestSaga(LOAD_ORDERED_LIST, orderListAPI.loadOrderList);

export function* orderedListSaga() {
  yield takeLatest(LOAD_ORDERED_LIST, loadOrderedListSaga);
}

// init
const initialState = {
  ordered: [],
  error: null,
  page: 0,
}

const orderedList = handleActions({
    [LOAD_ORDERED_LIST_SUCCESS]: (state, {payload: orderedList}) => (
      console.log(orderedList),{
      ...state,
      ordered: orderedList.data.content,
      page: orderedList.data.pageable.pageNumber
    }),

    [LOAD_ORDERED_LIST_FAILURE]: (state, {payload: error}) => ({
      ...state,
      error,
    }),
  },
  initialState
);

export default orderedList;
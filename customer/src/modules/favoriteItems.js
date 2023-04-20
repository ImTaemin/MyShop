import createRequestSaga, {createRequestActionTypes} from "../lib/createRequestSaga";
import {createAction, handleActions} from "redux-actions";
import {takeLatest} from "redux-saga/effects";
import * as favoriteAPI from "../lib/api/favorite";

// action
const [LOAD_FAVORITE_ITEMS, LOAD_FAVORITE_ITEMS_SUCCES, LOAD_FAVORITE_ITEMS_FAILURE] =
  createRequestActionTypes("favoriteItems/LOAD_FAVORITE_ITEMS");
const UNLOAD_FAVORITE_ITEMS = "favoriteItems/UNLOAD_FAVORITE_ITEMS";

// action creators
export const loadFavoriteItems = createAction(LOAD_FAVORITE_ITEMS);
export const unloadFavoriteItems = createAction(UNLOAD_FAVORITE_ITEMS);

// redux-saga
const loadFavoriteItemsSaga = createRequestSaga(LOAD_FAVORITE_ITEMS, favoriteAPI.loadFavoriteItems);

export function* favoriteItemsSaga() {
  yield takeLatest(LOAD_FAVORITE_ITEMS, loadFavoriteItemsSaga);
}

// init
const initialState = {
  favoriteItems: null,
  error: null,
}

// reducer
const favoriteItems = handleActions({
    [LOAD_FAVORITE_ITEMS_SUCCES]: (state, {payload: favoriteItems}) => ({
      ...state,
      favoriteItems: favoriteItems.data.data,
    }),

    [LOAD_FAVORITE_ITEMS_FAILURE]: (state, {payload: error}) => ({
      ...state,
      error: error.response.data,
    }),

    [UNLOAD_FAVORITE_ITEMS]: () => initialState,
  },

  initialState
);

export default favoriteItems;

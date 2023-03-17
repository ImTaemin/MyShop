import {combineReducers} from "@reduxjs/toolkit";
import categoryItems, {categoryItemSaga} from "./categoryItems";
import { all } from 'redux-saga/effects';
import loading from "./loading";

const rootReducer = combineReducers({
  loading,
  categoryItems,
});

export function* rootSaga() {
  yield all([categoryItemSaga()]);
}

export default rootReducer;
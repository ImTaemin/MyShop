import createRequestSaga, {createRequestActionTypes} from "../lib/createRequestSaga";
import {createAction, handleActions} from "redux-actions";
import {takeLatest} from "redux-saga/effects";
import * as authAPI from "../lib/api/auth";

// action
const [LOAD_AUTH_INFO, LOAD_AUTH_INFO_SUCCESS, LOAD_AUTH_INFO_FAILURE] =
  createRequestActionTypes("mypage/LOAD_AUTH_INFO");
const CHANGE_FIELD = "mypage/CHANGE_FIELD";
const UNLOAD_AUTH_INFO = "mypage/UNLOAD_AUTH_INFO";

// action creators
export const loadAuthInfo = createAction(LOAD_AUTH_INFO);
export const unLoadAuthInfo = createAction(UNLOAD_AUTH_INFO);
export const changeField = createAction(
  CHANGE_FIELD,
  ({name, value}) => ({name, value})
);

// redux-saga
const loadAuthInfoSaga = createRequestSaga(LOAD_AUTH_INFO, authAPI.loadAuthInfo);

export function* authInfoSaga() {
  yield takeLatest(LOAD_AUTH_INFO, loadAuthInfoSaga);
}

// init
const initialState = {
  userId: '',
  password: '',
  modifyPassword: '',
  phone: '',
  name: '',
  createDate: '',
}

// reducer
const mypage = handleActions({
    [LOAD_AUTH_INFO_SUCCESS]: (state, {payload: authInfo}) => ({
      ...state,
      userId: authInfo.data.userId,
      phone: authInfo.data.phone,
      name: authInfo.data.name,
      createDate: authInfo.data.createDate
    }),

    [LOAD_AUTH_INFO_FAILURE]: (state, {payload: error}) => ({
      ...state,
      error: error.response.data,
    }),

    [CHANGE_FIELD]: (state, {payload: {name, value} }) => ({
      ...state,
      [name]: value
    }),

    [UNLOAD_AUTH_INFO]: () => initialState,
  },

  initialState
);

export default mypage;
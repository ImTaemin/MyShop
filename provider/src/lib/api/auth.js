import axios from "axios";
import client from "./client";
import {removeCookie} from "../cookie";

export const signUp = ((e) => {
  console.log(e.target.value);
  // return axios.post(
  //   process.env.REACT_APP_API_MYSHOP + "/provider/sign-up",
  //   signUpParam);
});

export const signIn = async ({id, password}) => {
  try {
    const response = await axios.post(
      process.env.REACT_APP_API_MYSHOP + "/provider/sign-in",
      {
        userId: id,
        password: password,
      }
    );

    // 전역 헤더 설정
    client.defaults.headers.common['X-AUTH-TOKEN'] = localStorage.getItem("accessToken");

    return response.data;
  } catch (error) {
    return error.response.data;
  }
};

export const signOut = () => {
  localStorage.removeItem("accessToken");
  removeCookie("refreshToken");
}
import axios from "axios";
import {removeCookie} from "../cookie";
import client from "./client";

export const signUp = async ({userId, password, phone, name }) => {

  try {
    const response = await axios.post(
      process.env.REACT_APP_API_MYSHOP + "/customer/sign-up",
      {userId, password, phone, name}
    );

    return response.data;
  } catch (e) {
    return e.response.data;
  }

};

export const signIn = async ({id, password}) => {
  try {
    const response = await axios.post(
      process.env.REACT_APP_API_MYSHOP + "/customer/sign-in",
      {
        userId: id,
        password: password,
      }
    );


    return response.data;
  } catch (error) {
    return error.response.data;
  }
};

export const loadAuthInfo = () => {
  return client.get("/customer");
}

export const submitAuthInfo = (submitAuthData) => {
  return client.put("/customer", submitAuthData);
}

export const signOut = () => {
  localStorage.removeItem("customerAccessToken");
  removeCookie("customerRefreshToken");
}
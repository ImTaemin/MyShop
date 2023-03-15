import axios from "axios";
import {removeCookie} from "../cookie";

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

export const signOut = () => {
  localStorage.removeItem("accessToken");
  removeCookie("refreshToken");
}
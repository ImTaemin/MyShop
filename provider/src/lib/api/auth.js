import axios from "axios";
import {removeCookie} from "../cookie";

export const signUp = async ({userId, password, phone, brandName }) => {

  try {
    const response = await axios.post(
      process.env.REACT_APP_API_MYSHOP + "/provider/sign-up",
      {userId, password, phone, brandName}
    );

    return response.data;
  } catch (e) {
    return e.response.data;
  }

};

export const signIn = async ({id, password}) => {
  try {
    const response = await axios.post(
      process.env.REACT_APP_API_MYSHOP + "/provider/sign-in",
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
  localStorage.removeItem("providerAccessToken");
  removeCookie("providerRefreshToken");
}
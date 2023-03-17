import axios from 'axios';
import {getCookie, setCookie} from "../cookie";

const client = axios.create({
  baseURL: process.env.REACT_APP_API_MYSHOP,
  headers: {
    "Content-Type": "application/json"
  },
});

//
let isReissuance = false;

// 응답 인터셉터
client.interceptors.response.use(
  response => {
    // 요청 성공 시 특정 작업 수행
    return response;
  },
  async (error) => {
    /**
     * accessToken 401 에러면 refreshToken으로 토큰 재발급 후 이전 요청 재요청
     */
    if(error.response.status === 401){
      if(isReissuance) {
        return;
      }
      isReissuance = true;

      const preRefreshToken = getCookie("refreshToken");

      if(preRefreshToken) {
        try{
          const response = await axios.post(`${process.env.REACT_APP_API_MYSHOP}/customer/reissue`, {},{
            headers: {
              "X-AUTH-TOKEN": preRefreshToken
            }
          });

          const {accessToken, refreshToken} = response.data;
          localStorage.setItem("accessToken", accessToken);
          client.defaults.headers.common['X-AUTH-TOKEN'] = accessToken;

          setCookie("refreshToken", refreshToken);
          error.config.headers["X-AUTH-TOKEN"] = accessToken;

          return client(error.config);
        } catch (e) {
          console.error("리프레시 토큰 재발급 에러", e);
        }
      } else {
        console.log("리프레시 토큰이 없습니다.");
      }
    } else {
      console.log(error);
      return error.response;
    }
    // 요청 실패 시 특정 작업 수행
    return Promise.reject(error);
  }
)

export default client;

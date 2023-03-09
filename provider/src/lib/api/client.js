import axios from 'axios';
import {getCookie, setCookie} from "../cookie";

const client = axios.create({
  baseURL: process.env.REACT_APP_API_MYSHOP
});

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
      localStorage.removeItem("accessToken");
      const refreshToken = getCookie("refreshToken");
      await axios.post(`${process.env.REACT_APP_API_MYSHOP}/provider/reissue`, {},{
        headers: {
          "X-AUTH-TOKEN": refreshToken
        }
      }).then((response) => {
        if(response.status === 200){
          const {accessToken, refreshToken} = response.data;
          localStorage.setItem("accessToken", accessToken);
          setCookie("refreshToken", refreshToken);

          /**
           * TODO: 토큰을 저장하긴 하는데 이전 요청 후 리렌더링이 되지 않음
           * 나중에 찾아볼 예정
           */
          error.config.headers["X-AUTH-TOKEN"] = accessToken;
          return client(error.config);
        }
      }).catch((error) => {
        console.log(error);
      });
    } else {
      console.log(error);
      return error.response;
    }
    // 요청 실패 시 특정 작업 수행
    return Promise.reject(error);
  }
)

export default client;

import axios from 'axios';

const client = axios.create();

  client.defaults.baseURL = process.env.REACT_APP_API_MYSHOP;

  // 헤더 설정
  client.defaults.headers.common['X-AUTH-TOKEN'] = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0YWVtaW4iLCJyb2xlcyI6WyJQUk9WSURFUiJdLCJpYXQiOjE2NzY3MjY3OTUsImV4cCI6MTY3NjczMDM5NX0.0cnyHTJU7zOb-zF5mG_SCpVDKbAt3wiBPDkQBrY52bs';

  // 인터셉터 설정
  axios.interceptors.response.use(
    response => {
      // 요청 성공 시 특정 작업 수행
      return response;
    },
    error => {
// 요청 실패 시 특정 작업 수행
      return Promise.reject(error);
    }
  )

export default client;

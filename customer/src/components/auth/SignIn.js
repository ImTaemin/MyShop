import React, {useState} from "react";
import logo from "../../assets/images/logo.png";
import {Alert} from "react-bootstrap";
import {Link, useNavigate} from "react-router-dom";
import {setCookie} from "../../lib/cookie";
import client from "../../lib/api/client";
import {Helmet} from "react-helmet-async";
import {signIn} from "../../lib/api/auth";

const SignIn = ({changeAuthMode, isRegistered, setIsRegistered}) => {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    id: '',
    password: ''
  });

  const {id, password} = formData;
  const [error, setError] = useState('');

  const inputHandler = (e) => {
    const {name, value} = e.target;

    setFormData({
      ...formData,
      [name]: value
    })
  };

  const submitHandler = async (e) => {
    e.preventDefault();

    setFormData({
      id: '',
      password: '',
    });

    try {
      const response = await signIn({ id, password });

      if(response.status === true) {
        localStorage.setItem("customerAccessToken", response.accessToken);
        setCookie("refreshToken", response.refreshToken);

        // 전역 헤더 설정
        client.defaults.headers.common['X-AUTH-TOKEN'] = localStorage.getItem("customerAccessToken");

        navigate('/');
      } else if (response.status === false) {
        setError(response.msg);
      }
    } catch (error) {
      setError(error.msg);
    }
  }

  return (
    <>
      {isRegistered && (
        <div className="alert-box">
          <Alert variant="info" onClose={() => setIsRegistered(false)} dismissible>
            <Alert.Heading>등록되었습니다.</Alert.Heading>
          </Alert>
        </div>
      )}
      {error && (
        <div className="alert-box">
          <Alert variant="danger" onClose={() => setError('')} dismissible>
            <Alert.Heading>{error}</Alert.Heading>
          </Alert>
        </div>
      )}

      <Helmet>
        <title>로그인</title>
      </Helmet>
      <div className="auth-form-container">
        <form className="auth-form" onSubmit={submitHandler}>
          <div className="auth-logo-wrap">
            <Link to="/">
              <img src={logo} className="auth-logo" alt="" />
            </Link>
          </div>
          <div className="auth-form-content">
            <h3 className="auth-form-title">로그인</h3>
            <div className="text-center">
              새 계정 만들기 {"  "}
              <span className="link-primary">
                <span onClick={changeAuthMode}>회원가입</span>
              </span>
            </div>
            <div className="form-group mt-3">
              <label>아이디</label>
              <input
                name="id"
                type="text"
                value={id}
                className="form-control mt-1"
                autoComplete='off'
                placeholder="아이디를 입력해주세요"
                onChange={inputHandler}
                required
              />
            </div>
            <div className="form-group mt-3">
              <label>비밀번호</label>
              <input
                name="password"
                type="password"
                value={password}
                className="form-control mt-1"
                autoComplete='off'
                placeholder="비밀번호를 입력해주세요"
                onChange={inputHandler}
                required
              />
            </div>
            <div className="d-grid gap-2 mt-3">
              <button type="submit" className="btn btn-primary">
                로그인
              </button>
            </div>
          </div>
        </form>
      </div>
    </>
  )
}

export default SignIn;
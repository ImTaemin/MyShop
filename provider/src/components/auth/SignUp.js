import React, {useCallback, useEffect, useState} from "react";
import logo from "../../assets/images/logo.png";
import axios from "axios";
import {debounce} from "lodash";
import {FaTimesCircle, FaCheck} from "react-icons/fa";
import {signUp} from "../../lib/api/auth";
import {Alert} from "react-bootstrap";
import {Helmet} from "react-helmet-async";

const SignUp = ({changeAuthMode, setIsRegistered}) => {
  const [formData, setFormData] = useState({
    userId: '',
    password: '',
    checkPassword: '',
    phone: '',
    brandName: ''
  });

  const {userId, password, checkPassword, phone, brandName} = formData;

  const [isCheckId, setIsCheckId] = useState(false);
  const [isBrandName, setIsBrandName] = useState(false);
  const [isPasswordEqual, setIsPasswordEqual] = useState(false);

  const [error, setError] = useState('');

  const debounceCheckId = useCallback(
    debounce((userId) => {
      axios.get(`${process.env.REACT_APP_API_MYSHOP}/provider/exists/id/${userId}`)
        .then((response) => {
          setIsCheckId(true);
        })
        .catch((err) => {
          setIsCheckId(false);
        });
    }, 300),
    []
  );

  const debounceBrandName = useCallback(
    debounce((brandName) => {
      axios.get(`${process.env.REACT_APP_API_MYSHOP}/provider/exists/brand/${brandName}`)
        .then((response) => {
          setIsBrandName(true);
        })
        .catch((err) => {
          setIsBrandName(false);
        });
    }, 300),
    []
  );

  const inputHandler = useCallback((e) => {
    const {name, value} = e.target;

    setFormData({
      ...formData,
      [name]: value
    });

    switch (name) {
      case "userId":
        debounceCheckId(value);
        break;

      case "brandName":
        debounceBrandName(value);
        break;

      default:
    }
  }, [formData]);

  useEffect(() => {
    setIsPasswordEqual(password === checkPassword && password !== '');
  }, [formData.password, formData.checkPassword, error])

  const submitHandler = async (e) => {
    e.preventDefault();

    if(!isCheckId || !isBrandName || !isPasswordEqual) {
      setError("모든 항목을 만족해야합니다.");
      return;
    }

    setFormData({
      userId: '',
      password: '',
      checkPassword: '',
      phone: '',
      brandName: ''
    });

    setIsPasswordEqual(false);
    setIsCheckId(false);
    setIsBrandName(false);

    try {
      const response = await signUp({userId, password, phone, brandName });

      if(response.status === true) {
        setIsRegistered(true);
        changeAuthMode();
      } else if (response.status === false) {
        setError(response.msg);
      }
    } catch (error) {
      setError("에러");
    }
  }

  return (
    <>
    {error && (
      <div className="alert-box">
        <Alert variant="danger" onClose={() => setError('')} dismissible>
          <Alert.Heading>{error}</Alert.Heading>
        </Alert>
      </div>
    )}

    <Helmet>
      <title>판매자 회원가입</title>
    </Helmet>
    <div className="auth-form-container">
      <form className="auth-form" onSubmit={submitHandler}>
        <div className="auth-logo-wrap">
          <img src={logo} className="auth-logo" alt="" />
        </div>
        <div className="auth-form-content">
          <h3 className="auth-form-title">회원가입</h3>
          <div className="text-center">
            이미 계정이 있습니까?{" "}
            <span className="link-primary">
              <span onClick={changeAuthMode}>로그인</span>
            </span>
          </div>
          <div className="form-group mt-3">
            <label>아이디</label>
            <div className="debounce-container">
              <input
                name={"userId"}
                type="text"
                value={userId}
                className="form-control mt-1"
                autoComplete='off'
                placeholder="아이디를 입력해주세요"
                onChange={inputHandler}
                required
              />
              <div className="debounce-wrap">
                {isCheckId && (
                  <FaCheck />
                )}
                {!isCheckId && (
                  <FaTimesCircle />
                )}
              </div>
            </div>
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
          <div className="form-group mt-3">
            <label>비밀번호 확인</label>
            <div className="debounce-container">
              <input
              name="checkPassword"
              type="password"
              value={checkPassword}
              className="form-control mt-1"
              autoComplete='off'
              placeholder="비밀번호 확인"
              onChange={inputHandler}
              required
            />
              <div className="debounce-wrap">
                {isPasswordEqual && (
                  <FaCheck />
                )}
                {!isPasswordEqual && (
                  <FaTimesCircle />
                )}
              </div>
            </div>
          </div>
          <div className="form-group mt-3">
            <label>핸드폰</label>
            <input
              name="phone"
              type="text"
              value={phone}
              className="form-control mt-1"
              autoComplete='off'
              placeholder="-포함"
              pattern="^\d{2,3}-\d{3,4}-\d{4}$"
              onChange={inputHandler}
              required
            />
          </div>
          <div className="form-group mt-3">
            <label>브랜드명</label>
            <div className="debounce-container">
              <input
                name="brandName"
                type="text"
                value={brandName}
                className="form-control mt-1"
                autoComplete='off'
                placeholder="브랜드명을 입력해주세요"
                onChange={inputHandler}
                required
              />
              <div className="debounce-wrap">
                {isBrandName && (
                  <FaCheck />
                )}
                {!isBrandName && (
                  <FaTimesCircle />
                )}
              </div>
            </div>
          </div>
          <div className="d-grid gap-2 mt-3">
            <button type="submit" className="btn btn-primary">
              회원가입
            </button>
          </div>
        </div>
      </form>
    </div>
    </>
  )
}

export default SignUp;
import React, {useRef} from "react";
import logo from "../../assets/images/logo.png";
import Loader from "../loader/Loader";

const SignUp = ({changeAuthMode, onSignUp}) => {

  return (
    <div className="auth-form-container">
      <form className="auth-form" onSubmit={onSignUp}>
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
            <div className="form-dup-wrap">
              <input
                name="id"
                type="text"
                className="form-control mt-1"
                placeholder="아이디를 입력해주세요"
                required
              />
              <div className="form-dup-loader">
                <Loader />
              </div>
            </div>
          </div>
          <div className="form-group mt-3">
            <label>비밀번호</label>
            <input
              name="password"
              type="password"
              className="form-control mt-1"
              placeholder="비밀번호를 입력해주세요"
              required
            />
          </div>
          <div className="form-group mt-3">
            <label>비밀번호 확인</label>
            <input
              name="password"
              type="password"
              className="form-control mt-1"
              placeholder="비밀번호 확인"
              required
            />
          </div>
          <div className="form-group mt-3">
            <label>핸드폰</label>
            <input
              type="text"
              className="form-control mt-1"
              placeholder="-포함"
              pattern="^\d{2,3}-\d{3,4}-\d{4}$"
              required
            />
          </div>
          <div className="form-group mt-3">
            <label>브랜드명</label>
            <div className="form-dup-wrap">
              <input
                type="text"
                className="form-control mt-1"
                placeholder="브랜드명을 입력해주세요"
                required
              />
              <div className="form-dup-loader">
                <Loader />
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
  )
}

export default SignUp;
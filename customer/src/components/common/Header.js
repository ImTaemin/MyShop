import "../../assets/scss/header.scss";
import {useEffect, useState} from "react";
import {decodeToken} from "react-jwt";
import {Link, useNavigate} from "react-router-dom";
import {signOut} from "../../lib/api/auth";

const Header = () => {
  const [userId, setUserId] = useState(null);
  const naviagte = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    const decoded = decodeToken(token);
    if(decoded && decoded.roles[0].authority === 'CUSTOMER'){
      setUserId(decoded.sub);
    }
  }, []);

  const onSignOut = () => {
    signOut();
    setUserId(null);
    naviagte("/", {replace: true});
  }

  return (
    <div className="header-container">
      {userId && (
        <Link to="/">
          <div className="header-wrap">
            {userId}
          </div>
        </Link>
      )}
      {!userId && (
        <Link to="/auth">
          <div className="header-wrap">
            로그인
          </div>
        </Link>
      )}
      <Link to="/">
        <div className="header-wrap">
          마이페이지
        </div>
      </Link>
      <Link to="/favorites">
        <div className="header-wrap">
          <span className="header-like">좋아요</span>
        </div>
      </Link>
      <Link to="/cart">
        <div className="header-wrap">
          장바구니
        </div>
      </Link>
      <Link to="/">
        <div className="header-wrap">
            주문조회
        </div>
      </Link>
      {userId && (
        <div className="header-wrap" onClick={onSignOut}>
          <span>로그아웃</span>
        </div>
      )}
    </div>
  );
}

export default Header;
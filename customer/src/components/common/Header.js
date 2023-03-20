import "../../assets/scss/header.scss";
import {useEffect, useState} from "react";
import {decodeToken} from "react-jwt";
import {Link} from "react-router-dom";
import {signOut} from "../../lib/api/auth";

const Header = () => {
  const [userId, setUserId] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    const decoded = decodeToken(token);
    if(decoded){
      setUserId(decoded.sub);
    }
  }, []);

  const onSignOut = () => {
    signOut();
    window.location.reload();
  }

  return (
    <div className="header-container">
      <div className="header-wrap">
        {userId && (
          userId
        )}
        {!userId && (
          <Link to="/auth">
            로그인
          </Link>
        )}
      </div>
      <div className="header-wrap">
        마이페이지
      </div>
      <div className="header-wrap">
        <span className="header-like">좋아요</span>
      </div>
      <div className="header-wrap">
        장바구니
      </div>
      <div className="header-wrap">
        주문조회
      </div>
        {userId && (
          <div className="header-wrap" onClick={onSignOut}>
            <span>로그아웃</span>
          </div>
        )}
    </div>
  );
}

export default Header;
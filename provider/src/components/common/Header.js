import "../../assets/scss/header.scss";
import {Nav} from "react-bootstrap";
import {BiClipboard, BiGridAlt, BiHomeAlt, BiPurchaseTagAlt} from "react-icons/bi";
import {Link, useLocation, useNavigate} from "react-router-dom";
import {signOut} from "../../lib/api/auth";

const navigation = [
  {
    title: "주문 관리",
    href: "/orders",
    icon: <BiClipboard className="icon"/>
  },
  {
    title: "상품 관리",
    href: "/items",
    icon: <BiGridAlt className="icon"/>
  },
  {
    title: "쿠폰 관리",
    href: "/coupons",
    icon: <BiPurchaseTagAlt className="icon"/>
  },
  {
    title: "상점 관리",
    href: "/info",
    icon: <BiHomeAlt className="icon"/>
  },
]

const Header = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const onSignOut = () => {
    signOut();
    navigate("/auth");
  }

  return (
    <Nav id="header">
      <div className="nav-item-wrapper">
        {navigation.map((nav, index) => (
          <Nav.Item key={index}>
            <Link to={nav.href} className={
              location.pathname === nav.href
                ? "active"
                : ""
            }>
              {nav.icon}
              <div className="header-title">{nav.title}</div>
            </Link>
          </Nav.Item>
        ))}
      </div>
      {/* 우측 */}
      <div className="nav-info" onClick={onSignOut}>
        로그아웃
      </div>
    </Nav>
  )
}

export default Header;
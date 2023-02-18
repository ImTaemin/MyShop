import "../../scss/Header.scss";
import {Nav, NavbarBrand} from "react-bootstrap";
import {BiClipboard, BiGridAlt, BiPurchaseTagAlt, BiHomeAlt} from "react-icons/bi";
import {useLocation, Link} from "react-router-dom";

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
      <div className="nav-info">
        넣을 거 있으면 넣기
      </div>
    </Nav>
  )
}

export default Header;
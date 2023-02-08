import "../scss/Header.scss";
import {Nav} from "react-bootstrap";
import {BiClipboard, BiGridAlt, BiPurchaseTagAlt, BiHomeAlt} from "react-icons/bi";
import {useLocation} from "react-router";

const navigation = [
  {
    title: "주문 정보",
    href: "/orders",
    icon: <BiClipboard className="icon"/>
  },
  {
    title: "상품 정보",
    href: "/items",
    icon: <BiGridAlt className="icon"/>
  },
  {
    title: "쿠폰 정보",
    href: "/coupons",
    icon: <BiPurchaseTagAlt className="icon"/>
  },
  {
    title: "상점 정보",
    href: "/info",
    icon: <BiHomeAlt className="icon"/>
  },
]

const Header = () => {

  const loaction = useLocation();

  return (
    <Nav id="header">
      <div className="nav-item-wrapper">
        {navigation.map(nav => (
          <Nav.Item className={
            loaction.pathname === nav.href
              ? "active"
              : "non-active"
          }>
            <Nav.Link href={nav.href} className="nav-link">
              <div className="nav-item">
                {nav.icon}
                <div className="header-title">{nav.title}</div>
              </div>
            </Nav.Link>
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
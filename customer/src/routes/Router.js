import MainPage from "../pages/MainPage";
import {Link, Navigate} from "react-router-dom";
import {lazy} from "react";
import {decodeToken} from "react-jwt";
import AuthPage from "../pages/AuthPage";
import ItemListPage from "../pages/ItemListPage";
import ItemInfoPage from "../pages/ItemInfoPage";
import OrderFormPage from "../pages/OrderFormPage";
import OrderedPage from "../pages/OrderedPage";
import FavoritePage from "../pages/FavoritePage";

// TODO: 지연로딩을 사용할 경우 스타일 시트를 불러오지 못해 일단 주석 처리.
// const AuthPage = lazy(() => import("../pages/AuthPage"));
// const ItemListPage = lazy(() => import("../pages/ItemListPage"));
// const ItemInfoPage = lazy(() => import("../pages/ItemInfoPage"));
// const OrderFormPage = lazy(() => import("../pages/OrderFormPage"));

// 액세스 토큰이 이미 있는 상태면 구매자인지 확인 후 이동
const AuthRoute = ({element, ...rest}) => {
  const accessToken = localStorage.getItem("accessToken");
  if(accessToken) {
    const decoded = decodeToken(accessToken);
    const role = decoded.roles[0].authority;
    if(role === 'CUSTOMER'){
      return <Navigate to="/" />;
    } else {
      localStorage.removeItem("accessToken");
    }
  }

  return element;
}

const PrivateRoute = ({ element, ...rest }) => {
  const accessToken = localStorage.getItem('accessToken');

  if (!accessToken) {
    return <Navigate to="/auth" />;
  }
  return element;
};


const ThemeRoutes = [
  {
    path: "/auth", element: <AuthRoute element={<AuthPage />} />
  },
  {
    path: "/",
    element: <MainPage />,
    children: [
      {path: "/", element: <Navigate to="/category/TOP" />},
      {path: "/category/:type", element: <ItemListPage />},
      {path: "/item/:itemId", element: <ItemInfoPage />},
      {path: "/order/order-form", element: <PrivateRoute element={<OrderFormPage />} />},
      {path: "/order", element: <PrivateRoute element={<OrderedPage />} />},
      {path: "/favorites", element: <PrivateRoute element={<FavoritePage />} />},
    ]
  },
  {
    path: "*",
    element: <>NOT FOUND<Link to="/">홈으로</Link></>,
  }
];

export default ThemeRoutes;
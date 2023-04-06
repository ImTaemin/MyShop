import {lazy} from "react";
import {Link, Navigate} from "react-router-dom";

// 페이지
const MainPage = lazy(() => import("../pages/MainPage.js"));
const OrderPage = lazy(() => import("../pages/OrderPage.js"));
const ItemsPage =  lazy(() => import("../pages/ItemsPage.js"));
const CouponPage =  lazy(() => import("../pages/CouponPage.js"));
const InfoPage =  lazy(() => import("../pages/InfoPage.js"));
const AuthPage =  lazy(() => import("../pages/AuthPage"));

const PrivateRoute = ({ element, ...rest }) => {
  const providerAccessToken = localStorage.getItem('providerAccessToken');

  if (!providerAccessToken) {
    return <Navigate to="/auth" />;
  }
  return element;
};

const AuthRoute = ({element, ...rest}) => {
  const providerAccessToken = localStorage.getItem("providerAccessToken");

  if (providerAccessToken) {
    return <Navigate to="/orders" />;
  }

  return element;
}

// Routes
const ThemeRoutes = [
  {
    path: "/auth", element: <AuthRoute element={<AuthPage />} />,
  },
  {
    path: "/",
    element: <MainPage/>,
    children: [
      {path: "/", element: <Navigate to="/auth"/>},
      {path: "/orders", element: <PrivateRoute element={<OrderPage />} />},
      {path: "/items", element: <PrivateRoute element={<ItemsPage />} />},
      {path: "/coupons", element: <PrivateRoute element={<CouponPage />} />},
      {path: "/info", element: <PrivateRoute element={<InfoPage />} />},
    ]
  },
  {
    path: "*",
    element: <>NOT FOUND<Link to="/orders">홈으로</Link></>,
  }
];

export default ThemeRoutes;
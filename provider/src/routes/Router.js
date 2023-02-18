import {lazy} from "react";
import {Navigate} from "react-router-dom";

// 페이지
const MainPage = lazy(() => import("../pages/MainPage.js"));
const OrderPage = lazy(() => import("../pages/OrderPage.js"));
const ItemPage =  lazy(() => import("../pages/ItemPage.js"));
const CouponPage =  lazy(() => import("../pages/CouponPage.js"));
const InfoPage =  lazy(() => import("../pages/InfoPage.js"));

// const {user} = useSelector(({user}) => ({user: user.user}));

// Routes
const ThemeRoutes = [
  {
    path: "/",
    element: <MainPage/>,
    children: [
      {path: "/", element: <Navigate to="/orders"/>},
      {path: "/orders", element: <OrderPage />},
      {path: "/items", element: <ItemPage />},
      {path: "/coupons", element: <CouponPage />},
      {path: "/info", element: <InfoPage />},
    ]
  }
];

export default ThemeRoutes;
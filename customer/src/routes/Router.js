import MainPage from "../pages/MainPage";
import {Link, Navigate} from "react-router-dom";
import {lazy} from "react";
import ItemListPage from "../pages/ItemListPage";
import ItemInfoPage from "../pages/ItemInfoPage";

const AuthPage = lazy(() => import("../pages/AuthPage"));

const AuthRoute = ({element, ...rest}) => {
  const accessToken = localStorage.getItem("accessToken");

  if (accessToken) {
    return <Navigate to="/" />;
  }

  return element;
}

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
      {path: "/item/:itemId", element: <ItemInfoPage />}
    ]
  },
  {
    path: "*",
    element: <>NOT FOUND<Link to="/">홈으로</Link></>,
  }
];

export default ThemeRoutes;
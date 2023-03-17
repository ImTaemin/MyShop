import Header from "../components/common/Header";
import {Helmet} from "react-helmet-async";
import React from "react";
import Category from "../components/common/Category";
import {Outlet} from "react-router-dom";
import "../assets/scss/main.scss";

const MainPage = () => {

  return (
    <>
      <Helmet>
        <title>마이샵</title>
      </Helmet>
      <main className="main">
        <Header />
        <div className="main-wrap">
          <Category />
          <div className="item-container">
            <Outlet />
          </div>
        </div>
      </main>
    </>
  );
}

export default MainPage;
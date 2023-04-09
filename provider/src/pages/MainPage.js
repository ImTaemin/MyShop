import React from "react";
import {Outlet} from "react-router-dom";
import Header from "../components/common/Header";
import '../assets/scss/main.scss';
import "../assets/scss/table.scss";

const MainPage = () => {

  return (
    <main style={{minWidth: "762px"}}>
      <div className="main-container">
        <Header/>
        <div className="content-container">
          <div className="content">
            <Outlet />
          </div>
        </div>
      </div>
    </main>
  )
}

export default MainPage;
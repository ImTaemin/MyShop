import Header from "../components/common/Header";
import {Helmet} from "react-helmet-async";
import React from "react";

const MainPage = () => {

  return (
    <>
      <Helmet>
        <title>마이샵</title>
      </Helmet>
      <main style={{minWidth: "762px"}}>
        <Header/>
      </main>
    </>
  );
}

export default MainPage;
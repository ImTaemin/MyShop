import React from "react";
import {Outlet} from "react-router";
import Header from "../components/Header";
import styled from 'styled-components';
import '../scss/Main.scss';
import {Container} from "react-bootstrap";

const MainPage = () => {
  return (
    <main>
      <div className="main-container">
        <Header className="header"/>
        <Container className="content-container">
          <div className="content">
            <Outlet />
          </div>
        </Container>
      </div>
    </main>
  )
}

export default MainPage;
import "./App.css";
import React, { useEffect } from "react";
import axios from "axios";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import NotFound from "./NotFound";

function App() {
  useEffect(() => {
    getRes();
  }, []);

  async function getRes() {
    await axios
      .get(process.env.REACT_APP_API_MYSHOP + "/api/hello")
      .then((response) => {
        console.log(response.data);
      })
      .catch((error) => {
        console.log(error);
      });
  }

  return (
    <div className="App">
      <Router>
        <Routes>
          <Route path="/hello" element={<>hihi</>} />
          <Route path="/*" element={<NotFound />} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;

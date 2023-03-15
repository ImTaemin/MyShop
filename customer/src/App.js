import {useRoutes} from "react-router-dom";
import ThemeRoutes from "./routes/Router";
import 'bootstrap/dist/css/bootstrap.css';
import React from "react";

function App() {
  const routing = useRoutes(ThemeRoutes);

  return (
    <div className="App">
      {routing}
    </div>
  );
}

export default App;

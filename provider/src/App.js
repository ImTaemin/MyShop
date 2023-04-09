import {useRoutes} from "react-router-dom";
import ThemeRoutes from './routes/Router'
import 'bootstrap/dist/css/bootstrap.css';
import React from "react";

const App = () => {
  const routing = useRoutes(ThemeRoutes);

  return (
    <div>
      {routing}
    </div>
  );
}

export default App;

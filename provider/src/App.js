import {useRoutes} from "react-router-dom";
import Themeroutes from './routes/Router'
import 'bootstrap/dist/css/bootstrap.css';

const App = () => {
  const routing = useRoutes(Themeroutes);

  return (
    <div>
      {routing}
    </div>
  );
}

export default App;

import {useRoutes} from "react-router";
import Themeroutes from './routes/Router'
import {Helmet} from "react-helmet-async";

const App = () => {
  const routing = useRoutes(Themeroutes);

  return (
    <>
      <Helmet>
        <title>메인 페이지</title>
      </Helmet>
      {routing}
    </>
  );
}

export default App;

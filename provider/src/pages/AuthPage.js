import React, {useState} from "react";
import "../scss/auth.scss";
import SignIn from "../components/auth/SignIn";
import SignUp from "../components/auth/SignUp";

const AuthPage = () => {
  const [authMode, setAuthMode] = useState(false);

  const changeAuthMode = () => {
    setAuthMode(!authMode);
  };

  return (
    <div className="auth-container" style={{}}>
      {authMode
        ? <SignUp changeAuthMode={changeAuthMode} />
        : <SignIn changeAuthMode={changeAuthMode} />}
    </div>
  );
}

export default AuthPage;
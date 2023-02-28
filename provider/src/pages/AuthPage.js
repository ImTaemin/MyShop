import React, {useState} from "react";
import "../scss/auth.scss";
import SignIn from "../components/auth/SignIn";
import SignUp from "../components/auth/SignUp";

const AuthPage = () => {
  const [authMode, setAuthMode] = useState(false);
  const [isRegistered, setIsRegistered] = useState(false);

  const changeAuthMode = () => {
    setAuthMode(!authMode);
  };

  return (
    <div className="auth-container">
      {authMode
        ? <SignUp changeAuthMode={changeAuthMode} setIsRegistered={setIsRegistered} />
        : <SignIn changeAuthMode={changeAuthMode} isRegistered={isRegistered} setIsRegistered={setIsRegistered} />}
    </div>
  );
}

export default AuthPage;
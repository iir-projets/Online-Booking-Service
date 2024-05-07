import plant from "../assets/plant.jpg";
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import { useRef } from "react";
import LoginSucces from "./LoginSucces";
import classes from "./Login.module.css";
import LoginFail from "./LoginFail";

function Login() {
  const errorRef = useRef(null);

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const [error, setError] = useState("");
  const navigateTo = useNavigate();

  const [isLoginFailVisible, setIsLoginFailVisible] = useState(false);
  const [isLoginSuccessVisible, setIsLoginSuccessVisible] = useState(false);
  const loginFailRef = useRef(null);
  const loginSuccessRef = useRef(null);

  const switchOn = (ref) => {
    if (ref === loginFailRef) {
      setIsLoginFailVisible(true);
    } else if (ref === loginSuccessRef) {
      setIsLoginSuccessVisible(true);
    }
  };

  const switchOff = () => {
    setIsLoginFailVisible(false);
    setIsLoginSuccessVisible(false);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch("http://localhost:9085/authenticate", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, password }),
      });

      if (!response.ok) {
        throw new Error("Invalid credentials");
      }

      const data = await response.json();
      localStorage.setItem("token", data.token);

      console.log(data);
      if (data.response == "503") {
        setError("Invalid Credentials");
        errorRef.current.classList.remove("hidden");
        errorRef.current.classList.add("block");
        switchOn(loginFailRef);
        const waitToTurnOff = setTimeout(() => switchOff(loginFailRef), 2000); //2s
      }
      if (data.role == "user") {
        localStorage.setItem("role","user")
        console.log("succes");
        switchOn(loginSuccessRef);
        const waitToSwitch = setTimeout(() => navigateTo("/home"), 3500); //3.5s
      }
      if (data.role == "admin") {
        localStorage.setItem("role","admin")
        console.log("succes");
        switchOn(loginSuccessRef);
        const waitToSwitch = setTimeout(() => navigateTo("/Admin"), 3500); //3.5s
      }
    } catch (error) {
      setError(error.message);
    }
  };

  return (
    <div>
      <div
        className={`${classes.backdrop} ${
          isLoginFailVisible ? "visible" : "hidden"
        }`}
        ref={loginFailRef}
      >
        <div className="ml-96 flex justify-start">
          <LoginFail />
        </div>
      </div>

      <div
        className={`${classes.backdrop} ${
          isLoginSuccessVisible ? "visible" : "hidden"
        }`}
        ref={loginSuccessRef}
      >
        <div className="w-2/6 ml-96">
          <LoginSucces />
        </div>
      </div>

      <div className="bg-slate-800 h-screen flex justify-center items-center">
        <div className="flex w-1/2 h-4/5 ring-2 justify-between p-4">
          <div className="w-full flex items-center p-4 m-2">
            <form className="max-w-sm mx-auto w-full" onSubmit={handleSubmit}>
              <div className="mb-5">
                <label
                  htmlFor="email"
                  className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                >
                  Your email
                </label>
                <input
                  type="email"
                  id="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                  placeholder="a43@email.com"
                  required
                />
              </div>
              <div className="mb-5">
                <label
                  htmlFor="password"
                  className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                >
                  Your password
                </label>
                <input
                  type="password"
                  id="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                  required
                />
              </div>
              <div className="flex items-center justify-center w-full">
                <p
                  ref={errorRef}
                  id="error"
                  className="text-white font-bold rounded-xl bg-red-500 w-full text-center p-2 mb-4 border-white border-2 hidden "
                >
                  {error}
                </p>
              </div>

              <button
                type="submit"
                className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"
              >
                Submit
              </button>
              <p className="font-serif text-xl mt-5 text-white">
                you want to create account ?{" "}
                <span className="text-blue-400 hover:text-pink-400 animate-pulse hover:transition duration-1000">
                  <Link to="/createaccount">Sign in </Link>
                </span>
              </p>
            </form>
          </div>
          <img src={plant} className="" alt="" />
        </div>
      </div>
    </div>
  );
}

export default Login;

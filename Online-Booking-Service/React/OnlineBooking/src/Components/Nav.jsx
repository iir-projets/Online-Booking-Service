import Logo from "../assets/A43.png";
import { useNavigate } from "react-router-dom";
import { RiLogoutCircleRLine } from "react-icons/ri";
import { useEffect, useRef, useState } from "react";
import classes from "./Nav.module.css";
import SessionExpired from "./SessionExpired";
import Switch from "react-switch";

function Nav() {
  const navigateTo = useNavigate();
  const [isSessionExpired, SetisSessionExpired] = useState("");
  const SessionAnimation = useRef(false);
  const buttonsRef = useRef(null);

  const [isDarkMode, setIsDarkMode] = useState(false);

  const SwitchTheme = () => {
    const newMode = !isDarkMode; // Calculate the new mode before updating state
    setIsDarkMode(newMode); // Update the state
    // Update dark mode styles on the document based on the new mode
    if (newMode) {
      document.documentElement.classList.add("dark");
    } else {
      document.documentElement.classList.remove("dark");
    }
  };

  useEffect(() => {
    const role = localStorage.getItem("role");
    if (role === "admin") {
      buttonsRef.current.style.display = "none";
    } else {
      buttonsRef.current.style.display = "flex";

    }
  }, []);

  const setLightMode = () => {
    document.querySelector("body").setAttribute("data-theme", "light");
    //console.log("set in light mode")
  };
  setLightMode();

  const switchOn = (ref) => {
    SetisSessionExpired(true);
  };

  useEffect(() => {
    const checkTokenValidity = async (token) => {
      try {
        const response = await fetch(
          `http://localhost:9085/checkToken?token=${token}`,
          {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
            },
          }
        );

        if (!response.ok) {
          throw new Error("Network response was not ok");
        }

        const data = await response.json();
        console.log("Token validity response:", data);

        return data; // This will be a boolean value
      } catch (error) {
        console.error("There was a problem checking token validity:", error);
        return false;
      }
    };

    const token = localStorage.getItem("token");
    if (token) {
      checkTokenValidity(token).then((valid) => {
        console.log(valid);
        if (valid) {
          // Token is valid, navigate to home
          //navigate("/home");
        } else {
          // Token is not valid, remove it and navigate to login
          const switchtologin = setTimeout(() => 
          navigateTo("/Login"), 4000); //3.5s
          switchOn(SessionAnimation);
          console.log(SessionAnimation);
          localStorage.removeItem("token");
        }
      });
    } else {
      // Token not found in localStorage, navigate to login
      //navigateTo("/Login");
    }
  }, []);

  const handleLogout = () => {
    // Remove token from local storage
    localStorage.removeItem("token");
    // Update isLoggedIn state to false
    navigateTo("/Login");
    // Log "test" to console
    console.log("test");
  };
  const switchProducts = () => {
    navigateTo("/pageable");
    // Log "test" to console
    console.log("test");
  };
  const switchtoContact = () => {

    navigateTo("/history");

    // Log "Contact us" to console
    console.log("");
  };
  return (
    <>
      <div
        className={`${classes.backdrop} ${
          isSessionExpired ? "visible" : "hidden"
        }`}
        ref={SessionAnimation}
      >
        <div className="w-1/2">
          <SessionExpired />
        </div>
      </div>

      <div className="flex dark:bg-gray-900 dark:mt-0 dark:pt-6 justify-between w-full mt-4 ">
        <img src={Logo} alt="" className="ml-8 sm:w-1/4" />
        {/* buttons container */}
        <div className="flex gap-24 " ref={buttonsRef}>
          <button
            className="border-2 p-4 font-thin font-serif text-l rounded-2xl dark:hover:text-sky-500 dark:text-white  dark:hover:border-sky-500 dark:hover:bg-white dark:hover:text-2xl duration-700 shadow-xl hover:animate-bounce dark:bg-sky-500 border-sky-400 bg-white text-sky-400  hover:text-white hover:bg-sky-400 hover:border-white hover:text-2xl"
            onClick={() => {
              navigateTo("/home");
            }}
          >
            Home
          </button>
          <button
            className="border-2 p-4 font-thin font-serif text-l rounded-2xl dark:hover:text-sky-500 dark:text-white  dark:hover:border-sky-500 dark:hover:bg-white dark:hover:text-2xl duration-700 shadow-xl hover:animate-bounce dark:bg-sky-500 border-sky-400 bg-white text-sky-400  hover:text-white hover:bg-sky-400 hover:border-white hover:text-2xl "
            onClick={switchProducts}
          >
            Services
          </button>
          <button
            className="border-2 p-4 font-thin font-serif text-l rounded-2xl dark:hover:text-sky-500 dark:text-white  dark:hover:border dark:text--sky-500 dark:hover:bg-white dark:hover:text-2xl duration-700 shadow-xl hover:animate-bounce dark:bg-sky-500 border-sky-400 bg-white text-sky-400  hover:text-white hover:bg-sky-400 hover:border-white hover:text-2xl"
            onClick={switchtoContact}
          >
            Booking History
          </button>
        </div>
        {/*   Dark/Light Mode Toogle    */}

        {/*<div className="flex justify-center items-center">
          <Switch onChange={SwitchTheme} checked={isDarkMode}  /> 
          </div>*/}

        {/*     Logout button     */}
        <button
          className="border-2 p-4 mr-4 font-thin font-serif text-xl bg-red-400 rounded-lg text-white
         hover:bg-white hover:text-red-400 duration-700 hover:border-red-400 shadow-2xl hover:translate-y-6 flex gap-2 dark:bg-purple-500 dark:hover:bg-white dark:hover:border-purple-500 dark:hover:text-purple-500"
          onClick={handleLogout}
        >
          Log out
          <RiLogoutCircleRLine className="mt-1" />
        </button>
      </div>
    </>
  );
}

export default Nav;

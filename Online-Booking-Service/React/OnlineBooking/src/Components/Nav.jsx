import Logo from "../assets/A43.png";
import { useNavigate } from "react-router-dom";
import { RiLogoutCircleRLine } from "react-icons/ri";
import { useEffect, useRef, useState } from "react";
import classes from "./Nav.module.css"
import SessionExpired from "./SessionExpired";

function Nav() {
  const navigateTo = useNavigate();
  const [isSessionExpired, SetisSessionExpired] = useState("");
  const SessionAnimation = useRef(false);

  const switchOn = (ref) => {
    SetisSessionExpired(true)
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
          const switchtologin = setTimeout(() => navigateTo("/Login"), 4000); //3.5s
          switchOn(SessionAnimation)
          console.log(SessionAnimation)
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
    navigateTo("/services");
    // Log "test" to console
    console.log("test");
  };
  const switchtoContact = () => {
    //navigateTo("/Login")
    // Log "Contact us" to console
    console.log("Contact us");
  };
  return (
    <>
          <div
        className={`${classes.backdrop} ${
           isSessionExpired? "visible" : "hidden"
        }`}
        ref={SessionAnimation}
      >
        <div className="w-1/2">
          <SessionExpired />
        </div>
      </div>
    
      <div className="flex justify-between w-full mt-4 ">
        <img src={Logo} alt="" className="ml-8 sm:w-1/4" />
        {/* buttons container */}
        <div className="flex gap-24 ">
          <button
            className="border-2 p-4 font-thin font-serif text-l rounded-2xl hover: hover:border-blue-600 hover:text-blue-600 duration-700 shadow-xl hover:animate-bounce"
            onClick={() => {
              navigateTo("/home");
            }}
          >
            Home
          </button>
          <button
            className="border-2 p-4 font-thin font-serif text-l rounded-2xl hover: hover:border-blue-600  hover:text-blue-600 duration-700 shadow-xl hover:animate-bounce"
            onClick={switchProducts}
          >
            Services
          </button>
          <button className="border-2 p-4 font-thin font-serif text-l rounded-2xl hover: hover:border-blue-600  hover:text-blue-600 duration-700 shadow-xl hover:animate-bounce">
            Contact Us
          </button>
        </div>
        {/*     Login button     */}
        <button
          className="border-2 p-4 mr-4 font-thin font-serif text-xl bg-red-400 rounded-lg text-white
         hover:bg-white hover:text-red-400 duration-700 hover:border-red-400 shadow-2xl hover:translate-y-6 flex gap-2"
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

import Logo from "../assets/A43.png";
import { useNavigate } from "react-router-dom";

function Nav() { 
  const navigateTo = useNavigate()

  const handleLogout = () => {
    // Remove token from local storage
    localStorage.removeItem("token");
    // Update isLoggedIn state to false
    navigateTo("/Login")
    // Log "test" to console
    console.log("test");
  };
  const switchProducts = () => {
    navigateTo("/services")
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
      <div className="flex justify-between w-full mt-4 ">
        <img src={Logo} alt="" className="ml-8 sm:w-1/4" />
        {/* buttons container */}
        <div className="flex gap-24 ">
          <button className="border-2 p-4 font-thin font-serif text-l rounded-2xl hover: hover:border-blue-600 hover:text-blue-600 duration-700 shadow-xl hover:animate-bounce"
          onClick={() => {
            navigateTo("/home")
          }}>
            Home
          </button>
          <button className="border-2 p-4 font-thin font-serif text-l rounded-2xl hover: hover:border-blue-600  hover:text-blue-600 duration-700 shadow-xl hover:animate-bounce"
          onClick={switchProducts}>
            Services
          </button>
          <button className="border-2 p-4 font-thin font-serif text-l rounded-2xl hover: hover:border-blue-600  hover:text-blue-600 duration-700 shadow-xl hover:animate-bounce">
            Contact Us
          </button>
        </div>
        {/*     Login button     */}
        <button
          className="border-2 p-4 mr-4 font-thin font-serif text-xl bg-red-400 rounded-lg text-white
         hover:bg-white hover:text-red-400 duration-700 hover:border-red-400 shadow-2xl hover:translate-y-6"
          onClick={handleLogout}
        >
          Log out
        </button>
      </div>
    </>
  );
}

export default Nav;

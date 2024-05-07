import { useEffect } from "react";
import { useState, useRef } from "react";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import { BsCircleFill } from "react-icons/bs";

function SignIn() {
  // State variables for form inputs
  const [username, setUsername] = useState("");
  const [CB, setCB] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [telephone, setTelephone] = useState("");
  const [Error, setError] = useState("");
  const [isDivVisible, setIsDivVisible] = useState(false);

  const navigateTo = useNavigate();

  const [isUserNameUnique, setisUserNameUnique] = useState();
  const [isPhoneUnique, setisPhoneUnique] = useState();
  const [isMailUnique, setisMailUnique] = useState();
  const [isCBUnique, setisisCBUnique] = useState();
  const [showPassword, setShowPassword] = useState(false);

  //Ref
  const userNameRef = useRef(null);
  const phoneRef = useRef(null);
  const mailRef = useRef(null);
  const cbRef = useRef(null);
  const divRef = useRef(null);

  const updateStyles = () => {
    userNameRef.current.style.color = isUserNameUnique ? "green" : "red";
    phoneRef.current.style.color = isPhoneUnique ? "green" : "red";
    mailRef.current.style.color = isMailUnique ? "green" : "red";
    cbRef.current.style.color = isCBUnique ? "green" : "red";
  };

  const toggleVisibility = () => {
    setIsDivVisible(!isDivVisible);
    if (divRef.current) {
      divRef.current.style.display = isDivVisible ? "none" : "flex";
    }
  };

  useEffect(() => {
    updateStyles();
  }, [isCBUnique, isMailUnique, isPhoneUnique, isUserNameUnique]);

  // Function to handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();

    // Prepare form data
    const formData = {
      userName: username,
      email: email,
      password: password,
      phone: telephone,
      carteBancaire: CB,
    };
    console.log(formData);

    // Make POST request to API
    try {
      const response = await fetch("http://localhost:9085/add", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });
      const data = await response.json();
      console.log(data);
      if (data.response == "503") {
        toggleVisibility()
        setisMailUnique(data.isMailUnique);
        setisUserNameUnique(data.isUserNameUnique);
        setisisCBUnique(data.isCBUnique);
        setisPhoneUnique(data.isPhoneUnique);
      } else {
        setError("");
      }
      if (data.response == "200") {
        localStorage.setItem("token", data.token);
        navigateTo("/home");
        localStorage.setItem("token",data.token)
        localStorage.setItem("role","user")
      }
      // Handle success or display any feedback to the user
    } catch (error) {
      console.error("Error:", error);
      // Handle error or display error message to the user
    }
  };
  return (
    <>
      <div className="flex justify-evenly w-full bg-slate-800">
        <div className="flex flex-col items-center justify-center w-full h-screen ">
          <form className="w-7/12 ring-2 p-8" onSubmit={handleSubmit}>
            {/* Input fields */}
            {/* Username */}
            <div className="relative z-0 w-full mb-5 group">
              <input
                type="text"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                className="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none dark:text-white dark:border-gray-600 dark:focus:border-blue-500 focus:outline-none focus:ring-0 focus:border-blue-600 peer"
                placeholder="Username"
                required
              />
            </div>
            {/* Email */}
            <div className="relative z-0 w-full mb-5 group">
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none dark:text-white dark:border-gray-600 dark:focus:border-blue-500 focus:outline-none focus:ring-0 focus:border-blue-600 peer"
                placeholder="Email address"
                required
              />
            </div>
            {/* Password */}
            <div className="relative z-0 w-full mb-5 group">
              <input
                type={showPassword ? 'text' : 'password'}
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none dark:text-white dark:border-gray-600 dark:focus:border-blue-500 focus:outline-none focus:ring-0 focus:border-blue-600 peer"
                placeholder="Password"
                required
              />
            </div>
            {/* Carte Bancaire */}
            <div className="relative z-0 w-full mb-5 group">
              <input
                type="text"
                value={CB}
                onChange={(e) => setCB(e.target.value)}
                className="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none dark:text-white dark:border-gray-600 dark:focus:border-blue-500 focus:outline-none focus:ring-0 focus:border-blue-600 peer"
                placeholder="Carte Bancaire 💳"
                required
              />
            </div>
            {/* Telephone */}
            <div className="relative z-0 w-full mb-5 group">
              <input
                type="tel"
                value={telephone}
                onChange={(e) => setTelephone(e.target.value)}
                className="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none dark:text-white dark:border-gray-600 dark:focus:border-blue-500 focus:outline-none focus:ring-0 focus:border-blue-600 peer"
                placeholder="Phone number (+212 6 11 22 33 44)"
                required
              />
            </div>
            {/*     Switch to Log in    */}
            <p className="font-serif text-xl mt-5 mb-5 text-white">
              you already have an account{" "}
              <span className="text-blue-400 hover:text-pink-400 animate-pulse hover:transition duration-1000">
                <Link to="/Login">Log In</Link>
              </span>
            </p>
            <div className="flex gap-5 mb-5">
              <input
                type="checkbox"
                checked={showPassword}
                onChange={() => setShowPassword(!showPassword)}
                className=""
              />
              Show Password
            </div>

            {/* Submit button */}
            <button
              type="submit"
              className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-red-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"
            >
              Submit
            </button>
            {Error && (
              <div className="text-red-500 mt-5 animate-pulse duration-1000">
                {Error}
              </div>
            )}
          </form>
        </div>
        {/*     Testing what should be Unique   */}
        <div
          className="flex w-3/4 flex-col items-center justify-center w- h-screen bg-slate-800 font-mono font-bold gap-5 text-3xl hidden"
          ref={divRef}
        >
          <p className="flex justify-between gap-5" ref={userNameRef}>
            <BsCircleFill />
            UserName Unique
          </p>
          <p className="flex justify-between gap-5" ref={phoneRef}>
            {" "}
            <BsCircleFill /> Phone Unique
          </p>
          <p className="flex justify-between gap-5" ref={mailRef}>
            {" "}
            <BsCircleFill /> Email Unique
          </p>
          <p className="flex justify-between gap-5" ref={cbRef}>
            {" "}
            <BsCircleFill /> Carte Bancaire Unique
          </p>
        </div>
        {/*     Regix Testing */}
        {/*<button onClick={toggleVisibility}>Toggle Visibility</button>*/}
      </div>
    </>
  );
}

export default SignIn;

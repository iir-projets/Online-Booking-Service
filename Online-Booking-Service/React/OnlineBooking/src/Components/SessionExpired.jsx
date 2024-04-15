import Lottie from "lottie-react";
import Session from "../assets/Session.json"

const SessionExpired = () => {
    return <div className="">
        <h1 className="text-center font-mono text-3xl mt-10 text-red-500 bg-white rounded-2xl">Sorry your Session has expired⌛</h1>
        <Lottie className="flex justify-center" animationData={Session} />
    </div>
  };
  
  export default SessionExpired;
import Lottie from "lottie-react";
import LoadingAnimation from "../assets/SuccesCart.json";

const Succes = () => {
  return (
    <>
      <div className="flex justify-center">
        <Lottie className="w-5/12" animationData={LoadingAnimation} />;
      </div>
    </>
  );
};

export default Succes;

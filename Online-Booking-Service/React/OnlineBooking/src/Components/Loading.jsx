import Lottie from "lottie-react";
import LoadingAnimation from "../assets/Loading.json";

const Loading = () => {
  return <>
  <div className="flex justify-center">
    <Lottie animationData={LoadingAnimation} />;
  </div>
</>;
};



export default Loading;

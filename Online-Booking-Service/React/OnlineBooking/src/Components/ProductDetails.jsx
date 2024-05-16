import classes from "./ProductDetails.module.css";
import PropTypes from "prop-types"; // Import PropTypes
import { useState, useEffect, useMemo, useRef } from "react";
import bike from "../assets/bike-service.png";
import delivery from "../assets/delivery-service.png";
import HomeDelivery from "../assets/home-delivery-service.png";
import OnlineShopping from "../assets/online-shopping.png";
import TaxiDriver from "../assets/taxi-driver.png";
import receptionist from "../assets/receptionist-working-on-her-desk-with-laptop.png";
import support from "../assets/support.png";
import { CiCircleCheck } from "react-icons/ci";
import "../index.css";
import Succes from "./Succes";
import Loading from "./Loading";
import axios from "axios";
import { Toaster, toast } from "react-hot-toast";
import Comment from "./Comment";

function ProductDetails(props) {

  //Ref
  const LoadingRef = useRef(null);
  const SuccesRef = useRef(null);
  // States
  const [isCommetVisible, setIsCommentVisible] = useState(false);
  const [comments, setComments] = useState([]);
  const [data, setData] = useState(props.service);

  const [image, Setimage] = useState(support);
  const [isLoadingVisible, setisLoadingVisible] = useState(false);
  const [isSuccesVisible, setisSuccesVisible] = useState(false);



  const switchOn = (ref) => {
    if (ref === SuccesRef) {
      setisLoadingVisible(true);
    } else if (ref === LoadingRef) {
      setisSuccesVisible(true);
    }
  };

  const switchOff = () => {
    setisLoadingVisible(false);
    setisSuccesVisible(false);
  };

  //here i am creating a map to link each image to a product
  // Use useMemo to memoize the 'gallery' object initialization
  const gallery = useMemo(
    () =>
      new Map([
        [1, support],
        [2, bike],
        [3, HomeDelivery],
        [4, delivery],
        [5, OnlineShopping],
        [6, TaxiDriver],
        [7, receptionist],
        [9, TaxiDriver],
      ]),
    []
  );

  useEffect(() => {
    // Update image whenever props.id changes
    Setimage((`data:image/jpeg;base64,${props.service.image}`))

  }, [data.id, gallery]);

  // Submit Reservatin handling :
  function makeReservation(event) {
    event.preventDefault(); // Prevent default form submission
    const token = localStorage.getItem("token");
    console.log(token);
    const requestBody = {
      productName: data.name,
      price: data.price,
      token: token,
    };

    fetch("http://localhost:9085/reservation/PDF", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(requestBody),
    })
      .then((response) => {
        if (response.ok) {
          // Check if response is OK (status code 200)
          return response.blob(); // Return the response as a blob (PDF file)
        }
        throw new Error("Network response was not ok.");
      })
      .then((blob) => {
        // Create a URL for the blob (PDF file)
        const url = URL.createObjectURL(blob);
        // Open the PDF in a new tab/window
        window.open(url);
        // Optional: Handle success or display a message
        toast.success("Successfully Booked!");
        console.log("Reservation made successfully");
      })
      .catch((error) => {
        console.error("Error:", error);
        // Handle errors
      });
  }

  const toggleCommentVisibility = () => {
    
    setIsCommentVisible(!isCommetVisible);
  };

  const GetComments = async () => {
    toggleCommentVisibility()
    try {
      const token = localStorage.getItem("token"); // Replace with your actual token value
      const id = props.service.id; // Replace with the actual ID value
      const response = await axios.post(
        `http://localhost:9085/services/comments?token=${token}&id=${id}`
      );
      setComments(response.data.data); // Assuming the response is an array of comments
      console.log(response.data.data)
    } catch (error) {
      console.error("Error fetching comments:", error);
    }
  };

  return (
    <div>
      <Toaster position="bottom-left" reverseOrder={false} />
      <div
        className={`backdrop ${isLoadingVisible ? "visible" : "hidden"}`}
        ref={LoadingRef}
      >
        <Succes className="" />
        <div className="ml-96">{/*<LoginFail />*/}</div>
      </div>

      <div
        className={`backdrop ${isSuccesVisible ? "visible" : "hidden"}`}
        ref={SuccesRef}
      >
        <Loading className="w-1/2" />
        <div className="w-2/6 ml-96">{/*<LoginSucces /> */}</div>
      </div>
      <div className={`${classes.container} dark:bg-slate-800`}>
        <div className="dark:bg-slate-800 flex w-full bg-white h-full ">
          <img
            src={image}
            className="w-1/3 h-1/2 mt-8 ml-8 shadow-sm ring-1"
            alt=""
          />
          <div className="text-container mt-5">
            <h1 className=" p-4">{data.name} </h1>
            <h1 className=" p-4">Rating : {"⭐".repeat(data.rating)}</h1>
            <h1 className=" p-4">Location : {data.location}</h1>
            <h1 className=" p-4">Category : {data.category}</h1>
            <h1 className=" p-4">Price : {data.price}</h1>
            <h1 className=" p-4">Description : {data.description}</h1>
            <div className="flex">
              {" "}
              <button
                onClick={makeReservation}
                className="flex justify-between gap-2 border-2 p-2 m-3 bg-red-400 text-white font-mono font-bold duration-1000 rounded-2xl hover:text-red-400 hover:bg-white hover:border-red-400 hover:translate-x-4"
              >
                Submit Reservation <CiCircleCheck className="mt-1" />
              </button>
              <button
                onClick={GetComments}
                className="flex justify-between gap-2 border-2 p-2 m-3 bg-red-400 text-white font-mono font-bold duration-1000 rounded-2xl hover:text-red-400 hover:bg-white hover:border-red-400 hover:translate-x-4"
              >
                See Others Reviews <CiCircleCheck className="mt-1" />
              </button>
            </div>
            {isCommetVisible && comments.map((comment,index)=>(
              <Comment key={index}
              comment={comment.comment.comment}
              user={comment.user.username} />
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}

ProductDetails.propTypes = {
  onCancel: PropTypes.func.isRequired,
  service: PropTypes.object.isRequired,
};

export default ProductDetails;

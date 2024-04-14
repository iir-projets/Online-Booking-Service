import classes from "./ProductDetails.module.css";
import PropTypes from "prop-types"; // Import PropTypes
import { useState, useEffect, useMemo } from "react";
import bike from "../assets/bike-service.png";
import delivery from "../assets/delivery-service.png";
import HomeDelivery from "../assets/home-delivery-service.png";
import OnlineShopping from "../assets/online-shopping.png";
import TaxiDriver from "../assets/taxi-driver.png";
import receptionist from "../assets/receptionist-working-on-her-desk-with-laptop.png";
import support from "../assets/support.png";
import { CiCircleCheck } from "react-icons/ci";

function ProductDetails(props) {
  // States

  const [data, setData] = useState(props.service);
  console.log("second component", data);
  const [image, Setimage] = useState(support);

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
      ]),
    []
  );

  useEffect(() => {
    // Update image whenever props.id changes
    Setimage(gallery.get(data.id));
  }, [data.id, gallery]);

  // Submit Reservatin handling :
  function makeReservation(event) {
    event.preventDefault(); // Prevent default form submission
    const token = localStorage.getItem("token");
    console.log(token);
    const requestBody = {
      productName: data.name,
      token: token,
    };

    fetch("http://localhost:9085/reservation", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(requestBody),
    })
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
        if (data.response == 200) {
          console.log("reservation made successfully");
        }
        // Handle response data as needed
      })
      .catch((error) => {
        console.error("Error:", error);
        // Handle errors
      });
  }

  console.log("props ====> " + image + data.id);
  console.log(localStorage.getItem("token"));

  return (
    <div className={classes.container}>
      <div className="flex w-full bg-white h-full">
        <img
          src={image}
          className="w-1/3 h-1/2 mt-8 ml-8 shadow-sm ring-1"
          alt=""
        />
        <div className="text-container mt-5">
          <h1 className=" p-4">{data.name} </h1>
          <h1 className=" p-4">Rating : ⭐⭐⭐⭐⭐</h1>
          <h1 className=" p-4">Location : {data.location}</h1>
          <h1 className=" p-4">Category : {data.category}</h1>
          <h1 className=" p-4">Description : {data.description}</h1>
          <button onClick={makeReservation} className="flex justify-between gap-2 border-2 p-2 m-3 bg-red-400 text-white font-mono font-bold duration-1000 rounded-2xl hover:text-red-400 hover:bg-white hover:border-red-400 hover:translate-x-4">
            Submit Reservation <CiCircleCheck className="mt-1"/>
          </button>
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

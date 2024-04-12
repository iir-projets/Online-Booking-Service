import classes from "./ProductDetails.module.css";
import PropTypes from "prop-types"; // Import PropTypes
import { useState } from "react";
import img from "../assets/bike-service.png";

function ProductDetails(props) {
  // States

  // Handle Changes

  function SubmitHandler(event) {
    event.preventDefault();
    // Pass data to parent component
    props.onAddNote();
    props.onCancel();
  }

  return (
    <div className={classes.container}>
      <div className="flex w-full bg-white h-full">
        <img
          src={img}
          className="w-1/3 h-1/2 mt-8 ml-8 shadow-sm ring-1"
          alt=""
        />
        <div className="text-container mt-5">
          <h1 className=" p-4">Service Name </h1>
          <h1 className=" p-4">Rating : ⭐⭐⭐⭐⭐</h1>
          <h1 className=" p-4">Location : Rabat</h1>
          <h1 className=" p-4">Description :</h1>
        </div>
      </div>
    </div>
  );
}

ProductDetails.propTypes = {
  onCancel: PropTypes.func.isRequired,
  onAddNote: PropTypes.func.isRequired,
};

export default ProductDetails;

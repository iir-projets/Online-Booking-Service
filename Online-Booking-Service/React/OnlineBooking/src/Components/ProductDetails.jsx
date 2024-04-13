import classes from "./ProductDetails.module.css";
import PropTypes from "prop-types"; // Import PropTypes
import { useState } from "react";
import img from "../assets/bike-service.png";

function ProductDetails(props) {
  // States
  const [data,setData] = useState(props.service)
  console.log("second component" , data)
  // Handle Changes

  function SubmitHandler(event) {
    event.preventDefault();
    // Pass data to parent component
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
          <h1 className=" p-4">{data.name} </h1>
          <h1 className=" p-4">Rating : ⭐⭐⭐⭐⭐</h1>
          <h1 className=" p-4">Location : {data.location}</h1>
          <h1 className=" p-4">Category : {data.category}</h1>
          <h1 className=" p-4">Description : {data.description}</h1>
        </div>
      </div>
    </div>
  );
}

ProductDetails.propTypes = {
  onCancel: PropTypes.func.isRequired,
  service: PropTypes.object.isRequired
};

export default ProductDetails;

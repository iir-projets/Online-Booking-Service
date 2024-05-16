import support from "../assets/support.png";
import PropTypes from "prop-types";
import bike from "../assets/bike-service.png";
import delivery from "../assets/delivery-service.png";
import HomeDelivery from "../assets/home-delivery-service.png";
import OnlineShopping from "../assets/online-shopping.png";
import TaxiDriver from "../assets/taxi-driver.png";
import receptionist from "../assets/receptionist-working-on-her-desk-with-laptop.png";
import { useState, useEffect, useMemo } from "react";
import { TbListDetails } from "react-icons/tb";

function Container(props) {
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
        [9, TaxiDriver],
      ]),
    []
  );

  useEffect(() => {
    // Update image whenever props.id changes
      
      if(props.image!= null){
        //console.log(`data:image/jpeg;base64,${props.image}`)
        Setimage((`data:image/jpeg;base64,${props.image}`))
      }else{
        console.log("iam here")
        Setimage(gallery.get(props.id));
      }
    
    
  }, [props.id, gallery]);
  
 

  return (

    <div className="flex-col border-2 w-64 shadow-xl items-center justify-center rounded-3xl ">

      <div className="flex justify-center">
        <img src={image} alt="" className="w-44" />
      </div>
      <p className="text-center font-mono text-2xl">{props.title}</p>
      <p className="mt-2 font-mono text-l">Rating: {"⭐".repeat(props.rating)} </p>
      <div className="flex justify-center items-center w-full">
        <button
          className="flex justify-between gap-2 border-2 p-2 m-3 bg-red-400 text-white font-mono font-bold duration-1000 rounded-2xl hover:text-red-400 hover:bg-white hover:border-red-400 hover:translate-x-4"
          onClick={props.onDetails}
        >
          View Details <TbListDetails className="mt-1"/>
        </button>
      </div>
    </div>
  );
}

Container.propTypes = {
  title: PropTypes.string.isRequired,
  onDetails: PropTypes.func.isRequired,
  id: PropTypes.number.isRequired, // Assuming 'id' is a number
};

export default Container;

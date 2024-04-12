import img from "../assets/support.png";
import PropTypes from 'prop-types';


function Container(props) {
  return (
    <div className="flex-col border-2 w-56 h-72 shadow-xl items-center justify-center">
      <div className="flex justify-center"> 
        <img src={img} alt="" className="w-44" />
      </div>
      <p className="text-center font-mono text-2xl">{props.title}</p>
      <p className="mt-2 font-mono text-l">Rating: ⭐⭐⭐⭐⭐</p>
      <button className="ml-12 w-28 border-2 p-2 m-3 bg-red-400 text-white font-mono font-bold duration-1000 rounded-2xl hover:text-red-400 hover:bg-white hover:border-red-400 hover:translate-x-4" onClick={props.onDetails}>Book Now</button>
    </div>
  );
}


Container.propTypes = {
  title: PropTypes.string.isRequired,
  onDetails: PropTypes.func.isRequired,
};


export default Container;

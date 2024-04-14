import Container from "./Container";
import Nav from "./Nav";
import { useState } from "react";
import Footer from "./Footer";
import Modal from "./Modal";
import ProductDetails from "./ProductDetails";
import { useEffect } from "react";

function Products() {
  const [isOpen, setIsOpen] = useState(false);
  const [selectedOption, setSelectedOption] = useState("");
  const [visibility, setVisibility] = useState(false);
  const [services, setServices] = useState([]);
  const [details , setDetails] = useState();
  const options = ["Category 1", "Category 2", "Category 3"];

  useEffect(() => {
    console.log(details); // This will log the updated state value
  }, [details]); // useEffect will run whenever details state changes

  const toggleDropdown = () => {
    setIsOpen(!isOpen);
  };

  const handleOptionSelect = (option) => {
    setSelectedOption(option);
    setIsOpen(false);
  };

  const switchVisibility = (visible) => {
    setVisibility(visible);
  };

  const onDetails = (services) => {
    setDetails(services)
    switchVisibility(true);
  };
  

  useEffect(() => {
    fetchDataFromApi();
  }, []);

  const fetchDataFromApi = async () => {
    try {
      // Get the token from localStorage
      const token = localStorage.getItem("token");
      console.log(token);
      // Check if token exists
      if (!token) {
        throw new Error("Token not found in localStorage");
      }

      const response = await fetch("http://localhost:9085/services", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          // Include the token in the Authorization header
          Authorization: `Bearer ${token}`,
        },
        body: token,
      });

      if (!response.ok) {
        throw new Error("Network response was not ok");
      }

      const ApiResponse = await response.json();
      setServices(ApiResponse.data);

      console.log("Data from API:", ApiResponse);
      return ApiResponse;
    } catch (error) {
      console.error("There was a problem fetching data from the API:", error);
      return null;
    }
  };

  return (
    <>
      <Nav />
      <div className="flex mt-24 ">
        {visibility && (
          <Modal onClose={() => switchVisibility(false)}>
            <ProductDetails service={details} onCancel={() => switchVisibility(false)} />
          </Modal>
        )}
        <div className="flex-col ml-10 ">
          <label htmlFor="" className="font-bold font-mono">
            Search :
          </label>
          <br />
          <input type="text" className="border-2  rounded-xl" />
          {/* Filter DropDown */}
          <div className="dropdown border-2 mt-5 p-2 text-center  rounded-2xl ">
            <div
              className="dropdown-toggle mb-3 font-mono font-bold"
              onClick={toggleDropdown}
            >
              {selectedOption || "Select an option"}
            </div>
            {isOpen && (
              <ul className="dropdown-menu mb-2  ">
                {options.map((option) => (
                  <li
                    className="text-sky-300 p-1 mb-3 hover:bg-sky-300 hover:font-bold hover:text-white duration-1000 ring-2 "
                    key={option}
                    onClick={() => handleOptionSelect(option)}
                  >
                    {option}
                  </li>
                ))}
              </ul>
            )}
          </div>
          {/* Price Range Slider */}
          <div className=" justify-center">
            <label
              htmlFor="default-range"
              className="block mt-4 mb-2 text-sm font-medium text-gray-900 "
            >
              Price Range :
            </label>
            <div>
              <input
                id="price-range"
                type="range"
                className="w-full h-2 bg-gray-200 rounded-lg appearance-none cursor-pointer dark:bg-gray-700"
                name="slide"
              />
            </div>
          </div>
        </div>
        {/* Product Container   */}
        <div className=" mr-10 ml-auto p-5 border-2 w-3/5 flex flex-wrap gap-10 justify-between">
          {services.map((services) => (
            <Container
              key={services.id}
              id={services.id}
              title={services.name}
              onDetails={() => {
                onDetails(services);
              }}
            />
          ))}
        </div>
      </div>
      <Footer />
    </>
  );
}

export default Products;

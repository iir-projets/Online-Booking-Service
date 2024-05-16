import Container from "./Container";
import Nav from "./Nav";
import { useState } from "react";
import Footer from "./Footer";
import Modal from "./Modal";
import ProductDetails from "./ProductDetails";
import { useEffect, useRef } from "react";

function ProductsPageable() {
  const [isOpen, setIsOpen] = useState(false);
  const [selectedOption, setSelectedOption] = useState("");
  const [visibility, setVisibility] = useState(false);
  const [services, setServices] = useState([]);
  const [details, setDetails] = useState();
  const [price, setPrice] = useState(0);
  const [Page, setPage] = useState(1);
  const [Operation, setOperation] = useState("");
  const [isLast, setIsLast] = useState();
  const [isFirst, setIsFirst] = useState();

  //Ref States
  const prevButtonRef = useRef(null);
  const nextButtonRef = useRef(null);

  const options = ["Category 1", "Category 2", "Category 3"];

  // Handle Pagination
  useEffect(() => {
    console.log(Operation);
    switch (Operation) {
      case "Price":
        FilterByPrice(price);
        break;
      case "Category":
        FilterCategory(selectedOption);
        break;
      default:
        fetchDataFromApi();
        break;
    }
  }, [Page, Operation]);

  const Handle_Pagination_Navigation = () => {
    console.log("Page" + Page + "\nIsLast " + isLast + "\nisFirst " + isFirst)
    // Disable Next button and enable Previous button
    if (nextButtonRef.current && isLast) {
      nextButtonRef.current.disabled = true;
    } else if (nextButtonRef.current && !isLast) {
      nextButtonRef.current.disabled = false;
    }
    if (prevButtonRef.current && isFirst) {
      prevButtonRef.current.disabled = false;
    } else if (prevButtonRef.current && !isFirst) {
      prevButtonRef.current.disabled = false;
    }
  };

  useEffect(() => {
    console.log(details); // This will log the updated state value
  }, [details]); // useEffect will run whenever details state changes

  const handlePriceChange = (event) => {
    setOperation("Price");
    const newPrice = parseInt(event.target.value, 10); // Parse the value as an integer
    setPrice(newPrice); // Set the new price value
    FilterByPrice(newPrice);
  };

  const toggleDropdown = () => {
    setIsOpen(!isOpen);
  };

  const handleOptionSelect = (option) => {
    setOperation("Category");
    setSelectedOption(option);
    setIsOpen(false);
    FilterCategory(option);
  };

  const switchVisibility = (visible) => {
    setVisibility(visible);
  };

  const onDetails = (services) => {
    setDetails(services);
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

      const response = await fetch(
        `http://localhost:9085/service/page?page=${Page}&token=${token}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (!response.ok) {
        throw new Error("Network response was not ok");
      }

      const ApiResponse = await response.json();
      setServices(ApiResponse.data.response);
      setIsLast(ApiResponse.data.isLast);
      setIsFirst(ApiResponse.data.isFirst);
      //console.log("Data from API:", ApiResponse);
      Handle_Pagination_Navigation();
      return ApiResponse;
    } catch (error) {
      console.error("There was a problem fetching data from the API:", error);
      return null;
    }
  };
  const FilterCategory = async (option) => {
    setPage(1);
    if (selectedOption === "") {
      console.log("Default option");
    }
    try {
      const queryParams = new URLSearchParams({
        token: localStorage.getItem("token"),
        category: option,
        page: Page,
      });

      const url = `http://localhost:9085/services/category?${queryParams}`;

      const response = await fetch(url, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
      });

      if (!response.ok) {
        throw new Error("Network response was not ok");
      }

      const ApiResponse = await response.json();
      setServices(ApiResponse.data);
      setIsLast(ApiResponse.data.isLast);
      setIsFirst(ApiResponse.data.isFirst);
      //console.log("Data from API:", ApiResponse);
      Handle_Pagination_Navigation();

      console.log("Data from API:", ApiResponse);
      return ApiResponse;
    } catch (error) {
      console.error("There was a problem fetching data from the API:", error);
      return null;
    }
  };
  const FilterByPrice = async (maxprice) => {
    setPage(1);
    if (selectedOption === "") {
      console.log("Default option");
    }
    try {
      const queryParams = new URLSearchParams({
        token: localStorage.getItem("token"),
        price: maxprice,
        page: Page,
      });

      const url = `http://localhost:9085/services/price?${queryParams}`;

      const response = await fetch(url, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
      });

      if (!response.ok) {
        throw new Error("Network response was not ok");
      }

      const ApiResponse = await response.json();
      setServices(ApiResponse.data);
      setIsLast(ApiResponse.data.isLast);
      setIsFirst(ApiResponse.data.isFirst);
      console.log("Data from API:", ApiResponse);
      Handle_Pagination_Navigation();

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
      {/*   Show Products    */}
      <div className="flex mt-24 ">
        {visibility && (
          <Modal onClose={() => switchVisibility(false)}>
            <ProductDetails
              service={details}
              image={services.image}
              onCancel={() => switchVisibility(false)}
            />
          </Modal>
        )}
        <div className="flex-col ml-10 w-56 flex ">
          <button
            className="border-2 rounded-2xl p-3 hover:bg-slate-300 hover:text-slate-800 duration-1000 hover:font-bold"
            onClick={() => {
              setOperation("default");
            }}
          >
            Reset
          </button>
          {/* Filter Category DropDown */}
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
            <div className="flex justify-between gap-5 items-center">
              <input
                id="price-range"
                type="range"
                className="w-full h-2 bg-gray-200 rounded-lg appearance-none cursor-pointer dark:bg-gray-700"
                name="slide"
                onChange={handlePriceChange}
                value={price}
                max={500}
              />
              <span className="text-center">{price}</span>
            </div>
          </div>
        </div>
        {/* Product Container   */}
        <div className=" mr-10 ml-auto p-6 border-2 w-3/5 flex flex-wrap gap-10 rounded-3xl ">
          {services.map((services) => (
            <Container
              key={services.id}
              rating ={services.rating}
              id={services.id}
              title={services.name}
              image={services.image}
              onDetails={() => {
                onDetails(services);
              }}
            />
          ))}
        </div>
      </div>
      {/*       Pagination      */}
      <div className="flex justify-end mt-10 mr-16">
        <div className="flex">
          {/* Previous Button */}
          <button
            href="#"
            className="flex items-center justify-center px-4 h-10 me-3 text-base font-medium text-gray-500 bg-white border border-gray-300 rounded-lg hover:bg-gray-100 hover:text-gray-700 dark:bg-gray-800 dark:border-gray-700 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white"
            onClick={() => {
              if (Page != 1) {
                setPage((prevPage) => prevPage - 1);
              }
            }}
            ref={prevButtonRef}
          >
            <svg
              className="w-3.5 h-3.5 me-2 rtl:rotate-180"
              aria-hidden="true"
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 14 10"
            >
              <path
                stroke="currentColor"
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth="2"
                d="M13 5H1m0 0 4 4M1 5l4-4"
              />
            </svg>
            Previous
          </button>

          {/* Next Button */}
          <button
            href="#"
            ref={nextButtonRef}
            onClick={() => {
              if (Page != 3) {
                setPage((prevPage) => prevPage + 1);
              }
            }}
            className="flex items-center justify-center px-4 h-10 text-base font-medium text-gray-500 bg-white border border-gray-300 rounded-lg hover:bg-gray-100 hover:text-gray-700 dark:bg-gray-800 dark:border-gray-700 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white"
          >
            Next
            <svg
              className="w-3.5 h-3.5 ms-2 rtl:rotate-180"
              aria-hidden="true"
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 14 10"
            >
              <path
                stroke="currentColor"
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth="2"
                d="M1 5h12m0 0L9 1m4 4L9 9"
              />
            </svg>
          </button>
        </div>
      </div>
      {/*       Footer      */}
      <Footer />
    </>
  );
}

export default ProductsPageable;

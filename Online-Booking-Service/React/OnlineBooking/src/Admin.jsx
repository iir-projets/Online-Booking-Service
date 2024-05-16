import { useEffect, useState } from "react";
import Nav from "./Components/Nav";
import DisplayProducts from "./Components/DisplayProducts";
import { IoAddCircleOutline } from "react-icons/io5";
import Modal from "./Components/Modal";
import ProductDetails from "./Components/ProductDetails";
import ProductForm from "./Components/ProductForm";
import { useNavigate } from "react-router-dom";

function Admin() {
  const [services, setServices] = useState([]);
  const [visibility, setVisibility] = useState(false);
  const [type, setType] = useState("");
  const [productName, setProductName] = useState("");
  const [productCategory, setProductCategory] = useState("");
  const [productLocation, setProductLocation] = useState("");
  const [productPrice, setProductPrice] = useState("");
  const [productDescription, setProductDescription] = useState("");

  const navigateTo = useNavigate();

  useEffect(() => {
    fetchDataFromApi();
  }, []);
  console.log("services =", services);

  const fetchDataFromApi = async () => {
    try {
      // Get the token from localStorage
      const token = localStorage.getItem("token");
      console.log("my token", token);
      // Check if token exists
      if (!token) {
        throw new Error("Token not found in localStorage");
      }

      const response = await fetch(
        `http://localhost:9085/services?token=${token}`,
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
      setServices(ApiResponse.data);

      console.log("Data from API:", ApiResponse);
      return ApiResponse;
    } catch (error) {
      console.error("There was a problem fetching data from the API:", error);
      return null;
    }
  };
  const addProductHandle = async (noteData) => {
    const token = localStorage.getItem("token"); // Assuming token is stored locally after login
    const requestOptions = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(noteData), // Send noteData as the request body
    };

    try {
      const response = await fetch(
        `http://localhost:9090/demandes/add?token=${token}`, // Include the token as a URL query parameter
        requestOptions
      );

      if (!response.ok) {
        throw new Error("Network response was not ok");
      }

      const data = await response.json();
      console.log("Note added successfully:", data);
      fetchDataFromApi();
    } catch (error) {
      console.error("There was a problem adding the note:", error);
    }
  };
  const editProductHandle = async (noteData) => {
    const token = localStorage.getItem("token"); // Assuming token is stored locally after login
    const requestOptions = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(noteData), // Send noteData as the request body
    };

    try {
      const response = await fetch(
        `http://localhost:9090/demandes/edit?token=${token}`, // Include the token as a URL query parameter
        requestOptions
      );

      if (!response.ok) {
        throw new Error("Network response was not ok");
      }

      const data = await response.json();
      console.log("Note added successfully:", data);
      fetchDataFromApi();
    } catch (error) {
      console.error("There was a problem adding the note:", error);
    }
  };
  const switchVisibilityOn = (type) => {
    console.log(type);
    setType(type);
    setVisibility(true);
  };

  const switchVisibilityOff = () => {
    setVisibility(false);
  };

  const handleButton = (name, category, location, price, desc, type) => {
    // Perform edit action, e.g., show modal with details pre-filled
    switchVisibilityOn(type);
    console.log(
      "Editing product:",
      name,
      category,
      location,
      price,
      desc,
      type
    );
    setProductCategory(category);
    setProductDescription(desc);
    setProductLocation(location);
    setProductName(name);
    setProductPrice(price);
  };

  return (
    <>
      <Nav />
      {/*   Popup JSX Code    */}
      {visibility && (
        <Modal onClose={switchVisibilityOff}>
          <ProductForm
            type={type}
            name={productName}
            desc={productDescription}
            location={productLocation}
            price={productPrice}
            category={productCategory}
          />
        </Modal>
      )}
      <div className="mt-16 flex justify-end mr-6 ">
        <button
          className="p-4 hover:bg-blue-500 duration-1000 hover:text-slate-900 hover:text-2xl font-mono font-bold ring-2 rounded-2xl "
          onClick={() => {
            navigateTo("/Admin/history");
          }}
        >
          See history
        </button>
      </div>

      {/*   Admin HeroSection   */}
      <div className="mt-32 p-4 ml-60 flex justify-between mr-60">
        <h1 className="font-bold font-serif text-3xl ">
          Welcome Admin to Dashboard{" "}
        </h1>
        <button
          type="submit"
          className="text-white bg-blue-700 hover:bg-white focus:ring-4  focus:ring-blue-300 rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center hover:text-blue-700 duration-1000 font-mono font-bold hover:border-blue-700 border-2 hover:animate-pulse flex gap-2"
          onClick={() => switchVisibilityOn("add")}
        >
          Add new Product
          <IoAddCircleOutline className="mt-0.5" />
        </button>
      </div>
      <div className="flex justify-center h-screen">
        <div className=" w-4/6 relative overflow-x-auto shadow-md sm:rounded-lg">
          <table className="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
            <thead className="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
              <tr>
                <th scope="col" className="px-6 py-3">
                  Product name
                </th>
                <th scope="col" className="px-6 py-3">
                  Category
                </th>
                <th scope="col" className="px-6 py-3">
                  Location
                </th>
                <th scope="col" className="px-6 py-3">
                  Price
                </th>
                <th scope="col" className="px-6 py-3 flex justify-center">
                  Action
                </th>
              </tr>
            </thead>
            <tbody>
              <DisplayProducts
                data={services}
                handleEdit={handleButton}
                type={type}
              />
              {/* Add more table rows as needed */}
            </tbody>
          </table>
        </div>
      </div>
    </>
  );
}

export default Admin;

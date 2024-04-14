import { useEffect, useState } from "react";
import Nav from "./Components/Nav";
import DisplayProducts from "./Components/DisplayProducts";
import { IoAddCircleOutline } from "react-icons/io5";



function Admin() {
  const [services, setServices] = useState([]);

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
      <div className="mt-32 p-4 ml-60 flex justify-between mr-60">
        <h1 className="font-bold font-serif text-3xl ">
          Welcome Admin to Dashboard{" "}
        </h1>
        <button
          type="submit"
          className="text-white bg-blue-700 hover:bg-white focus:ring-4  focus:ring-blue-300 rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center hover:text-blue-700 duration-1000 font-mono font-bold hover:border-blue-700 border-2 hover:animate-pulse flex gap-2"
        >
          Add new Product
          <IoAddCircleOutline className="mt-0.5" />
        </button>
      </div>
      <div className="flex justify-center items-center mt-10">
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
              <DisplayProducts data={services} />
              {/* Add more table rows as needed */}
            </tbody>
          </table>
        </div>
      </div>
    </>
  );
}

export default Admin;

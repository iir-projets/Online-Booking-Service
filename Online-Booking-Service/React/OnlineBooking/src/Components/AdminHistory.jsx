import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

import Nav from "./Nav";

function AdminHistory() {
  //States
  const [Data, setData] = useState([]);
  const [Price, setPrice] = useState(0);
  const [Reservations, setReservations] = useState(0);
  const [Feedback, setFeedback] = useState(0);

  useEffect(() => {
    const fetchBookingHistory = async () => {
      try {
        const response = await axios.get(
          "http://localhost:9085/Adminhistory",
          {}
        );
        if (response.data.response === 200) {
          console.log(response.data.data);
          setData(response.data.data);
          setPrice(response.data.TotalPrice);
          setFeedback(response.data.feedback)
          setReservations(response.data.reservations)
        } else {
          console.error("Error fetching booking history:", response.data);
        }
      } catch (error) {
        console.error("Error fetching booking history:", error);
      }
    };

    fetchBookingHistory();
  }, []);

  function formatDate(dateString) {
    const date = new Date(dateString);
    const day = date.getDate();
    const month = date.getMonth() + 1; // Months are zero-based, so add 1
    const year = date.getFullYear();

    // Pad single-digit day and month with leading zeros if needed
    const formattedDay = String(day).padStart(2, "0");
    const formattedMonth = String(month).padStart(2, "0");

    // Format the date as DD/MM/YYYY
    const formattedDate = `${formattedDay}/${formattedMonth}/${year}`;

    return formattedDate;
  }

  const navigateTo = useNavigate();
  return (
    <>
      <Nav />
      <div className="h-svh mt-10">
        {" "}
        <div className=" flex justify-between mr-6 ">
            <div className="flex flex-col gap-4 justify-center font-mono text-2xl ml-8 p-2">
                <h1 className="">Totale CA made until now is : {Price}</h1>
                <h1 className="">Totale Reservatins made are : {Reservations}</h1>
                <h1 className="">Totale feedback we got is : {Feedback}</h1>
            </div>
          <button
            className="p-4 h-20 hover:bg-blue-500 duration-1000 hover:text-slate-900 hover:text-2xl font-mono font-bold ring-2 rounded-2xl "
            onClick={() => {
              navigateTo("/Admin");
            }}
          >
            Back to Products
          </button>
        </div>
        <div className="p-4   flex justify-center items-center mt-20">
          <div className="flex overflow-y-scroll w-full justify-center h-64">
            <table className="text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
              <thead className="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
                <tr>
                  <th scope="col" className="px-6 py-3">
                    Product Name
                  </th>
                  <th scope="col" className="px-6 py-3">
                    Product Price
                  </th>
                  <th scope="col" className="px-6 py-3">
                    Date
                  </th>
                </tr>
              </thead>
              <tbody>
                {Data.map((product, index) => (
                  <tr
                    key={index}
                    className={`${
                      index % 2 === 0 ? "even:bg-gray-50" : "odd:bg-white"
                    } ${
                      index % 2 !== 0
                        ? "even:dark:bg-gray-800"
                        : "odd:dark:bg-gray-900"
                    } border-b dark:border-gray-700`}
                  >
                    <td
                      scope="row"
                      className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white"
                    >
                      {product.product.name}
                    </td>
                    <td className="px-6 py-4">{product.product.price}</td>
                    <td className="px-6 py-4">
                      {formatDate(product.reservationDate)}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </>
  );
}

export default AdminHistory;

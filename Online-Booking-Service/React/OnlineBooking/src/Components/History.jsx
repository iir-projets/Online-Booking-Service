import Nav from "./Nav";
import { useEffect, useState } from "react";
import axios from "axios";

function History() {
  console.log(localStorage.getItem("token"));

  const [Data, setData] = useState([]);
  const [DataHistory, setDataHistory] = useState([]);

  useEffect(() => {
    const fetchBookingHistory = async () => {
      try {
        const response = await axios.get("http://localhost:9085/history", {
          params: {
            token: localStorage.getItem("token"), // Replace 'your_token_here' with the actual token
          },
        });
        if (response.data.response === 200) {
          setData(response.data.data);
        } else {
          console.error("Error fetching booking history:", response.data);
        }
      } catch (error) {
        console.error("Error fetching booking history:", error);
      }
    };

    fetchBookingHistory();
  }, []);

  useEffect(() => {
    const newDataHistory = [];
    Data.forEach((element) => {
      console.log(element.id);
      newDataHistory.push(element.id);
    });
    setDataHistory(newDataHistory);
  }, [Data]);

  return (
    <>
      <Nav />
      <div className="p-4 mt-24  flex justify-center items-center h-1/4 overflow-x-auto">
        <table className="w-1/2 text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
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
                <td className="px-6 py-4">{product.reservationDate}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </>
  );
}

export default History;

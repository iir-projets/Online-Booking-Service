import Nav from "./Nav";
import { useEffect, useState } from "react";
import Modal from "./Modal";
import axios from "axios";
import Rating from "./Rating";

function History() {
  const [Data, setData] = useState([]);
  const [ReservationToRate, setReservationToRate] = useState();
  const [DataHistory, setDataHistory] = useState([]);
  const [visibility, setVisibility] = useState(false);

  useEffect(() => {
    const fetchBookingHistory = async () => {
      try {
        const response = await axios.get("http://localhost:9085/history", {
          params: {
            token: localStorage.getItem("token"), // Replace 'your_token_here' with the actual token
          },
        });
        if (response.data.response === 200) {
          console.log(response.data.data);
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
      //console.log(element.id);
      newDataHistory.push(element.id);
    });
    setDataHistory(newDataHistory);
  }, [Data]);

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

  const switchVisibilityOff = () => {
    setVisibility(false);
  };
  const switchVisibilityOn = () => {
    setVisibility(true);
  };

  const handleButton = (data) => {
    // Perform edit action, e.g., show modal with details pre-filled
    setReservationToRate(data);
    switchVisibilityOn();
  };

  return (
    <>
      <Nav />
      {visibility && (
        <Modal onClose={switchVisibilityOff}>
          <Rating data={ReservationToRate} />
        </Modal>
      )}

      <div className="p-4 flex justify-center items-center overflow-x-auto h-screen">
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
              <th scope="col" className="px-6 py-3">
                Action
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
                <td className="px-6 py-4">
                  <button
                    className={`text-blue-500 font-bold ${
                      product.comment !== null
                        ? "text-gray-400 cursor-not-allowed"
                        : ""
                    }`}
                    onClick={() => handleButton(product)}
                    disabled={product.comment !== null}
                  >
                    Add Rating
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </>
  );
}

export default History;

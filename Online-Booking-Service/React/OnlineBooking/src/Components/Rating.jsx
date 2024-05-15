import { useEffect, useState } from "react";
import classes from "./Rating.module.css";

function Rating(props) {
  const [rating, setRating] = useState(1); // State to store the selected rating
  const [Commentinput, setComment] = useState(""); // State to store the textarea value
  // Function to handle the change event of the dropdown

  useEffect(() => {
    console.log(rating);
  }, [rating]);

  useEffect(() => {
    console.log(Commentinput);
  }, [Commentinput]);

  const handleSubmit = async () => {
    const token = localStorage.getItem("token");
    const requestBody = {
      reservation: props.data, // Assuming props.data contains the reservation object
      Rating: rating,
      Commentinput: Commentinput,
    };
    console.log("requestBody",requestBody)
    try {
      const response = await fetch(`http://localhost:9085/services/addRating?token=${token}&comment=${encodeURIComponent(Commentinput)}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(requestBody),
      });

      if (response.ok) {
        const responseData = await response.json(); // Parse response data as JSON
        console.log("Response data:", responseData); // Log the response data
        // Handle successful response
        console.log("Rating added successfully!");
      } else {
        // Handle error response
        console.error("Failed to add rating:", response.statusText);
      }
    } catch (error) {
      // Handle network errors
      console.error("Error while adding rating:", error);
    }
  };

  const handleRatingChange = (event) => {
    setRating(parseInt(event.target.value)); // Update the rating state with the selected value
  };


  const convertToStars = (num) => {
    return "⭐".repeat(num);
  };

      // Function to handle textarea change event
      const handleTextareaChange = (event) => {
        setComment(event.target.value); // Update the textarea value state
    };
  return (
    <div
      className={`${classes.container} bg-slate-400 dark:bg-slate-900 p-10 `}
    >
      <div className="flex w-full justify-center gap-10 h-1/6">
        <div className="flex justify-center items-center ">
          <h1>Add your Rating : </h1>
        </div>
        <select
          className="w-1/2 h-full rounded-2xl "
          value={rating}
          onChange={handleRatingChange}
        >
          {[1, 2, 3, 4, 5].map((option) => (
            <option className="" key={option} value={option}>
              {convertToStars(option)}
            </option>
          ))}
        </select>
      </div>
      <div className="flex justify-center gap-10 mt-10">
        <h1 className="flex justify-center">Add Comment (optionel)</h1>
        <textarea 
                className="w-1/2 rounded-3xl" 
                name="textarea" 
                id="textarea" 
                value={Commentinput} 
                onChange={handleTextareaChange}
            ></textarea>
      </div>

      <div className="flex justify-end mt-10 ">
      <button className="bg-white p-4 text-black rounded-3xl hover:bg-black hover:text-white duration-1000 dark:bg-purple-500 border-2 dark:text-white dark:hover:bg-white dark:hover:text-purple-500 dark:hover:border-purple-500  border-white"
      onClick={handleSubmit}>Submit Review</button>
      </div>
    </div>
  );
}

export default Rating;

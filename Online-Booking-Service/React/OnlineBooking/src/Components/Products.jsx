import Container from "./Container";
import Nav from "./Nav";
import { useState } from "react";
import Footer from "./Footer";

function Products() {
  const [isOpen, setIsOpen] = useState(false);
  const [selectedOption, setSelectedOption] = useState("");

  const options = ["Category 1", "Category 2", "Category 3"];

  const toggleDropdown = () => {
    setIsOpen(!isOpen);
  };

  const handleOptionSelect = (option) => {
    setSelectedOption(option);
    setIsOpen(false);
  };

  return (
    <>
      <Nav />
      <div className="flex mt-24 ">
        <div className="flex-col ml-10 ">
          <label htmlFor="" className="font-bold font-mono">
            Search :
          </label>
          <br />
          <input type="text" className="border-2" />
          {/*   Filter DropDown   */}
          <div className="dropdown border-2 mt-5 p-2 text-center ">
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
          {/*     Price Range Slider     */}
          <div>
            <label
              htmlFor="default-range"
              className="block mt-4 mb-2 text-sm font-medium text-gray-900 "
            >
              Price Range :
            </label>
            <input
              id="default-range"
              type="range"
              value="5"
              className="w-full h-2 bg-gray-200 rounded-lg appearance-none cursor-pointer dark:bg-gray-700"
            />
          </div>
        </div>
        <div className=" mr-10 ml-auto p-5 border-2 w-3/5 flex flex-wrap gap-10 justify-between">
          <Container className="" />
          <Container className="" />
          <Container className="" />
          <Container className="" />
          <Container className="" />
          <Container className="" />
        </div>
      </div>
      <Footer/>
    </>
  );
}

export default Products;

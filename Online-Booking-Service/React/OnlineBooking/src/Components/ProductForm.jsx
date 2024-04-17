import { useState, useEffect ,useRef } from "react";
import classes from "./ProductForm.module.css";

function ProductForm(props) {
  // States management:
  const [productName, setProductName] = useState("");
  const [productCategory, setProductCategory] = useState("");
  const [productLocation, setProductLocation] = useState("");
  const [productPrice, setProductPrice] = useState("");
  const [productDescription, setProductDescription] = useState("");
  //Ref declaration :
  const NameInput = useRef(null)
  const CategoryInput = useRef(null)
  const LocationInput = useRef(null)
  const PriceInput = useRef(null)
  const DescriptionInput = useRef(null)
  // Category List:
  const categories = ["Category 1", "Category 2", "Category 3", "Category 4"];
  // Custom height value
  const height = {
    height: "150px", // Set the height to 50px
  };
  //get token :
  const token = localStorage.getItem("token"); // Get the token from localStorage

  // Functions and Logic:
  const handleCategoryChange = (e) => {
    setProductCategory(e.target.value);
  };
  //Enable or disable input:
  useEffect(() => {
    // Assurez-vous que la référence est initialisée avant de l'utiliser
    if (NameInput.current) {
      if (props.type === 'add') {
        setProductName("");
        setProductCategory("");
        setProductLocation("");
        setProductPrice("");
        setProductDescription("");

      } 
      if (props.type === 'edit') {
        setProductName(props.name);
        setProductCategory(props.category);
        setProductLocation(props.location);
        setProductPrice(props.price);
        setProductDescription(props.desc);
        NameInput.current.disabled = true;
        NameInput.current.className = 'border-2 border-blue-600 rounded-xl p-2 " bg-slate-300';
      } 
      if(props.type === 'delete'){
        setProductName(props.name);
        setProductCategory(props.category);
        setProductLocation(props.location);
        setProductPrice(props.price);
        setProductDescription(props.desc);
        NameInput.current.disabled = true;
        CategoryInput.current.disabled = true;
        LocationInput.current.disabled = true;
        PriceInput.current.disabled = true;
        DescriptionInput.current.disabled = true;
        NameInput.current.className = 'border-2 bg-slate-300 border-blue-600 rounded-xl p-2  ';
        CategoryInput.current.className = 'border-2 border-blue-600 rounded-xl w-5/12 text-center bg-slate-300';
        LocationInput.current.className = 'border-2 bg-slate-300 border-blue-600 rounded-xl p-2  ';
        PriceInput.current.className = 'border-2 bg-slate-300 border-blue-600 rounded-xl p-2  ';
        DescriptionInput.current.className = 'border-2 bg-slate-300 border-blue-600 rounded-xl p-2  ';
      }
    }
  }, [props.type]);

  function SubmitHandler(){
    console.log(productCategory , productDescription , productLocation , productName , productPrice)
  }

  //API diffrent Calls :
  // Calling Create API 
  const handleAddProduct = async () => {
    // Ensure that productName, productCategory, productLocation,
    // productPrice, and productDescription are defined and accessible here
    const product = {
      name: productName,
      category: productCategory,
      location: productLocation,
      price: productPrice,
      description: productDescription,
      availability: "true", 
    };

  
    const requestOptions = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(product),
    };
    console.log("my product", product);
  
    try {
      const response = await fetch(`http://localhost:9085/services/add?token=${token}`, requestOptions);
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
  
      const data = await response.json();
      console.log("Product added successfully:", data);
      // Optionally, you can update state or perform any other action after adding the product
    } catch (error) {
      console.error("There was a problem adding the product:", error);
    }
  };
  // Calling Create API
  const handleDeleteProduct = async () => {
    // Ensure that productName, productCategory, productLocation,
    // productPrice, and productDescription are defined and accessible here
    const product = {
      name: productName,
      category: productCategory,
      location: productLocation,
      price: productPrice,
      description: productDescription,
      availability: "true",
    };

  
    const requestOptions = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(product),
    };
    console.log("my product", product);
  
    try {
      const response = await fetch(`http://localhost:9085/services/delete?token=${token}`, requestOptions);
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
  
      const data = await response.json();
      console.log("Product added successfully:", data);
      // Optionally, you can update state or perform any other action after adding the product
    } catch (error) {
      console.error("There was a problem adding the product:", error);
    }
  };
  // Calling Create API
  const handleEditProduct = async () => {
    // Ensure that productName, productCategory, productLocation,
    // productPrice, and productDescription are defined and accessible here
    const product = {
      name: productName,
      category: productCategory,
      location: productLocation,
      price: productPrice,
      description: productDescription,
      availability: "true",
    };

  
    const requestOptions = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(product),
    };
    console.log("my product", product);
  
    try {
      const response = await fetch(`http://localhost:9085/services/edit?token=${token}`, requestOptions);
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
  
      const data = await response.json();
      console.log("Product added successfully:", data);
      // Optionally, you can update state or perform any other action after adding the product
    } catch (error) {
      console.error("There was a problem adding the product:", error);
    }
  };


  
  //handling Submit 
  function handleSubmit(){
    switch (props.type) {
      case "add":
        console.log("methode api add")
        handleAddProduct();
        break;
      case "edit":
        console.log("methode api edit")
        handleEditProduct();
      break;
      case "delete":
        console.log("methode api delete")
        handleDeleteProduct();
      break;
      default:
        break;
    }
  }
  //debugging 
  console.log( "props debugg" , props)

  return (
    <>
      <div className={`${classes.container} flex justify-center items-center`}>
        <div className="p-8 grid grid-cols-2 gap-5 justify-center">
          {/* Div container for each label and input */}
          {/* Product Name */}
          <div className="flex justify-between">
            <label htmlFor="" className="font-mono font-bold text-2xl">
              Product Name:
            </label>
            <input
              type="text"
              className="border-2 border-blue-600 rounded-xl p-2 "
              ref={NameInput}
              value={productName}
              onChange={(e) => setProductName(e.target.value)}
            />
          </div>
          {/* Product Category */}
          <div className="flex justify-between">
            <label htmlFor="" className="font-mono font-bold text-2xl">
              Product Category:
            </label>
            <select
              className="border-2 border-blue-600 rounded-xl w-5/12 text-center"
              ref={CategoryInput}
              value={productCategory}
              onChange={handleCategoryChange}
            >
              {/* Render dropdown options from the categories array */}
              {categories.map((category, index) => (
                <option className="p-2 mt-2 mb-2" key={index} value={category}>
                  {category}
                </option>
              ))}
            </select>
          </div>
          {/* Product Location */}
          <div className="flex justify-between">
            <label htmlFor="" className="font-mono font-bold text-2xl">
              Product Location:
            </label>
            <input
              type="text"
              ref={LocationInput}
              className="border-2 border-blue-600 rounded-xl p-2"
              value={productLocation}
              onChange={(e) => setProductLocation(e.target.value)}
            />
          </div>
          {/* Product Price */}
          <div className="flex justify-between">
            <label htmlFor="" className="font-mono font-bold text-2xl">
              Product Price:
            </label>
            <input
              type="number"
              ref={PriceInput}
              className="border-2 border-blue-600 rounded-xl p-2"
              value={productPrice}
              onChange={(e) => setProductPrice(e.target.value)}
            />
          </div>
          {/* Product Description */}
          <label htmlFor="" className="font-mono font-bold text-2xl">
            Product Description:
          </label>
          <textarea
            className="border-2 border-blue-600 rounded-xl  p-4 text-top text-wrap"
            ref={DescriptionInput}
            style={{ ...height, whiteSpace: "wrap !important" }}
            value={productDescription}
            onChange={(e) => setProductDescription(e.target.value)}
          ></textarea>
          <p></p>
          <div className="flex justify-center">
            <button className="border-2 p-4 w-1/2 font-thin font-serif text-xl bg-red-400 rounded-2xl text-white justify-center hover:bg-white hover:text-red-400 duration-700 hover:border-red-400 shadow-2xl hover:translate-y-6 flex gap-2" 
            onClick={handleSubmit}>
              Submit
            </button>
          </div>
        </div>
      </div>
    </>
  );
}

export default ProductForm;

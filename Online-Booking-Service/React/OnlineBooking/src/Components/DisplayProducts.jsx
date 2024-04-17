import React, { useState, useEffect } from "react";

function DisplayProducts(props) {
  // Initialize state for products using props.data
  const [products, setProducts] = useState(props.data);



  //Logic and methodes

 /* useEffect(() => {
    setProducts(props.data); // Update products when props.data changes
  }, [props.data]);
  console.log(products);*/

  useEffect(() => {
    setProducts(props.data); // Update products when props.data changes
  }, [props.data, props.type]);

  
  

  //Debugging :
  //console.log("type of operation is  " ,props.type)

  return (
    <>
      {products.map((product, index) => (
        <tr
          key={index}
          className={`${index % 2 === 0 ? "even:bg-gray-50" : "odd:bg-white"} ${
            index % 2 !== 0 ? "even:dark:bg-gray-800" : "odd:dark:bg-gray-900"
          } border-b dark:border-gray-700`}
        >
          <td
            scope="row"
            className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white"
          >
            {product.name}
          </td>
          <td className="px-6 py-4">{product.category}</td>
          <td className="px-6 py-4">{product.location}</td>
          <td className="px-6 py-4">{product.price}</td>
          <td className="px-6 py-4">
            <div className="flex gap-4 justify-center">
              <button
                href="#"
                className="font-medium text-blue-600 dark:text-blue-500 hover:underline"
                onClick={() => props.handleEdit(product.name, product.category, product.location, product.price , product.description , "edit")}

              >
                Edit
              </button>
              <button
                href="#"
                className="font-medium text-blue-600 dark:text-blue-500 hover:underline"
                onClick={() => props.handleEdit(product.name, product.category, product.location, product.price , product.description , "delete")}
              >
                Delete
              </button>
            </div>
          </td>
        </tr>
      ))}
    </>
  );
}

export default DisplayProducts;

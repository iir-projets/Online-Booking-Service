import React from 'react'
import ReactDOM from 'react-dom/client'
import './index.css'
import {createBrowserRouter , RouterProvider} from 'react-router-dom';
import Home from './Home.jsx';
import Container from './Components/Container.jsx';
import Products from './Components/Products.jsx';


const router = createBrowserRouter([
  {
    //default to try new features
    path:'/',
    element: <Products/>
  },
  {
    //default to try new features
    path:'/container',
    element: <Container/>
  },
  {
    //default to try new features
    path:'/home',
    element: <Home/>
  }
]);

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
        <RouterProvider router={router}/>
  </React.StrictMode>,
)

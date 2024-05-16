import React from 'react'
import ReactDOM from 'react-dom/client'
import './index.css'
import {createBrowserRouter , RouterProvider} from 'react-router-dom';
import Home from './Home.jsx';
import Container from './Components/Container.jsx';
import Products from './Components/Products.jsx';
import Login from './Components/Login.jsx';
import Admin from './Admin.jsx';
import Loading from './Components/Loading.jsx';
import Succes from './Components/Succes.jsx';
import SessionExpired from './Components/SessionExpired.jsx';
import PDFTesting from './Components/PDFTesting.jsx';
import History from './Components/History.jsx';

import ProductsPageable from './Components/ProductsPageable.jsx';
import SignIn from './Components/SignIn.jsx';
import AdminHistory from './Components/AdminHistory.jsx';



const router = createBrowserRouter([
  {
    //default to try new features
    path:'/',
    element: <Login/>
  },
  {
    //default to try new features
    path:'/history',
    element: <History/>
  },
  {
    //default to try new features
    path:'/pdf',
    element: <PDFTesting/>
  },
  {
    //default to try new features
    path:'/session',
    element: <SessionExpired/>
  },
  {
    //default to try new features
    path:'/loading',
    element: <Loading/>
  },
  {
    //default to try new features
    path:'/Succes',
    element: <Succes/>
  },
  {
    //default to try new features
    path:'/Admin',
    element: <Admin/>
  },
  {
    //default to try new features
    path:'/Admin/history',
    element: <AdminHistory/>
  },
  {
    //default for production
    path:'/Login',
    element: <Login/>
  },/*
  {
    //default for production
    path:'/',
    element: <Login/>
  },*/
  {
    //default to try new features
    path:'/services',
    element: <Products/>
  },
  {
    //default to try new features
    path:'/pageable',
    element: <ProductsPageable/>
  },
  {
    //default to try new features
    path:'/createaccount',
    element: <SignIn/>
  },
  {
    //default to try new features
    path:'/Login',
    element: <Login/>
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

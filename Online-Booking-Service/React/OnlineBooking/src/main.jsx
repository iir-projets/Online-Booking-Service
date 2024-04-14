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

const router = createBrowserRouter([
  {
    //default to try new features
    path:'/',
    element: <Login/>
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

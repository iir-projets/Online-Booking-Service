import { useState ,useEffect } from "react";
import Nav from "./Components/Nav";
import HeroSection from "./Components/HeroSection";
import Footer from "./Components/Footer";
import { useNavigate } from "react-router-dom";


function Home() {

  
  return (
    <>
      <Nav></Nav>
      <HeroSection/>
      <Footer/>
    </>
  );
}

export default Home;

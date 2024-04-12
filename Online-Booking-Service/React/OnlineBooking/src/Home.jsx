import { useState } from "react";
import Nav from "./Components/Nav";
import HeroSection from "./Components/HeroSection";
import Footer from "./Components/Footer";

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

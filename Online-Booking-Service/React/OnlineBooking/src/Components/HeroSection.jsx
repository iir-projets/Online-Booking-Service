import Animation from "../Components/Animation";
import { TypeAnimation } from "react-type-animation";

function HeroSection() {
  console.log(localStorage.getItem("token"));

  return (
    <>
      {/*   hero Section  */}
      <div className="flex justify-between w-full mt-24">
        <div className="flex-col ml-28 mt-28 px-8 font-serif text-3xl font-bold text-wrap w-96 ">
          <TypeAnimation
            sequence={[
              // Same substring at the start will only be typed out once, initially
              "Book your dream experience with a click.",
              1000,
              "Explore our exceptional services now and book an unforgettable experience!",
              1000,
              "You wish We Deliver !",
              1000,
              "Discover, book, and enjoy - it's that simple.",
              1000,
            ]}
            wrapper="span"
            speed={50}
            style={{ fontSize: "1em", display: "inline-block" }}
            repeat={Infinity}
          />
          <p>______________</p>
          <button
            className="flex ml-12 border-2 p-4 font-thin font-serif text-l bg-red-400 mr-8 rounded-lg text-white hover:bg-white hover:text-red-400 duration-700 hover:border-red-400 shadow-2xl hover:translate-y-6 gap-2 dark:bg-purple-500 dark:hover:bg-white dark:hover:border-purple-500 dark:hover:text-purple-500"
          >
            Join us Now !
          </button>
        </div>
        <Animation />
      </div>
    </>
  );
}

export default HeroSection;

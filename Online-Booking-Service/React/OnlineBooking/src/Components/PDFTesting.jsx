import { useState } from "react";
import axios from "axios";

function PDFTesting() {
  const [loading, setLoading] = useState(false);

  const handleClick = async () => {
    try {
      setLoading(true);
      const response = await axios.post(
        "http://localhost:9085/reservation/PDF",
        {
          serviceName: "Service in PDF",
          servicePrice: 100, // Replace with the actual service price
        }
      );
      console.log("PDF generated:", response);
    } catch (error) {
      console.error("Error generating PDF:", error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex justify-center items-center rounded-lg w-screen h-svh font-mono text-3xl">
      <div className="grid grid-cols-1 gap-10">
        <h1>Hey, this is for Generating PDF Testing</h1>
        <button
          onClick={handleClick}
          disabled={loading}
          className="rounded-2xl ring-2 p-4 hover:bg-red-400 duration-1000 hover:translate-y-4 hover:text-emerald-950"
        >
          {loading ? "Generating PDF..." : "Click to generate PDF"}
        </button>
      </div>
    </div>
  );
}

export default PDFTesting;

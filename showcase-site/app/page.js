"use client";

import { useRef } from "react"; // 1. Import useRef
import ProductCanvas from "./ProductCanvas";
import { motion, useScroll, useTransform } from "framer-motion";

import NavBar from "./components/NavBar";
import HowItWorks from "./components/HowItWorks";
import Features from "./components/Features";
import Waitlist from "./components/Waitlist";
import FAQ from "./components/FAQ";
import Footer from "./components/Footer";
import Contact from "./components/Contact";

export default function Home() {
  const containerRef = useRef(null); // 2. Create the container ref // 3. Update useScroll to target the container.

  // This calculates progress just for the container, from when its top hits
  // the viewport top ("start start") to when its bottom hits the viewport bottom ("end end").
  const { scrollYProgress } = useScroll({
    target: containerRef,
    offset: ["start start", "end end"],
  }); // These transforms are now perfectly tied to the container's scroll progress

  const heroTextY = useTransform(scrollYProgress, [0, 0.1], ["0%", "-50%"]); // Fades in first 10%
  const heroTextOpacity = useTransform(scrollYProgress, [0, 0.1], [1, 0]);

  return (
    <main>
      <NavBar />
      <div ref={containerRef} className="relative h-[250vh]">
        <section className="sticky top-20 h-screen flex items-center justify-center bg-base-100 overflow-hidden">
          <motion.div
            style={{ y: heroTextY, opacity: heroTextOpacity }}
            className="relative z-10 text-center p-4"
          >
            <div className="inline-block">
              <h2
                className="text-5xl sm:text-7xl lg:text-8xl font-extrabold tracking-tight text-hero"
                style={{
                  textShadow:
                    "2px 2px 0px #000, 4px 4px 0px #333, 6px 6px 0px #666, 8px 8px 0px #999",
                }}
              >
                ZenTap
              </h2>

              <p
                className="mt-4 text-lg sm:text-xl max-w-2xl mx-auto font-extrabold tracking-tight text-hero/80"
                style={{
                  textShadow: "2px 2px 0px #000, 3px 3px 0px #333",
                }}
              >
                Reclaim Your Time From Your Phone
              </p>
            </div>
          </motion.div>

          <div className="absolute inset-0 w-full h-full z-0">
            <ProductCanvas containerRef={containerRef} />
          </div>
        </section>
      </div>
      <HowItWorks />
      <Features />
      <Waitlist />
      <FAQ />
      <Contact />
      <Footer />
    </main>
  );
}

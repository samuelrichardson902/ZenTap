"use client";

import { useRef } from "react";
import ProductCanvas from "./ProductCanvas";
import { motion, useScroll, useTransform } from "framer-motion";
import Link from "next/link";

import NavBar from "./components/NavBar";
import HowItWorks from "./components/HowItWorks";
import Features from "./components/Features";
import FAQ from "./components/FAQ";
import Footer from "./components/Footer";
import Contact from "./components/Contact";
import WhyWeAreDifferent from "./components/WhyWeAreDifferent";
import GetStarted from "./components/GetStarted";

export default function Home() {
  const containerRef = useRef(null);

  const { scrollYProgress } = useScroll({
    target: containerRef,
    offset: ["start start", "end end"],
  });

  const heroTextY = useTransform(scrollYProgress, [0, 0.1], ["0%", "-50%"]);
  const heroTextOpacity = useTransform(scrollYProgress, [0, 0.1], [1, 0]);

  return (
    <main>
      <NavBar />
      <div ref={containerRef} className="relative h-[200vh]">
        <section className="sticky top-20 h-screen flex items-center justify-center bg-base-100 overflow-hidden">
          <motion.div
            style={{ y: heroTextY, opacity: heroTextOpacity }}
            className="relative z-10 text-center p-4"
          >
            <div className="inline-block">
              <h1 className="text-3xl sm:text-5xl lg:text-6xl font-extrabold tracking-tight text-hero headerShadow">
                ZenTap
              </h1>
              <p className="text-xl sm:text-xl lg:text-2xl font-bold tracking-tight text-hero headerShadow mt-4">
                The Open-Source Physical App Blocker
              </p>
              <div className="mt-6 flex justify-center gap-4">
                <Link
                  href="#getStarted"
                  className="btn bg-hero rounded-lg boxShadow"
                >
                  Get started
                </Link>
              </div>
            </div>
          </motion.div>

          <div className="absolute inset-0 w-full h-full z-0">
            <ProductCanvas containerRef={containerRef} />
          </div>
        </section>
      </div>
      <HowItWorks />
      <WhyWeAreDifferent />
      <Features />
      <GetStarted />
      <FAQ />
      <Contact />
      <Footer />
    </main>
  );
}

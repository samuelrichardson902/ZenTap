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
      <div ref={containerRef} className="relative h-[250vh]">
        <section className="sticky top-20 h-screen flex items-center justify-center bg-base-100 overflow-hidden">
          <motion.div
            style={{ y: heroTextY, opacity: heroTextOpacity }}
            className="relative z-10 text-center p-4"
          >
            <div className="inline-block">
              <h1 className="text-4xl sm:text-6xl lg:text-7xl font-extrabold tracking-tight text-hero headerShadow">
                The Open-Source, Offline-First App Blocker
              </h1>
              <div className="mt-8 flex justify-center gap-4">
                <Link href="#get-started" className="btn btn-primary">
                  Get it now
                </Link>
                <Link href="#get-started" className="btn btn-secondary">
                  Buy a Supporter Tag
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

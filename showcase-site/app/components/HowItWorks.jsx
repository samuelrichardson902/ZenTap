"use client";

import { useState, useEffect } from "react";
import PhoneDisplay from "./PhoneDisplay";

export default function HowItWorks() {
  const [currentImageIndex, setCurrentImageIndex] = useState(0);

  const blockedAppScreens = [
    "/app-showcase/blocked-app-screens/instagram_space.png",
    "/app-showcase/blocked-app-screens/netflix_sleeping.png",
    "/app-showcase/blocked-app-screens/snapchat_ghosted.png",
    "/app-showcase/blocked-app-screens/youtube_fishing.png",
  ];

  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentImageIndex(
        (prevIndex) => (prevIndex + 1) % blockedAppScreens.length
      );
    }, 3000); // Change image every 3 seconds

    return () => clearInterval(interval);
  }, []);

  return (
    <section id="howItWorks" className="relative bg-base-200 p-12 scroll-mt-20">
      <h2 className="text-4xl font-bold">How ZenTap Works</h2>
      <div className="flex">
        <p>put some mockups/descriptions here</p>
        <PhoneDisplay
          phoneImg={blockedAppScreens[currentImageIndex]}
          imgAlt={"App Blocking Screen"}
        />
      </div>
    </section>
  );
}

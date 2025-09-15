"use client";

import { useState, useEffect } from "react";
import PhoneDisplay from "./PhoneDisplay";

export default function HowItWorks() {
  const [currentImageIndex, setCurrentImageIndex] = useState(0);

  const blockedAppScreens = [
    "/app-showcase/blocked-app-screens/instagram_sleeping.png",
    "/app-showcase/blocked-app-screens/netflix_space.png",
    "/app-showcase/blocked-app-screens/snapchat_coffee.png",
    "/app-showcase/blocked-app-screens/youtube_construction.png",
  ];

  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentImageIndex(
        (prevIndex) => (prevIndex + 1) % blockedAppScreens.length
      );
    }, 3000);

    return () => clearInterval(interval);
  }, [blockedAppScreens.length]);

  return (
    <section
      id="howItWorks"
      className="relative bg-base-200 py-24 px-4 scroll-mt-20"
    >
      <div className="max-w-3xl mx-auto text-center mb-16">
        <h2 className="text-4xl lg:text-5xl font-bold mb-4 headerShadow">
          How ZenTap Works
        </h2>
        <p className="text-lg text-base-content/80">
          Regain your focus in three simple steps. Select distracting apps,
          activate the blocker, and get your time back.
        </p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-12 max-w-7xl mx-auto">
        <FeatureStep
          title="1. Select Your Apps"
          description="Choose exactly which apps you want to block."
        >
          <PhoneDisplay
            phoneImg={"/app-showcase/app_selection.png"}
            imgAlt={"Choose What Apps to Block"}
          />
        </FeatureStep>

        <FeatureStep
          title="2. Activate Focus"
          description="Activate the blocker from the home screen or by tapping your ZenTap tag."
        >
          <PhoneDisplay
            phoneImg={"/app-showcase/home_screen.png"}
            imgAlt={"Home Screen"}
          />
        </FeatureStep>

        <FeatureStep
          title="3. Enjoy Focus"
          description="Your apps are now blocked until you tap your tag again to unlock."
        >
          <PhoneDisplay
            phoneImg={blockedAppScreens[currentImageIndex]}
            imgAlt={"App Blocking Screen"}
          />
        </FeatureStep>
      </div>
    </section>
  );
}

function FeatureStep({ title, description, children }) {
  return (
    <div className="flex flex-col items-center text-center justify-between">
      <div>
        <h3 className="text-xl font-bold mb-3">{title}</h3>
        <p className="text-base-content/70 mb-6 max-w-xs">{description}</p>
      </div>

      <div>{children}</div>
    </div>
  );
}

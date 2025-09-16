"use client";

import { useState } from "react";
import PhoneDisplay from "./PhoneDisplay";

import { TbLock, TbClockCog, TbClockPlus, TbTags } from "react-icons/tb";
import { IoWarningOutline } from "react-icons/io5";

// (featuresData array remains the same)
// ...
const featuresData = [
  {
    icon: TbLock,
    title: "Strict Mode",
    description:
      "Turns the tag toggling on and off the blocker to instead only allowing you access for your chosen amount of time set by the unlock duration for unbreakable focus.",
    phoneImg: "/app-showcase/home_screen_strict.png",
    imgAlt: "Strict Mode Enabled",
  },
  {
    icon: TbClockCog,
    title: "AutoLock",
    description:
      "Automatically lock and unlock your apps on a schedule. Set it for work, study, or bedtime.",
    phoneImg: "/app-showcase/set_auto_lock.png",
    imgAlt: "AutoLock scheduling screen",
  },
  {
    icon: IoWarningOutline,
    title: "Break Glass",
    description:
      "Need emergency access? This feature lets you turn off blocking if you lose your tag",
    phoneImg: "/app-showcase/break_glass.png",
    imgAlt: "Break Glass feature",
  },
  {
    icon: TbClockPlus,
    title: "Request a Minute",
    description:
      "Lets you request a 1-minute unlock, however it makes you wait 30 seconds allowing you emergency access to respond to someone but helping to stop impulsive app openings.",
    phoneImg: "/app-showcase/request_minute.png",
    imgAlt: "Request a Minute feature",
  },
  {
    icon: TbTags,
    title: "Manage Your Tags",
    description:
      "You can have multiple tags allowing you to lock and unlock your apps in  different places like at the office or your home",
    phoneImg: "/app-showcase/manage_tags.png",
    imgAlt: "Manage Tags feature",
  },
];
// ...

const DEFAULT_DISPLAY = {
  img: featuresData[0].phoneImg,
  alt: featuresData[0].imgAlt,
};

export default function Features() {
  const [activeDisplay, setActiveDisplay] = useState(DEFAULT_DISPLAY);

  return (
    <section
      id="features"
      className="relative bg-base-200 py-24 px-4 scroll-mt-20"
    >
      <div className="max-w-7xl mx-auto text-center mb-16">
        <h2 className="text-4xl lg:text-5xl font-bold mb-4 headerShadow">
          Features
        </h2>
      </div>

      {/* --- Main 2-Column Grid --- */}
      <div
        className="grid grid-cols-1 md:grid-cols-2 gap-12 max-w-7xl mx-auto items-start"
        // --- THIS IS THE CHANGE ---
        // The handler is now on the grid container
        onMouseLeave={() => setActiveDisplay(DEFAULT_DISPLAY)}
      >
        {/* 4. Left Column: List of Feature Cards */}
        <div
          className="flex flex-col gap-6"
          // onMouseLeave was here, but has been moved up
        >
          {featuresData.map((feature) => (
            <FeatureItem
              key={feature.title}
              feature={feature}
              onMouseEnter={() =>
                setActiveDisplay({
                  img: feature.phoneImg,
                  alt: feature.imgAlt,
                })
              }
              // No onMouseLeave here
            />
          ))}
        </div>

        {/* 5. Right Column: Sticky Phone Display (unchanged) */}
        <div className="hidden md:block sticky top-28 h-fit">
          <div key={activeDisplay.img} className="animate-fadeIn">
            <PhoneDisplay
              phoneImg={activeDisplay.img}
              imgAlt={activeDisplay.alt}
            />
          </div>
        </div>
      </div>
    </section>
  );
}

function FeatureItem({ feature, onMouseEnter }) {
  const Icon = feature.icon;

  return (
    <div
      className="flex items-center gap-5 p-5 bg-neutral rounded-2xl boxShadow hover:-translate-y-1 transition-all duration-300 cursor-pointer"
      onMouseEnter={onMouseEnter}
    >
      {/* Icon Container (will no longer stretch) */}
      <div className="flex-shrink-0 p-3 bg-base-100 rounded-lg ring-1 ring-white/10">
        <Icon className="w-6 h-6 text-primary" />
      </div>

      {/* Text Content */}
      <div className="text-left">
        <h3 className="text-lg font-bold mb-1">{feature.title}</h3>
        <p className="text-base-content/70 text-sm">{feature.description}</p>
      </div>
    </div>
  );
}

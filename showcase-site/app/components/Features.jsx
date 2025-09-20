"use client";

import { useState } from "react";
import PhoneDisplay from "./PhoneDisplay";

import { TbLock, TbClockCog, TbClockPlus, TbTags } from "react-icons/tb";
import { IoWarningOutline } from "react-icons/io5";

const featuresData = [
  {
    icon: TbLock,
    title: "Strict Mode",
    description: `For unbreakable focus. In Strict Mode, tapping your tag won't turn off the block. It will only grant you a short, pre-set "unlock duration" (like 5 minutes) to quickly do what you need before re-locking automatically.`,
    phoneImg: "/app-showcase/home_screen_strict.png",
    imgAlt: "Strict Mode Enabled",
  },
  {
    icon: TbClockCog,
    title: "AutoLock",
    description:
      "Build a routine and put your focus on autopilot. Schedule ZenTap to automatically lock distracting apps during work hours, study sessions, or in the evening to protect your time.",
    phoneImg: "/app-showcase/set_auto_lock.png",
    imgAlt: "AutoLock scheduling screen",
  },
  {
    icon: IoWarningOutline,
    title: "Break Glass",
    description: `For true emergencies, the 'Break Glass' feature is your escape hatch. It requires you to type a random string of text to disable the blocker, and it's purposefully annoying. This creates just enough friction to make you stop and think: 'Is this really an emergency?'`,
    phoneImg: "/app-showcase/break_glass.png",
    imgAlt: "Break Glass feature",
  },
  {
    icon: TbClockPlus,
    title: "Request a Minute",
    description: `Sometimes you just need a moment. This feature grants a 60-second unlock for quick tasks, like responding to a message. A built-in 30-second delay acts as a "cooling-off" period, preventing impulse and preserving your overall focus.`,
    phoneImg: "/app-showcase/request_minute.png",
    imgAlt: "Request a Minute feature",
  },
  {
    icon: TbTags,
    title: "Manage Your Tags",
    description:
      "Sync multiple tags to your account. Keep a tag at your office desk, one in your kitchen at home, and another in your bag, ensuring you always have a key to your focus nearby.",
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
      className="relative bg-base-100 py-24 px-4 scroll-mt-20"
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

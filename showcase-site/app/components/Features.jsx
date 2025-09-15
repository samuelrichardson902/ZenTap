"use client";

import PhoneDisplay from "./PhoneDisplay";

const featuresData = [
  {
    title: "Strict Mode",
    description: "For when you need unbreakable focus. Strict mode prevents you from changing settings or unlocking apps early.",
    phoneImg: "/app-showcase/home_screen_strict.png",
    imgAlt: "Strict Mode Enabled",
  },
  {
    title: "AutoLock",
    description: "Automatically lock your apps on a schedule. Set it for work hours, study sessions, or bedtime, and let ZenTap do the rest.",
    phoneImg: "/app-showcase/set_auto_lock.png",
    imgAlt: "AutoLock scheduling screen",
  },
  {
    title: "Break Glass",
    description: "Need to access a blocked app in an emergency? The 'Break Glass' feature lets you unlock an app for one minute.",
    phoneImg: "/app-showcase/request_minute.png",
    imgAlt: "Break Glass feature",
  },
];

export default function Features() {
  return (
    <section
      id="features"
      className="relative bg-base-200 py-24 px-4 scroll-mt-20"
    >
      <div className="max-w-7xl mx-auto text-center mb-16">
        <h2 className="text-4xl lg:text-5xl font-bold mb-4 headerShadow">
          A Powerful Toolkit for Focus
        </h2>
        <p className="text-lg text-base-content/80 max-w-2xl mx-auto">
          All the tools you need to build better habits, not just a simple
          blocker.
        </p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-12 max-w-7xl mx-auto">
        {featuresData.map((feature) => (
          <FeatureCard
            key={feature.title}
            title={feature.title}
            description={feature.description}
            phoneImg={feature.phoneImg}
            imgAlt={feature.imgAlt}
          />
        ))}
      </div>
    </section>
  );
}

function FeatureCard({ title, description, phoneImg, imgAlt }) {
  return (
    <div className="flex flex-col items-center text-center">
      <div className="mb-6">
        <PhoneDisplay phoneImg={phoneImg} imgAlt={imgAlt} />
      </div>
      <h3 className="text-2xl font-bold mb-3">{title}</h3>
      <p className="text-base-content/70 max-w-xs">{description}</p>
    </div>
  );
}

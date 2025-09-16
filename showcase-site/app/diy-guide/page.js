"use client";

import Link from "next/link";
import { ArrowLeft } from "lucide-react";
import PhoneDisplay from "../components/PhoneDisplay";
import { useState } from "react";

// --- The steps array (unchanged) ---
const steps = [
  {
    number: 1,
    title: "Get a Compatible Tag",
    description:
      "First, you'll need a compatible NFC tag. Most NTAG (NTAG213, 215, 216) types will work. They are widely available and inexpensive online.",
    img: "/diy-guide/step_1.png",
  },
  {
    number: 2,
    title: "Download 'NFC Tools' App",
    description:
      "You'll need an app that can write data to an NFC tag. We recommend 'NFC Tools', as it's free, popular, and easy to use.",
    img: "/diy-guide/step_1.png",
  },
  {
    number: 3,
    title: "Add a New Record",
    description:
      "Open the 'NFC Tools' app, go to the 'Write' tab, and tap the 'Add a record' button.",
    img: "/diy-guide/step_2.png",
  },
  {
    number: 4,
    title: "Choose 'Custom URL/URI'",
    description:
      "A list of record types will appear. Scroll down and select 'Custom URL/URI' from the options.",
    img: "/diy-guide/step_3.png",
  },
  {
    number: 5,
    title: "Enter Your Custom URI",
    description: (
      <>
        This is the most important step. In the input field, type your unique
        URI in this format:
        <br />
        <code className="bg-base-300 p-2 rounded-md my-2 inline-block">
          zentap://tag/my-unique-id
        </code>
        <br />
        The `my-unique-id` part can be any random text (e.g., 'office-tag' or
        '12345'). Just make it unique! Tap 'OK' when you're done.
      </>
    ),
    img: "/diy-guide/step_4.png",
  },
  {
    number: 6,
    title: "Write to Your Tag",
    description:
      "You'll be back on the 'Write' tab. Tap the large 'Write' button and then immediately hold your phone over your blank NFC tag to write the data.",
    img: "/diy-guide/step_5.png",
  },
  {
    number: 7,
    title: "You're All Set!",
    description:
      "Once you see the 'Write complete' confirmation, that's it! Open the ZenTap app, and your tag is now ready to use to activate and deactivate focus mode.",
    img: "/diy-guide/step_6.png",
  },
];

export default function DiyGuidePage() {
  const [activeScreen, setActiveScreen] = useState(steps[0].img);

  return (
    <main className="bg-base-100 lg:h-screen lg:overflow-hidden">
      {/* *** CHANGED ***
        - Changed 'lg:py-8' to 'lg:pt-8 lg:pb-12' to add bottom padding.
      */}
      <div className="container mx-auto px-4 py-16 lg:pt-8 lg:pb-12 h-full flex flex-col">
        <div className="max-w-5xl mx-auto w-full flex flex-col h-full">
          <div className="mb-8">
            <Link href="/" className="btn btn-ghost">
              <ArrowLeft size={20} className="mr-2" />
              Back to Home
            </Link>
          </div>

          <h1 className="text-4xl lg:text-5xl font-bold mb-4 headerShadow">
            DIY Guide: Program Your Own NFC Tag
          </h1>
          <p className="text-lg text-base-content/80 mb-8">
            Follow these simple steps to set up your own NFC tag to work with
            ZenTap.
          </p>

          <div className="flex flex-col-reverse lg:flex-row gap-12 lg:gap-16 flex-1 min-h-0">
            {/* --- 1. LEFT (SCROLLING) COLUMN --- */}
            <div className="lg:w-1/2 lg:overflow-y-auto space-y-4 scrollbar-hide lg:pr-4">
              {steps.map((step) => (
                <Step
                  key={step.number}
                  number={step.number}
                  title={step.title}
                  description={step.description}
                  onHover={() => setActiveScreen(step.img)}
                />
              ))}

              <div className="mt-8">
                <Link href="/" className="btn btn-ghost">
                  <ArrowLeft size={20} className="mr-2" />
                  Back to Home
                </Link>
              </div>
            </div>

            {/* --- 2. RIGHT (STATIC) COLUMN --- */}
            <div className="lg:w-1/2">
              <div>
                <PhoneDisplay
                  phoneImg={activeScreen}
                  imgAlt="NFC programming steps"
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  );
}

// --- The Step component (unchanged) ---
function Step({ number, title, description, onHover }) {
  return (
    <div
      onMouseEnter={onHover}
      className="flex items-start p-4 rounded-lg transition-colors duration-150 hover:bg-base-200 cursor-pointer"
    >
      <div className="flex-shrink-0">
        <span className="flex items-center justify-center h-12 w-12 rounded-full bg-primary text-primary-content font-bold text-xl">
          {number}
        </span>
      </div>
      <div className="ml-4">
        <h3 className="text-2xl font-bold capitalize">{title}</h3>
        <p className="mt-1 text-base-content/70">{description}</p>
      </div>
    </div>
  );
}

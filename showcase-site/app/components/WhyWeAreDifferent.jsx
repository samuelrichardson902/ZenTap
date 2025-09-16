import { ShieldCheck, Code, Tag } from "lucide-react";
import { IoKeyOutline } from "react-icons/io5";
import { TbBarrierBlock } from "react-icons/tb";

export default function WhyWeAreDifferent() {
  const principles = [
    {
      icon: <Code size={48} className="text-primary" />,
      title: "Open-Source",
      description:
        "Our code is 100% open-source on GitHub. This means complete transparency and the ability for you to inspect, modify, and even add features to your own app.",
      link: "https://github.com/samuelrichardson902/ZenTap",
    },
    {
      icon: <TbBarrierBlock size={48} className="text-primary" />,
      title: "Physical Barrier to Impulse",
      description:
        "We turn a digital problem into a physical solution. Mindless scrolling is an impulse. To unlock your apps, you must perform a deliberate, physical action. This friction breaks the habit loop and gives you back control.",
    },
    {
      icon: <ShieldCheck size={48} className="text-primary" />,
      title: "Privacy-First & Offline",
      description:
        "ZenTap is 100% offline. All your data is stored exclusively on your device. No servers, no tracking, no exceptions. Your data is yours, period.",
    },
    {
      icon: <Tag size={48} className="text-primary" />,
      title: "Free Forever",
      description:
        "The ZenTap app is 100% free. Period. It works with any standard NFC tag, removing the barrier to entry. Follow our simple guide to program your own, or buy a pre-programmed tag from us to skip the setup and support the project.",
    },
  ];

  return (
    <section id="whyWereDifferent" className="bg-base-100 py-24 px-4">
      <div className="max-w-5xl mx-auto">
        <div className="text-center mb-16">
          <h2 className="text-4xl lg:text-5xl font-bold mb-4 headerShadow">
            Why We're Different
          </h2>
        </div>
        <div className="grid md:grid-cols-4 gap-12 text-center">
          {principles.map((principle) => (
            <div key={principle.title} className="flex flex-col items-center">
              <div className="mb-4">{principle.icon}</div>
              <h3 className="text-2xl font-bold mb-2">{principle.title}</h3>
              <p className="text-base-content/70 mb-4">
                {principle.description}
              </p>
              {principle.link && (
                <a
                  href={principle.link}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="link link-primary"
                >
                  View on GitHub
                </a>
              )}
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}

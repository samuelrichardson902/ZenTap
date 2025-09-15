import { ShieldCheck, Code, Tag } from "lucide-react";

export default function WhyWeAreDifferent() {
  const principles = [
    {
      icon: <Code size={48} className="text-primary" />,
      title: "Open-Source",
      description:
        "Our code is 100% open-source on GitHub. This means complete transparency and the ability for you to inspect, modify, and even add features to your own app.",
      link: "https://github.com/your-repo", // Replace with your actual GitHub repo
    },
    {
      icon: <ShieldCheck size={48} className="text-primary" />,
      title: "Privacy-First & Offline",
      description:
        "ZenTap is 100% offline. All your data is stored exclusively on your device. No servers, no tracking, no exceptions. Your data is yours, period.",
    },
    {
      icon: <Tag size={48} className="text-primary" />,
      title: "Your Choice (BYO Tag)",
      description:
        "The app is free to use. You can program any standard NFC tag to work with ZenTap. We empower you to use what you already have, removing any barrier to entry.",
    },
  ];

  return (
    <section id="why-we-are-different" className="bg-base-100 py-24 px-4">
      <div className="max-w-5xl mx-auto">
        <div className="text-center mb-16">
          <h2 className="text-4xl lg:text-5xl font-bold mb-4 headerShadow">
            Why We're Different
          </h2>
          <p className="text-lg text-base-content/80 max-w-2xl mx-auto">
            Our philosophy is simple: build a tool that respects users and puts them in control.
          </p>
        </div>
        <div className="grid md:grid-cols-3 gap-12 text-center">
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

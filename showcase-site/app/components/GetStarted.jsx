import Link from "next/link";
import { TestTube, Heart, Wrench } from "lucide-react";

export default function GetStarted() {
  const options = [
    {
      icon: <TestTube size={48} className="text-primary" />,
      title: "Become a Tester",
      description: "Get the app for free and help us improve. Your feedback is invaluable.",
      cta: "Sign Up as a Tester",
      link: "https://play.google.com/apps/internaltest/4700210753530599830",
      isExternal: true,
    },
    {
      icon: <Heart size={48} className="text-primary" />,
      title: "Buy a Supporter Tag",
      description: "Skip the setup and support the project. Get a pre-programmed tag that works instantly.",
      cta: "Buy Now",
      link: "https://your-store-link.com", // Replace with your actual store link
      isExternal: true,
    },
    {
      icon: <Wrench size={48} className="text-primary" />,
      title: "Program Your Own Tag",
      description: "Have your own NFC tag? Follow our simple guide to set it up yourself.",
      cta: "View DIY Guide",
      link: "/diy-guide",
      isExternal: false,
    },
  ];

  return (
    <section id="get-started" className="bg-base-100 py-24 px-4 scroll-mt-20">
      <div className="max-w-5xl mx-auto">
        <div className="text-center mb-16">
          <h2 className="text-4xl lg:text-5xl font-bold mb-4 headerShadow">
            Get Started with ZenTap
          </h2>
          <p className="text-lg text-base-content/80 max-w-2xl mx-auto">
            Choose the path that's right for you.
          </p>
        </div>
        <div className="grid md:grid-cols-3 gap-8">
          {options.map((option) => (
            <div
              key={option.title}
              className="bg-base-200 p-8 rounded-lg flex flex-col items-center text-center boxShadow"
            >
              <div className="mb-4">{option.icon}</div>
              <h3 className="text-2xl font-bold mb-2">{option.title}</h3>
              <p className="text-base-content/70 mb-6 flex-grow">
                {option.description}
              </p>
              <Link
                href={option.link}
                className="btn btn-primary"
                target={option.isExternal ? "_blank" : "_self"}
                rel={option.isExternal ? "noopener noreferrer" : ""}
              >
                {option.cta}
              </Link>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}

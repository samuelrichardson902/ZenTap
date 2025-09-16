import Link from "next/link";
import { Heart, Wrench } from "lucide-react";
import { GrTest } from "react-icons/gr";
import { RiGooglePlayLine } from "react-icons/ri";

export default function GetStarted() {
  const options = [
    {
      icon: <RiGooglePlayLine size={48} className="text-primary" />,
      title: "Become a Tester",
      description:
        "Get the app on google play for free and help us to improve. Your feedback is invaluable.",
      cta: "Sign Up as a Tester on",
      link: "https://play.google.com/apps/internaltest/4700210753530599830",
      isExternal: true,
    },
    {
      icon: <Heart size={48} className="text-primary" />,
      title: "Buy a Supporter Tag",
      description:
        "Skip the setup and support the project. Get a pre-programmed tag that works instantly.",
      cta: "Buy Now",
      link: "/shop",
      isExternal: false,
    },
    {
      icon: <Wrench size={48} className="text-primary" />,
      title: "Program Your Own Tag",
      description:
        "Have your own NFC tag? Follow our simple guide to set it up yourself.",
      cta: "View DIY Guide",
      link: "/diy-guide",
      isExternal: false,
    },
  ];

  return (
    <section id="getStarted" className="bg-base-100 py-24 px-4 scroll-mt-20">
      <div className="max-w-5xl mx-auto">
        <div className="text-center mb-16">
          <h2 className="text-4xl lg:text-5xl font-bold mb-4 headerShadow">
            Get Started with ZenTap
          </h2>
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

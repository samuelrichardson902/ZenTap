"use client";
import { useState, useRef, useEffect } from "react";
import { ChevronDown } from "lucide-react";
import Link from "next/link";

export default function FAQ() {
  const faqData = [
    {
      question: "How do I program my own tag?",
      answer: (
        <>
          We have a dedicated guide for that! You can find the full
          step-by-step instructions on our{" "}
          <Link href="/diy-guide" className="link link-primary">
            DIY Guide page
          </Link>
          .
        </>
      ),
    },
    {
      question: "What kind of NFC tag do I need to buy?",
      answer:
        "You'll need an NTAG213, NTAG215, or NTAG216 chip. These are the most common and widely compatible types of NFC tags. We recommend a tag with at least 144 bytes of memory to be safe.",
    },
    {
      question: "What app do I use to write to my tag?",
      answer:
        "We recommend 'NFC Tools' for both Android and iOS. It's a free and powerful app that makes it easy to write the necessary data to your tag.",
    },
    {
      question: "How do I know my data is private?",
      answer:
        "Because ZenTap is 100% offline. The app has no permission to access the internet, and we have no servers to store your data on. All your information stays on your device, always.",
    },
    {
      question: "Why is it open-source?",
      answer:
        "We believe in transparency and community. By making our code open-source, anyone can inspect it to verify our privacy claims. It also allows the community to contribute, suggest features, and even create their own versions of the app.",
    },
  ];

  const [openIndex, setOpenIndex] = useState(null);

  const toggleFAQ = (index) => {
    setOpenIndex(openIndex === index ? null : index);
  };

  return (
    <section
      id="faq"
      className="relative min-h-screen bg-base-200 py-12 px-4 sm:px-6 lg:px-12 scroll-mt-20 flex items-center justify-center"
    >
      <div className="w-full max-w-4xl mx-auto">
        <div className="text-center mb-12">
          <h2 className="text-4xl md:text-5xl font-bold headerShadow">
            Frequently Asked Questions
          </h2>
          <p className="mt-4 text-lg text-base-content/80">
            Here are some of our most common questions. If you can't find what
            you're looking for, feel free to contact us.
          </p>
        </div>

        <div className="space-y-4">
          {faqData.map((item, index) => (
            <FaqItem
              key={index}
              question={item.question}
              answer={item.answer}
              isOpen={openIndex === index}
              onClick={() => toggleFAQ(index)}
            />
          ))}
        </div>
      </div>
    </section>
  );
}

const FaqItem = ({ question, answer, isOpen, onClick }) => {
  const contentRef = useRef(null);
  const [height, setHeight] = useState(0);

  useEffect(() => {
    if (contentRef.current) {
      setHeight(isOpen ? contentRef.current.scrollHeight : 0);
    }
  }, [isOpen]);

  return (
    <div className="bg-base-100 border border-base-300 rounded-lg overflow-hidden boxShadow">
      <button
        className="w-full flex justify-between items-center px-4 py-4 text-left text-xl font-medium transition-colors duration-300 hover:bg-base-200"
        onClick={onClick}
      >
        {question}
        <ChevronDown
          className={`w-6 h-6 transition-transform duration-300 ${
            isOpen ? "rotate-180" : ""
          }`}
        />
      </button>

      <div
        ref={contentRef}
        style={{ maxHeight: `${height}px` }}
        className="transition-[max-height] duration-500 ease-in-out overflow-hidden px-4 text-base-content/80"
      >
        <div className="py-2">{answer}</div>
      </div>
    </div>
  );
};

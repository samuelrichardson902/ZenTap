"use client";
import { useState, useRef, useEffect } from "react";
import { ChevronDown } from "lucide-react";

export default function FAQ() {
  const faqData = [
    {
      question: "Do you track any of my data?",
      answer:
        "No our app functions solely on your phone and doesn't send anything to our servers",
    },
    {
      question: "How long does shipping take?",
      answer:
        "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.",
    },
    {
      question: "Do you ship internationally?",
      answer:
        "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium.",
    },
    {
      question: "How can I track my order?",
      answer:
        "Nemo enim ipsam voluptatem quia voluptas sit asnatur aut odit aut fugit, sed quia consequuntur magni dolores eos.",
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

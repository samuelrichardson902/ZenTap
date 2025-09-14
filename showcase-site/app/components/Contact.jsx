"use client";

import { useState } from "react";
import emailjs from "@emailjs/browser";
import { toast } from "react-hot-toast";

export default function Contact() {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    message: "",
  });
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);

  // EmailJS config from environment variables
  const serviceId = process.env.NEXT_PUBLIC_EMAILJS_SERVICE_ID;
  const templateId = process.env.NEXT_PUBLIC_EMAILJS_TEMPLATE_ID;
  const publicKey = process.env.NEXT_PUBLIC_EMAILJS_PUBLIC_KEY;

  // Email validation regex
  const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  const validateForm = () => {
    console.log("EmailJS config:", {
      serviceId,
      templateId,
      publicKey,
      formData,
    });

    let formErrors = {};
    let valid = true;
    if (!formData.name.trim()) {
      formErrors.name = "Name is required";
      valid = false;
    }
    if (!formData.email) {
      formErrors.email = "Email is required";
      valid = false;
    } else if (!emailPattern.test(formData.email)) {
      formErrors.email = "Please enter a valid email";
      valid = false;
    }
    if (!formData.message.trim()) {
      formErrors.message = "Message is required";
      valid = false;
    }
    setErrors(formErrors);
    return valid;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
    if (errors[name]) {
      setErrors((prevErrors) => ({
        ...prevErrors,
        [name]: null,
      }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validateForm()) return;
    setLoading(true);
    setErrors({});
    try {
      await emailjs.send(serviceId, templateId, formData, publicKey);
      toast.success("Message sent successfully!");
      setFormData({ name: "", email: "", message: "" });
    } catch (error) {
      toast.error("Failed to send message.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <section
        id="contact"
        className="relative min-h-screen bg-base-200 py-12 px-4 sm:px-6 lg:px-12 scroll-mt-20 flex items-center justify-center"
      >
        <div className="w-full max-w-4xl mx-auto">
          <div className="text-center mb-12">
            <h2 className="text-4xl md:text-5xl font-bold headerShadow">
              Contact Us
            </h2>
            <p className="mt-4 text-lg text-base-content/80">
              Don't hesitate to get in touch.
            </p>
          </div>

          {/* Contact Form */}
          <form
            onSubmit={handleSubmit}
            className="bg-base-100 border border-base-300 rounded-lg p-6 space-y-4 boxShadow"
          >
            <div>
              <label className="label">
                <span className="label-text">Name</span>
              </label>
              <input
                type="text"
                name="name"
                value={formData.name}
                onChange={handleChange}
                className={`input input-bordered w-full ${
                  errors.name ? "input-error" : ""
                }`}
              />
              {errors.name && (
                <p className="text-error text-sm mt-1">{errors.name}</p>
              )}
            </div>
            <div>
              <label className="label">
                <span className="label-text">Email</span>
              </label>
              <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                className={`input input-bordered w-full ${
                  errors.email ? "input-error" : ""
                }`}
              />
              {errors.email && (
                <p className="text-error text-sm mt-1">{errors.email}</p>
              )}
            </div>
            <div>
              <label className="label">
                <span className="label-text">Message</span>
              </label>
              <textarea
                name="message"
                value={formData.message}
                onChange={handleChange}
                className={`textarea textarea-bordered w-full ${
                  errors.message ? "textarea-error" : ""
                }`}
                rows={5}
              />
              {errors.message && (
                <p className="text-error text-sm mt-1">{errors.message}</p>
              )}
            </div>
            <div className="flex gap-2">
              <button
                type="submit"
                className="btn btn-primary flex-1"
                disabled={loading}
              >
                {loading ? (
                  <span className="loading loading-spinner"></span>
                ) : (
                  "Send"
                )}
              </button>
            </div>
          </form>
        </div>
      </section>
    </>
  );
}

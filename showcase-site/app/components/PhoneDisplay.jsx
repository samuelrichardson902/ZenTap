"use client";

export default function PhoneDisplay({ phoneImg, imgAlt }) {
  return (
    <div
      className={"mockup-phone border-primary border-[2px] scale-85 origin-top"}
    >
      <div className="mockup-phone-camera z-10"></div>
      <div className="mockup-phone-display">
        {/* Main Image */}
        <img
          src={phoneImg}
          alt={imgAlt}
          className="w-full h-full object-cover"
        />
      </div>
    </div>
  );
}

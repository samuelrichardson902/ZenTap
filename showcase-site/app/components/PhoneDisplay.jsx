"use client";

export default function PhoneDisplay({ phoneImg, imgAlt }) {
  return (
    <div className="mockup-phone border-primary border-[2px]">
      <div className="mockup-phone-camera"></div>
      <div className="mockup-phone-display relative">
        {/* Status Bar */}
        <div className="absolute top-0 left-0 right-0 h-10 bg-glass rounded-t-lg flex items-center justify-between pl-10 pr-8 z-20 text-white text-xs font-semibold">
          <div className="flex items-center space-x-1">17:38</div>
          <div className="flex items-center space-x-2">
            {/* 1. Cell Signal Bars*/}
            <div className="flex items-end space-x-0.25">
              <div className="w-1 h-1 bg-white rounded-sm"></div>
              <div className="w-1 h-2 bg-white rounded-sm"></div>
              <div className="w-1 h-3 bg-white rounded-sm"></div>
              <div className="w-1 h-4 bg-white rounded-sm"></div>
            </div>

            {/* 2. Network*/}
            <div>5G</div>

            {/* 3. Battery*/}
            <div className="relative flex items-center">
              {/* Battery Body */}
              <div className="w-6 h-3 bg-white rounded-sm"></div>

              {/* Battery Text */}
              <div className="absolute inset-0 flex items-center justify-center text-black text-[10px] font-bold">
                100
              </div>

              {/* Battery Terminal/Nub */}
              {/* Positioned absolutely to the right of the main body */}
              <div className="absolute left-full top-1/2 -translate-y-1/2 w-px h-1.5 bg-white rounded-sm ml-px"></div>
            </div>
          </div>
        </div>

        {/* Main Image */}
        <img
          src={phoneImg}
          alt={imgAlt}
          className="w-full h-full object-cover rounded-lg"
          style={{
            clipPath: "inset(38px 0 28px 0)",
            objectPosition: "center top",
            transform: "scale(1.1)",
          }}
        />

        {/* Home Indicator */}
        <div className="absolute bottom-3 left-1/2 transform -translate-x-1/2 w-32 h-1 bg-white rounded-full opacity-60 z-20"></div>
      </div>
    </div>
  );
}

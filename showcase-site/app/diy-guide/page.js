import Link from "next/link";
import { ArrowLeft } from "lucide-react";

export default function DiyGuidePage() {
  return (
    <main className="bg-base-100">
      <div className="container mx-auto px-4 py-16">
        <div className="max-w-3xl mx-auto">
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
            Follow these simple steps to set up your own NFC tag to work with ZenTap.
          </p>

          <div className="space-y-8">
            <Step
              number={1}
              title="Get a Compatible NFC Tag"
              description="You'll need an NTAG213, NTAG215, or NTAG216 chip. These are widely available and inexpensive online. We recommend a tag with at least 144 bytes of memory."
            />
            <Step
              number={2}
              title="Download an NFC Writing App"
              description="You'll need an app that can write data to an NFC tag. We recommend 'NFC Tools' for both Android and iOS, as it's free and easy to use."
            />
            <Step
              number={3}
              title="Write the Data to the Tag"
              description={
                <>
                  Open your NFC writing app and create a new record. You'll want to create a 'Custom URL' or 'URI' record. The value you need to write is:
                  <br />
                  <code className="bg-base-300 p-2 rounded-md my-2 inline-block">
                    zentap:focus
                  </code>
                  <br />
                  This is the custom URI that the ZenTap app listens for. Once you've entered this value, hold your NFC tag to the back of your phone to write the data.
                </>
              }
            />
            <Step
              number={4}
              title="You're All Set!"
              description="That's it! Your tag is now ready to use with ZenTap. Open the app, and you can start using your tag to activate and deactivate focus mode."
            />
          </div>
        </div>
      </div>
    </main>
  );
}

function Step({ number, title, description }) {
  return (
    <div className="flex items-start">
      <div className="flex-shrink-0">
        <span className="flex items-center justify-center h-12 w-12 rounded-full bg-primary text-primary-content font-bold text-xl">
          {number}
        </span>
      </div>
      <div className="ml-4">
        <h3 className="text-2xl font-bold">{title}</h3>
        <p className="mt-1 text-base-content/70">{description}</p>
      </div>
    </div>
  );
}

import Head from "next/head";

export default function Waitlist() {
  return (
    <>
      {/*stop robots following the link*/}
      <Head>
        <meta name="robots" content="noindex, nofollow" />
        <title>Waitlist</title>
      </Head>

      <section
        id="waitlist"
        className="relative min-h-screen bg-base-200 py-12 px-4 sm:px-6 lg:px-12 scroll-mt-20 flex items-center justify-center"
      >
        <div className="w-full max-w-4xl mx-auto">
          <div className="text-center mb-12">
            <h2 className="text-4xl md:text-5xl font-bold headerShadow">
              Waitlist
            </h2>
            <p className="mt-4 text-lg text-base-content/80">
              join the waitlist.
            </p>
            <a
              href="https://play.google.com/apps/internaltest/4700210753530599830"
              className="btn btn-primary rounded-lg"
              target="_blank"
              rel="noopener noreferrer"
            >
              Sign Up as a Tester
            </a>
          </div>
        </div>
      </section>
    </>
  );
}

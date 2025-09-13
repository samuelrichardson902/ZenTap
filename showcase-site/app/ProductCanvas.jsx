import ProductCanvas from "./ProductCanvas";

export default function Home() {
  return (
    // Wrap the page in a <main> tag to hold multiple sections
    <main>
      {/* This is your existing hero section */}
      <section className="relative h-screen flex items-center justify-center bg-base-100 overflow-hidden">
        {/* Layer 3: Header (Navbar) */}
        <header className="absolute top-0 w-full p-4 flex justify-between z-20">
          <h1 className="text-xl font-bold">MyProduct</h1>
          <nav className="space-x-4">
            <a href="#features">Features</a>
            <a href="#buy">Buy</a>
          </nav>
        </header>

        {/* Layer 2: Big Header Text (Hero Content) */}
        <div className="relative z-10 text-center p-4">
          <h2 className="text-5xl sm:text-7xl lg:text-8xl font-extrabold tracking-tight text-base-content">
            Your Big Headline
          </h2>
          <p className="mt-4 text-lg sm:text-xl text-base-content/80 max-w-2xl mx-auto">
            This is the catchy subtitle that explains what your product does.
          </p>
        </div>

        {/* Layer 1: Product Showcase (Background) */}
        <div className="absolute inset-0 w-full h-full z-0">
          <ProductCanvas />
        </div>
      </section>

      {/* --- NEW SPACER SECTION --- */}
      {/* This new section adds content below the hero, forcing the page to scroll.
        Giving it 'h-screen' makes it a very large spacer.
        The 'id="features"' makes your navbar link work.
      */}
      <section id="features" className="h-screen bg-base-200 p-12">
        <h2 className="text-4xl font-bold">Features Content Goes Here</h2>
        <p>This is the next part of your page.</p>
      </section>
    </main>
  );
}

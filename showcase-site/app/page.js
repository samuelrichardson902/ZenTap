import ProductCanvas from "./ProductCanvas";

export default function Home() {
  return (
    // This parent container centers the hero text (Layer 2) using flex.
    <section className="relative h-screen flex items-center justify-center bg-base-100 overflow-hidden mb-800">
      {/* Layer 3: Header (Navbar) */}
      {/* This stays on top of everything with the highest z-index. */}
      <header className="absolute top-0 w-full p-4 flex justify-between z-20">
        <h1 className="text-xl font-bold">MyProduct</h1>
        <nav className="space-x-4">
          <a href="#features">Features</a>
          <a href="#buy">Buy</a>
        </nav>
      </header>

      {/* Layer 2: Big Header Text (Hero Content) */}
      {/* This is the new content. It is centered by the parent flexbox. */}
      {/* We make it 'relative' and give it z-10 so it stacks above the canvas. */}
      <div className="relative z-10 text-center p-4">
        <h2 className="text-5xl sm:text-7xl lg:text-8xl font-extrabold tracking-tight text-base-content">
          Your Big Headline
        </h2>
        <p className="mt-4 text-lg sm:text-xl text-base-content/80 max-w-2xl mx-auto">
          This is the catchy subtitle that explains what your product does.
        </p>
      </div>

      {/* Layer 1: Product Showcase (Background) */}
      {/* We make this div 'absolute' and use 'inset-0' to fill the entire section. */}
      {/* z-0 places it at the very bottom of the stack, behind the text. */}
      <div className="absolute inset-0 w-full h-full z-0">
        <ProductCanvas />
      </div>
    </section>
  );
}

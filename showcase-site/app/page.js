import ProductCanvas from "./ProductCanvas";

export default function Home() {
  return (
    <section className="relative h-screen flex items-center justify-center bg-base-100">
      {/* Header */}
      <header className="absolute top-0 w-full p-4 flex justify-between z-20">
        <h1 className="text-xl font-bold">MyProduct</h1>
        <nav className="space-x-4">
          <a href="#features">Features</a>
          <a href="#buy">Buy</a>
        </nav>
      </header>

      {/* Product Showcase */}
      <div className="w-full h-full z-10">
        <ProductCanvas />
      </div>
    </section>
  );
}

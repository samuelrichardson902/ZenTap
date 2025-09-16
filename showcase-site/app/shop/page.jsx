import Link from "next/link";
import { ArrowLeft, ShoppingBag } from "lucide-react"; // Using a different icon

export default function ShopPage() {
  return (
    <main className="bg-base-100">
      <div className="container mx-auto px-4 py-16">
        <div className="max-w-md mx-auto text-center">
          {/* Back to Home button (at the top for easy access) */}
          <div className="mb-8">
            <Link href="/" className="btn btn-ghost">
              <ArrowLeft size={20} className="mr-2" />
              Back to Home
            </Link>
          </div>

          {/* The "Coming Soon" Card */}
          <div className="bg-base-200 p-8 py-12 rounded-lg boxShadow">
            <ShoppingBag size={64} className="text-primary mx-auto mb-6" />

            <h1 className="text-3xl lg:text-4xl font-bold mb-4 headerShadow">
              Shop Coming Soon
            </h1>

            <p className="text-lg text-base-content/80 mb-8">
              Our shop for pre-programmed supporter tags is not quite ready yet.
              We're working hard to get it live!
            </p>

            {/* A clear CTA to go back */}
            <Link href="/" className="btn btn-primary">
              Take Me Back Home
            </Link>
          </div>
        </div>
      </div>
    </main>
  );
}

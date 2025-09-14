export default function Footer() {
  return (
    <footer className="footer footer-horizontal footer-center bg-base-200 text-base-content rounded p-10">
      <nav className="grid grid-flow-col gap-4">
        <a href="/terms" className="link link-hover">
          Terms of use
        </a>
        <a href="/privacy" className="link link-hover">
          Privacy policy
        </a>
      </nav>
      <p>
        Copyright © {new Date().getFullYear()} - All right reserved by Sam
        Richardson
      </p>
    </footer>
  );
}

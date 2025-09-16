export default function NavBar() {
  return (
    <div className="navbar bg-base-100 fixed top-0 w-full z-50 shadow-lg h-20">
      <div className="navbar-start">
        <a href="#" className="btn btn-ghost text-3xl font-bold headerShadow">
          ZenTap
        </a>
      </div>
      <div className="navbar-center hidden lg:flex">
        <ul className="menu menu-horizontal px-1">
          <li>
            <a href="#howItWorks">How it Works</a>
          </li>
          <li>
            <a href="#features">Features</a>
          </li>
          <li>
            <a href="#getStarted">Get Started</a>
          </li>
          <li>
            <a href="#faq">FAQ's</a>
          </li>
          <li>
            <a href="#contact">Contact</a>
          </li>
        </ul>
      </div>
      <div className="navbar-end">
        <a href="#getStarted" className="btn border border-primary rounded-lg">
          Get It Now
        </a>
        <div className="dropdown lg:hidden">
          <div tabIndex={0} role="button" className="btn btn-ghost">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-5 w-5"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth="2"
                d="M4 6h16M4 12h16M4 18h16"
              />
            </svg>
          </div>
          <ul
            tabIndex={0}
            className="menu menu-sm dropdown-content mt-3 z-[1] p-2 shadow bg-base-100 rounded-box w-52 right-0"
          >
            <li>
              <a href="#howItWorks">How it Works</a>
            </li>
            <li>
              <a href="#features">Features</a>
            </li>
            <li>
              <a href="#getStarted">Get Started</a>
            </li>
            <li>
              <a href="#faq">FAQ's</a>
            </li>
            <li>
              <a href="#contact">Contact</a>
            </li>
          </ul>
        </div>
      </div>
    </div>
  );
}

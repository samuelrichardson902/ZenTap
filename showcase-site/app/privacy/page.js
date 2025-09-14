import Link from "next/link";
/* eslint-disable react/no-unescaped-entities */

export default function PrivacyPolicy() {
  return (
    <div className="min-h-screen bg-base-100 text-base-content/90">
      <div className="container mx-auto px-4 py-12 max-w-4xl">
        <h1 className="text-4xl font-bold mb-8 text-base-content">
          Privacy Policy
        </h1>
        <p className="mb-8 text-base-content/70">
          Last Updated: <strong>14/09/2025</strong>
        </p>

        <h2 className="text-2xl font-semibold mt-8 mb-4 text-base-content">
          1. Introduction
        </h2>
        <p className="mb-4 leading-relaxed">
          Welcome to Zentap. This Privacy Policy explains how we, Zentap (the
          developer), collect, use, and safeguard your information when you use
          our mobile application. We are committed to protecting your privacy.
        </p>

        <h2 className="text-2xl font-semibold mt-8 mb-4 text-base-content">
          2. Information We Collect
        </h2>
        <p className="mb-4 leading-relaxed">
          Zentap is designed to run entirely on your device. We do not collect
          or transmit any personal data to our servers. All data remains on your
          device. The app requires certain permissions to function correctly,
          which are explained below.
        </p>

        <h2 className="text-2xl font-semibold mt-8 mb-4 text-base-content">
          3. How We Use Your Information
        </h2>
        <p className="mb-4 leading-relaxed">
          The permissions granted to Zentap are used solely for the app's core
          functionality:
        </p>
        <ul className="list-disc pl-6 mb-4 space-y-2 leading-relaxed">
          <li>
            <strong>Accessibility Service:</strong> This permission is used to
            monitor which app is currently in the foreground. This allows Zentap
            to identify when a blocked app is opened and display the blocking
            screen.
          </li>
          <li>
            <strong>Query All Packages:</strong> This permission is used to get
            a list of all installed applications on your device. This list is
            displayed to you so you can select which apps to block.
          </li>
          <li>
            <strong>Display Over Other Apps:</strong> This permission is used to
            show the blocking screen on top of other applications.
          </li>
          <li>
            <strong>Alarms & Reminders:</strong> This permission is used to
            schedule automatic blocking sessions.
          </li>
        </ul>

        <h2 className="text-2xl font-semibold mt-8 mb-4 text-base-content">
          4. Data Storage
        </h2>
        <p className="mb-4 leading-relaxed">
          All data, including the list of blocked apps and your settings, is
          stored locally on your device using Android's SharedPreferences. This
          data is not accessible to other apps and is deleted when you uninstall
          Zentap.
        </p>

        <h2 className="text-2xl font-semibold mt-8 mb-4 text-base-content">
          5. Data Sharing
        </h2>
        <p className="mb-4 leading-relaxed">
          We do not share any of your data with third parties.
        </p>

        <h2 className="text-2xl font-semibold mt-8 mb-4 text-base-content">
          6. US Export Laws Compliance
        </h2>
        <p className="mb-4 leading-relaxed">
          Zentap does not use any non-standard encryption and is compliant with
          US export laws.
        </p>

        <h2 className="text-2xl font-semibold mt-8 mb-4 text-base-content">
          7. Changes to This Privacy Policy
        </h2>
        <p className="mb-4 leading-relaxed">
          We may update our Privacy Policy from time to time. We will notify you
          of any changes by posting the new Privacy Policy on this page.
        </p>

        <h2 className="text-2xl font-semibold mt-8 mb-4 text-base-content">
          8. Contact Us
        </h2>
        <p className="mb-4 leading-relaxed">
          If you have any questions about this Privacy Policy, please contact us
          at{" "}
          <a
            href="mailto:samuelrichardson902@gmail.com"
            className="text-primary hover:underline"
          >
            samuelrichardson902@gmail.com
          </a>
          .
        </p>

        <div className="mt-12 pt-8 border-t border-base-300">
          <Link href="/" className="btn btn-primary">
            ← Back to Home
          </Link>
        </div>
      </div>
    </div>
  );
}

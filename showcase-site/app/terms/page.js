import Link from "next/link";
/* eslint-disable react/no-unescaped-entities */

export default function TermsOfService() {
  return (
    <div className="min-h-screen bg-base-100 text-base-content/90">
      <div className="container mx-auto px-4 py-12 max-w-4xl">
        <h1 className="text-4xl font-bold mb-4 text-base-content">
          Terms of Service
        </h1>
        <p className="mb-8 text-base-content/70">
          Last Updated: <strong>14/09/2025</strong>
        </p>

        <div className="space-y-4">
          <p className="leading-relaxed">
            These Terms of Service ("Terms") create a binding legal contract
            between you ("you" or "User") and <strong>Samuel Richardson</strong>
            , the developer of Zentap ("we," "us," or "Zentap").
          </p>
          <p className="leading-relaxed">
            These Terms govern your download, installation, and use of the
            Zentap mobile application (the "Application"). These Terms
            supplement, and do not replace, any terms or conditions of the
            specific open-source license that governs the Application's source
            code.
          </p>
          <p className="leading-relaxed font-semibold">
            BY DOWNLOADING, INSTALLING, OR USING THE APPLICATION, YOU ARE
            AGREEING TO BE BOUND BY THESE TERMS. IF YOU DO NOT AGREE TO THESE
            TERMS, DO NOT DOWNLOAD, INSTALL, OR USE THE APPLICATION AND DELETE
            IT IMMEDIATELY.
          </p>
          <p className="leading-relaxed">
            You represent that you are over the age of 13.
          </p>
        </div>

        {/* --- Section 1 --- */}
        <h2 className="text-2xl font-semibold mt-8 mb-4 text-base-content">
          1. Open-Source License and Trademarks
        </h2>
        <div className="space-y-4">
          <p className="leading-relaxed">
            You acknowledge that the Application and its underlying source code
            are licensed under the terms of the <strong>MIT License</strong>{" "}
            (the "Open-Source License").
          </p>
          <p className="leading-relaxed">
            Your rights to copy, modify, and distribute the software are
            governed entirely by the terms of that Open-Source License. Nothing
            in these Terms of Service shall be construed to limit, restrict, or
            contradict the rights granted to you by the Open-Source License.
          </p>
          <p className="leading-relaxed">
            While the software code is provided under the Open-Source License,
            the name "Zentap" and any associated logos or branding are the
            exclusive trademarks of the developer and are <strong>not</strong>{" "}
            licensed under the Open-Source License. You may not use the "Zentap"
            name or logos to endorse or promote any derivative works without our
            express written permission.
          </p>
        </div>

        {/* --- Section 2 --- */}
        <h2 className="text-2xl font-semibold mt-8 mb-4 text-base-content">
          2. Application Functionality and User Responsibility
        </h2>
        <div className="space-y-4">
          <p className="leading-relaxed">
            You acknowledge that for the Application to function as intended,
            you must manually grant specific device permissions, including but
            not limited to the Accessibility Service, Query All Packages, and
            Display Over Other Apps permissions. The Application cannot perform
            its core function without these permissions.
          </p>
          <p className="leading-relaxed">
            You are solely responsible for managing these permissions and for
            all configurations, settings, and app-blocking lists you create
            within the Application.
          </p>
        </div>

        {/* --- Section 3 --- */}
        <h2 className="text-2xl font-semibold mt-8 mb-4 text-base-content">
          3. Privacy and Data
        </h2>
        <p className="leading-relaxed">
          The Application operates entirely on your device. As outlined in our{" "}
          <a href="/privacy" className="text-primary hover:underline">
            Privacy Policy
          </a>
          , we do not collect, store, or transmit any of your personal data, app
          lists, or settings. All data remains locally on your device and is
          deleted upon uninstallation. The Privacy Policy is incorporated by
          reference into these Terms.
        </p>

        {/* --- Section 4 --- */}
        <h2 className="text-2xl font-semibold mt-8 mb-4 text-base-content">
          4. Fees and Payments
        </h2>
        <p className="leading-relaxed">
          The Application may be offered for a one-time purchase fee, as a
          recurring subscription, or as a free-to-use application. All billing
          and financial transactions are processed through the third-party app
          store provider from which you downloaded the Application (e.g., Google
          Play Store). Any payment terms, refunds, or billing disputes are
          subject to the terms and conditions of that app store provider, and
          you must address such issues directly with them.
        </p>

        {/* --- Section 5 --- */}
        <h2 className="text-2xl font-semibold mt-8 mb-4 text-base-content">
          5. DISCLAIMER OF WARRANTIES (IMPORTANT)
        </h2>
        <div className="space-y-4">
          <p className="leading-relaxed font-semibold">
            Consistent with the terms of most open-source licenses, the
            Application is provided "AS IS" AND "AS AVAILABLE," AT YOUR OWN
            RISK, WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED.
          </p>
          <p className="leading-relaxed">
            TO THE FULLEST EXTENT PERMITTED BY LAW, ZENTAP EXPLICITLY DISCLAIMS
            ANY AND ALL WARRANTIES, WHETHER EXPRESS, IMPLIED, OR STATUTORY,
            INCLUDING (BUT NOT LIMITED TO) ANY IMPLIED WARRANTIES OF
            MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, TITLE, AND
            NON-INFRINGEMENT.
          </p>
          <p className="leading-relaxed">
            ZENTAP MAKES NO WARRANTY THAT THE APPLICATION WILL MEET YOUR
            REQUIREMENTS, BE FREE OF BUGS, ERRORS, OR INTERRUPTIONS, OR THAT IT
            WILL GUARANTEE YOU WILL NOT ACCESS BLOCKED APPLICATIONS. THE
            APPLICATION IS A MONITORING AID AND CAN BE CIRCUMVENTED BY DISABLING
            ITS PERMISSIONS.
          </p>
        </div>

        {/* --- Section 6 --- */}
        <h2 className="text-2xl font-semibold mt-8 mb-4 text-base-content">
          6. LIMITATION OF LIABILITY (IMPORTANT)
        </h2>
        <div className="space-y-4">
          <p className="leading-relaxed">
            TO THE MAXIMUM EXTENT PERMITTED BY APPLICABLE LAW, IN NO EVENT SHALL
            ZENTAP (OR ITS DEVELOPERS OR CONTRIBUTORS) BE LIABLE FOR ANY
            INDIRECT, INCIDENTAL, SPECIAL, CONSEQUENTIAL, OR PUNITIVE DAMAGES,
            OR ANY LOSS OF PROFITS, REVENUE, OR DATA, ARISING FROM OR IN
            CONNECTION WITH YOUR USE OF (OR INABILITY TO USE) THE APPLICATION OR
            THESE TERMS.
          </p>
          <p className="leading-relaxed">
            THIS INCLUDES, BUT IS NOT LIMITED TO, ANY DAMAGES RESULTING FROM
            BUGS OR MALFUNCTIONS; ANY FAILURE TO BLOCK AN APP; OR ANY DAMAGE TO
            YOUR DEVICE, EVEN IF WE HAVE BEEN ADVISED OF THE POSSIBILITY OF SUCH
            DAMAGE. THIS LIMITATION OF LIABILITY IS A FUNDAMENTAL PART OF THE
            BARGAIN BETWEEN YOU AND ZENTAP, IN LINE WITH THE PROVISION OF THIS
            SOFTWARE ON AN OPEN-SOURCE BASIS.
          </p>
          <p className="leading-relaxed">
            IN NO EVENT SHALL ZENTAP'S TOTAL AGGREGATE LIABILITY TO YOU FOR ALL
            CLAIMS RELATING TO THE APPLICATION EXCEED THE TOTAL AMOUNT (IF ANY)
            YOU PAID TO ZENTAP FOR THE APPLICATION.
          </p>
        </div>

        {/* --- Section 7 --- */}
        <h2 className="text-2xl font-semibold mt-8 mb-4 text-base-content">
          7. Indemnification
        </h2>
        <p className="leading-relaxed">
          You agree to indemnify and hold harmless Zentap and its developers
          from and against any and all claims, disputes, demands, liabilities,
          damages, and costs (including reasonable legal fees) arising out of or
          in any way connected with: (a) your use of the Application in
          violation of these Terms, (b) your violation of any applicable law, or
          (c) your violation of the rights of any third party.
        </p>

        {/* --- Section 8 --- */}
        <h2 className="text-2xl font-semibold mt-8 mb-4 text-base-content">
          8. Changes to These Terms
        </h2>
        <p className="leading-relaxed">
          We reserve the right to modify these Terms at any time. If we make
          material changes, we will provide notice through the Application or by
          other appropriate means. Your continued use of the Application after
          such notice constitutes your acceptance of the modified Terms. If you
          do not agree to the new terms, you must stop using the Application and
          delete it.
        </p>

        {/* --- Section 9 --- */}
        <h2 className="text-2xl font-semibold mt-8 mb-4 text-base-content">
          9. Governing Law and Jurisdiction
        </h2>
        <div className="space-y-4">
          <p className="leading-relaxed">
            These Terms, and any dispute or claim arising out of or in
            connection with them, shall be governed by and construed in
            accordance with the laws of <strong>England and Wales</strong>.
          </p>
          <p className="leading-relaxed">
            You irrevocably agree that the courts of{" "}
            <strong>England and Wales</strong> shall have exclusive jurisdiction
            to settle any dispute or claim that arises out of or in connection
            with these Terms.
          </p>
        </div>

        {/* --- Section 10 --- */}
        <h2 className="text-2xl font-semibold mt-8 mb-4 text-base-content">
          10. General Terms
        </h2>
        <ul className="list-disc pl-6 space-y-2 leading-relaxed">
          <li>
            <strong>Entire Agreement:</strong> These Terms (including the
            Privacy Policy and the separate Open-Source License) constitute the
            entire agreement between you and Zentap regarding the Application.
          </li>
          <li>
            <strong>Severability:</strong> If any provision of these Terms is
            held to be invalid or unenforceable, that provision will be enforced
            to the maximum extent permissible, and the remaining provisions will
            remain in full force and effect.
          </li>
          <li>
            <strong>Contact:</strong> If you have any questions about these
            Terms, please contact us at samuelrichardson902@gmail.com.
          </li>
        </ul>

        {/* --- Footer Link --- */}
        <div className="mt-12 pt-8 border-t border-base-300">
          <Link href="/" className="btn btn-primary">
            ← Back to Home
          </Link>
        </div>
      </div>
    </div>
  );
}

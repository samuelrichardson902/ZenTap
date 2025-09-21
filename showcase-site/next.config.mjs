/** @type {import('next').NextConfig} */
const nextConfig = {
  async redirects() {
    return [
      {
        source: "/tester-signup",
        destination:
          "https://play.google.com/apps/internaltest/4700210753530599830",
        permanent: false,
      },
    ];
  },
};

export default nextConfig;

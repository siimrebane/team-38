import type { NextConfig } from "next";

const nextConfig: NextConfig = {
    typescript: {
        ignoreBuildErrors: true,
    },
    output: 'export',
    images: {
        unoptimized: true,
    },
    trailingSlash: true,
    // Dev-only: proxy /api requests to the backend running on localhost:8080
    async rewrites() {
        return [
            { source: '/api/:path*', destination: 'http://localhost:8080/api/:path*' },
        ];
    },
};

export default nextConfig;

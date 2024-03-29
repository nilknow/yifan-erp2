/** @type {import('next').NextConfig} */
const nextConfig = {
    env: {
        CURRENT_ENV: 'local',
        BACKEND_URL: 'http://hei.test.com'
    },
    output: 'export',
    distDir: 'dist',
    async rewrites() {
        return this.env.CURRENT_ENV === 'local'
            ?
            [
                {
                    source: '/api/:path*',
                    destination: `${this.env.BACKEND_URL}/api/:path*`
                }
            ]
            : []
    }
};

export default nextConfig;

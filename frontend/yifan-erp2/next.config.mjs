/** @type {import('next').NextConfig} */
const nextConfig = {
    // env: {
    //     CURRENT_ENV: 'local',
    //     BACKEND_URL: ''
    // },
    // distDir: 'dist',
    // async rewrites() {
    //     return this.env.CURRENT_ENV === 'local'
    //         ?
    //         [
    //             {
    //                 source: '/api/:path*',
    //                 destination: `${this.env.BACKEND_URL}/api/:path*`
    //             }
    //         ]
    //         : []
    // }
    async headers() {
        return [
            {
                source: '/(.*)',
                headers: [
                    {
                        key: 'Access-Control-Allow-Origin',
                        value: '/'
                    },
                ],
            },
        ];
    },
};

export default nextConfig;

const { defineConfig } = require('@vue/cli-service')
const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin
const CompressionWebpackPlugin = require('compression-webpack-plugin')
const AutoImport = require('unplugin-auto-import/webpack')
const Components = require('unplugin-vue-components/webpack')
const { ElementPlusResolver } = require('unplugin-vue-components/resolvers')

module.exports = defineConfig({
    transpileDependencies: true,
    devServer: {
        proxy: {
            // 配置跨域
            '/req1/': {
                target: 'http://127.0.0.1:4080/',
                pathRewrite: { '^/req1/': '' },
                ws: true,
                changeOrigin: true
            },
            '/req2/': {
                target: 'http://127.0.0.1:8000/',
                pathRewrite: { '^/req2/': '' },
                ws: true,
                changeOrigin: true
            }
        }
    },
    configureWebpack: {
        externals: {
            echarts: 'echarts',
            axios: 'axios',
            'js-cookie': 'Cookies'
        },
        plugins: [
            new BundleAnalyzerPlugin({
                openAnalyzer: false
            }),
            new CompressionWebpackPlugin({
                test: /\.(js|css)$/,
                algorithm: 'gzip',
                threshold: 10240,
                deleteOriginalAssets: false,
                minRatio: 0.8
            }),
            AutoImport({
                resolvers: [ElementPlusResolver()]
            }),
            Components({
                resolvers: [ElementPlusResolver()]
            })
        ]
    }
})

var webpack = require('webpack');
var config = require('./scalajs.webpack.config');
var merge = require("webpack-merge");

module.exports = merge(config, {
    entry: {
        vendor: ['react', 'react-dom'],
    },
    output: {
        filename: '[name].[chunkhash].bundle.js',
        chunkFilename: '[name].[chunkhash].bundle.js',
    },
    mode: 'production',
    optimization: {
      splitChunks: {
        cacheGroups: {
          vendor: {
            chunks: 'initial',
            name: 'vendor',
            test: 'vendor',
            enforce: true
          },
        }
      },
      runtimeChunk: true
    }
});

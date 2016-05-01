'use strict';


module.exports = function (grunt) {
    // simplifies loading grunt tasks
    require('load-grunt-tasks')(grunt);

    grunt.initConfig({
        clean: {
            main: ['dist']
        },
        copy: {
            main : {
                files: [
                    /*{
                        expand: true,
                        src: [ 'public/bower_components/!**' ],
                        dest: 'dist/'
                    },*/
                    {
                        expand: true,
                        src: [ 'public/**', 'index.html' ],
                        dest: 'dist/'
                    }
                ]
            }

        },
        uglify : {
            options: {
                mangle: false,
                expand: true,
                cwd : '.'
            },
            my_target: {
                files: {
                    'dist/client.min.js': ['./public/javascripts/**/*.js']
                }
            }
        }
    });

    //grunt.registerTask('default', function () {
    //    console.log('I\'m running grunt');
    //});

    grunt.registerTask('default', ['copy']);
};
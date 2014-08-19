module.exports = function(grunt) {

	var config = {
		src: 'src/main/assets/less/',
		dest: 'src/main/webapp/css/'
	};

	grunt.initConfig({
		config: config,

		less: {
			main: {
				options: {
					sourceMap: true,
					outputSourceFiles: true
				},
				files:[{
					expand: true,
					cwd: '<%= config.src %>',
					src: ['**/*.less'],
					dest: '<%= config.dest %>',
					ext: '.css'
				}]
			}
		}
    });

    grunt.loadNpmTasks('grunt-contrib-less');

	grunt.registerTask('default', ['less']);

};
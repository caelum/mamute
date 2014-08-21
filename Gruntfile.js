module.exports = function(grunt) {

	var config = {
		src: 'src/main/assets/less/',
		dest: 'src/main/webapp/css/mamute/'
	};

	grunt.initConfig({
		config: config,

		clean: ["<%= config.dest %>"],
		
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
		},
		
		watch: {
			less: {
				files: ['<%= config.src %>/**/*.less'],
				tasks: ['less'],
				options: {
					spawn: false
				}
			}
		}
    });
	
	['contrib-clean',
	 'contrib-less',
	 'contrib-watch'
	].forEach(function(plugin) {
		grunt.loadNpmTasks('grunt-' + plugin);
	});

	grunt.registerTask('default', ['clean', 'less', 'watch']);

};
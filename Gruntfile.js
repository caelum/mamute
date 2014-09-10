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

		useminPrepare: {
			html: 'src/main/webapp/WEB-INF/{jsp,tags}/**/*.{jsp,jspf,tag}',
			options: {
				dest: 'src/main/webapp/',
				root: 'src/main/webapp/'
			}
		},

		usemin: {
			html: ['src/main/webapp/WEB-INF/{jsp,tags}/**/*.{jsp,jspf,tag}'],
			options: {

				assetsDirs: ['<%= config.dest %>']
			}
		},

		uglify: { 
	      main: {
	        expand: true,
	        cwd: 'src/main/webapp/js/',
	        src: ['**/*.js', '!**/*.min.js'],
	        dest: 'src/main/webapp/js/'
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
	 'contrib-watch',
	 'contrib-concat',
	 'contrib-cssmin',
	 'contrib-uglify',
	 'usemin'
	].forEach(function(plugin) {
		grunt.loadNpmTasks('grunt-' + plugin);
	});

	grunt.registerTask('default', ['clean', 'less']);
	grunt.registerTask('build', ['default', 'useminPrepare', 'concat:generated', 'cssmin:generated', 'uglify', 'usemin']);
	grunt.registerTask('run', ['default', 'watch']);

};
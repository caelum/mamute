module.exports = function(grunt) {

	var config = {
		src: 'src/main/assets/less/',
		root: 'src/main/webapp/'
	};

	grunt.initConfig({
		config: config,

		clean: ["<%= config.root %>/css/mamute", "<%= config.root %>/js/mamute"],
		
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
					dest: '<%= config.root %>/css/mamute/',
					ext: '.css'
				}]
			}
		},

		useminPrepare: {
			html: '<%= config.root %>/WEB-INF/{jsp,tags}/**/*.{jsp,jspf,tag}',
			options: {
				dest: '<%= config.root %>',
				root: '<%= config.root %>'
			}
		},

		usemin: {
			html: ['<%= config.root %>/WEB-INF/{jsp,tags}/**/*.{jsp,jspf,tag}'],
			options: {

				assetsDirs: ['<%= config.root %>/css/mamute']
			}
		},

		uglify: { 
	      main: {
	        expand: true,
	        cwd: '<%= config.root %>/js/',
	        src: ['**/*.js', '!**/*.min.js'],
	        dest: '<%= config.root %>/js/'
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
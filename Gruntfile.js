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
				dirs: ['<%= config.root %>'],
				assetsDirs: ['<%= config.root %>']
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

	    filerev: {
	    	options: {
		        encoding: 'utf8',
		        algorithm: 'md5',
		        length: 8
		    },
		    source: {
		    	files: [{
		    		src: ['<%= config.root %>/{js,css}/mamute/*.{js,css}']
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
	 'contrib-watch',
	 'contrib-concat',
	 'contrib-cssmin',
	 'contrib-uglify',
	 'filerev',
	 'usemin'
	].forEach(function(plugin) {
		grunt.loadNpmTasks('grunt-' + plugin);
	});

	grunt.registerTask('default', ['clean', 'less']);
	grunt.registerTask('build', ['default', 'useminPrepare', 'concat:generated', 'cssmin:generated', 
									'uglify', 'filerev', 'usemin']);
	grunt.registerTask('run', ['default', 'watch']);

};
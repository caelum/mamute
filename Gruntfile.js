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
				assetsDirs: ['<%= config.root %>'],
				blockReplacements: {
					css: function (block) {
						return '<link rel="stylesheet" href="${contextPath}' + block.dest + '"/>';
					},
					js: function (block) {
						return '<script src="${contextPath}' + block.dest + '"></script>';
					}
				}
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

	//remaps the filerev data to match the prefixed filenames
	grunt.registerTask('remapFilerev', function(){
		var root = grunt.config().config.root;
		var summary = grunt.filerev.summary;
		var fixed = {};

		for(key in summary){
			if(summary.hasOwnProperty(key)){
				var orig = key.replace(root, root+'${contextPath}/');
				var revved = summary[key].replace(root, root+'${contextPath}/');
				fixed[orig] = revved;
			}
		}

		grunt.filerev.summary = fixed;
	});

	grunt.registerTask('default', ['clean', 'less']);
	grunt.registerTask('build', ['default', 'useminPrepare', 'concat:generated', 'cssmin:generated',
									'uglify', 'filerev', 'remapFilerev', 'usemin']);
	grunt.registerTask('run', ['default', 'watch']);

};
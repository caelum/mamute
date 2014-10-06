module.exports = function(grunt) {

	var config = {
		assets: 'src/main/webapp/assets/',
		webapp: 'src/main/webapp/'
	};

	grunt.initConfig({
		config: config,

		clean: {
			before: ["<%= config.webapp %>/{css, js, imgs, font}/"],
			after: ["<%= config.assets %>", "<%= config.webapp %>/css/generated-css/"]
		},
		
		less: {
			main: {
				files:[{
					expand: true,
					cwd: '<%= config.assets %>/less',
					src: ['**/*.less'],
					dest: '<%= config.webapp %>/css/generated-css/',
					ext: '.css'
				}]
			}
		},

		useminPrepare: {
			html: '<%= config.webapp %>/WEB-INF/{jsp,tags}/**/*.{jsp,jspf,tag}',
			options: {
				dest: '<%= config.webapp %>',
				root: '<%= config.webapp %>'
			}
		},

		usemin: {
			html: ['<%= config.webapp %>/WEB-INF/{jsp,tags}/**/*.{jsp,jspf,tag}'],
			options: {
				dirs: ['<%= config.webapp %>'],
				assetsDirs: ['<%= config.webapp %>'],
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
	        cwd: '<%= config.webapp %>/js/',
	        src: ['**/*.js', '!**/*.min.js'],
	        dest: '<%= config.webapp %>/js/'
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
		    		src: ['<%= config.webapp %>/{js,css}/*.{js,css}']
		    	}]
		    }
	    },

	    copy: {
		  js: {
		  	expand: true,
		  	cwd: '<%= config.assets %>/grunt-ignore/js/',
		    src: '**',
		    dest: '<%= config.webapp %>/js/grunt-ignore'
		  },
		  css: {
		  	expand: true,
		  	cwd: '<%= config.assets %>/grunt-ignore/css/',
		    src: '**',
		    dest: '<%= config.webapp %>/css/'
		  },
		  font: {
		  	expand: true,
		  	cwd: '<%= config.assets %>/grunt-ignore/font/',
		    src: '**',
		    dest: '<%= config.webapp %>/font/'
		  },
		  img: {
		  	expand: true,
		  	cwd: '<%= config.assets %>/grunt-ignore/imgs/',
		    src: '**',
		    dest: '<%= config.webapp %>/imgs/',
		  }
		},
		
		watch: {
			less: {
				files: ['<%= config.assets %>/less/**/*.less'],
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
	 'contrib-copy',
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

	grunt.registerTask('default', ['clean:before', 'less', 'copy']);
	grunt.registerTask('build', ['default', 'useminPrepare', 'concat:generated', 'cssmin:generated', 
									'uglify', 'filerev', 'remapFilerev', 'usemin', 'clean:after']);
	grunt.registerTask('run', ['default', 'watch']);

};
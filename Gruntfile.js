/* 
/webapp/assets -> before build
	 -> /grunt-ignore -> assets that will just be copied to production without any modifications (modify them freely)
     -> /js -> js at development (modify them freely)
     -> /less -> less (modify them freely)
     -> /generated-css -> css processed from less + ignored css (these are generated automatically, please don't touch them)
/webapp/{css/js/imgs/font} -> after all the build, production assets (these are generated automatically, please don't touch them)
*/

module.exports = function(grunt) {

	var config = {
		generatedCss: 'src/main/webapp/assets/generated-css/',
		assets: 'src/main/webapp/assets/',
		webapp: 'src/main/webapp/',
		ignored: 'src/main/webapp/assets/grunt-ignore/'
	};

	grunt.initConfig({
		config: config,

		clean: {
			before: ["<%= config.webapp %>/{css,js,imgs,font}/"],
			after: ["<%= config.assets %>"]
		},
		
		less: {
			main: {
				files:[{
					expand: true,
					cwd: '<%= config.assets %>/less',
					src: ['**/*.less'],
					dest: '<%= config.generatedCss %>',
					ext: '.css'
				}]
			}
		},

	    copy: {
	    	ignored: {
			  files: [
				  {
				  	expand: true,
				  	cwd: '<%= config.ignored %>/js/',
				    src: ['**'],
				    dest: '<%= config.webapp %>/js/'
				  },
				  {
				  	expand: true,
				  	cwd: '<%= config.ignored %>/css/',
				    src: ['**'],
				    dest: '<%= config.webapp %>/css/'
				  },
				  {
				  	expand: true,
				  	cwd: '<%= config.ignored %>/font/',
				    src: ['**'],
				    dest: '<%= config.webapp %>/font/'
				  },
				  {
				  	expand: true,
				  	cwd: '<%= config.ignored %>/imgs/',
				    src: ['**'],
				    dest: '<%= config.webapp %>/imgs/',
				  }
				]
	    	},
			special:{
				files:[
					{
						expand: true,
						cwd: '<%= config.webapp %>/imgs',
						src: ['**'],
						dest: '<%= config.webapp %>/css/imgs'
					},
					{
						expand: true,
						cwd: '<%= config.webapp %>/imgs',
						src: ['**'],
						dest: '<%= config.webapp %>/assets/imgs'
					}
				]
			}
		},
		
		useminPrepare: {
			html: '<%= config.webapp %>/WEB-INF/{jsp,tags}/**/*.{jsp,jspf,tag}',
			options: {
				root: '<%= config.webapp %>',
				dest: '<%= config.webapp %>'
			}
		},

	    filerev: {
	    	options: {
		        encoding: 'utf8',
		        algorithm: 'md5',
		        length: 8
		    },
		    source: {
		    	files: [
			    	{src: ['<%= config.webapp %>/js/**/*.js']},
			    	{src: ['<%= config.webapp %>/css/**/*.css']}
		    	]
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

	//remaps the filerev data to match the prefixed filenames
	grunt.registerTask('remapFilerev', function(){
		var root = grunt.config().config.webapp;
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


	grunt.registerTask('default', ['clean:before', 'less', 'copy:ignored']);
	grunt.registerTask('build', ['default', 'useminPrepare', 'concat:generated', 'cssmin:generated', 
									'uglify', 'filerev', 'remapFilerev', 'usemin', 'clean:after']);
	grunt.registerTask('run', ['default', 'copy:special', 'watch']);

};
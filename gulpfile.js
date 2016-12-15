var gulp = require('gulp'),
    concat = require('gulp-concat');

var srcArr = [
  'src/main/resources/static/app/vendors/angular.min.js',
  'src/main/resources/static/app/vendors/zepto.min.js',
  'src/main/resources/static/app/vendors/**/*.js',
  'src/main/resources/static/app/app.js',
  "src/main/resources/static/app/**/*.js"
];

gulp.task('concatJS', function () {
  return gulp.src(srcArr)
      .pipe(concat('minify.js'))
      .pipe(gulp.dest('src/main/resources/static/minify'));
});


gulp.task('concatCSS', function () {
  return gulp.src(['src/main/resources/static/css/vendors/*.css', 'src/main/resources/static/css/**/*.css'])
    .pipe(concat('minify.css'))
    .pipe(gulp.dest('src/main/resources/static/minify'));
});

gulp.task('build', ['concatJS', 'concatCSS']);

var watcher = gulp.watch(['src/main/resources/static/app/**/*.js','src/main/resources/static/css/**/*.css'], ['build']);
watcher.on('change', function (event) {
  console.log('File ' + event.path + ' was ' + event.type + ', running tasks...');
});

gulp.task('default', function () {
  //concat js
  gulp.start('build');
});
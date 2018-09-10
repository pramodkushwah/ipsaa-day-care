const gulp = require('gulp');
const concat = require('gulp-concat');
const rename = require('gulp-rename');
const minifyCSS = require('gulp-minify-css');
const minifyJS = require('gulp-minify');
const uglify = require('gulp-uglify-es').default;
const clean = require('gulp-clean');
const gulpSequence = require('gulp-sequence');
const runSequence = require('run-sequence');
const inject = require('gulp-inject');
const uuid = require('uuid/v4');
const eventStream = require('event-stream');
const series = require('stream-series');
const git = require('gulp-git');

const misCss = [
    'assets/css/bootstrap.min.css',
    'assets/css/bootstrap-datetimepicker.min.css',
    'assets/css/material-dashboard.css',
    'assets/css/font-awesome.min.css',
    'assets/css/all.css',
    'assets/css/mis.css'
];
const ppCss = [
    'assets/css/bootstrap.min.css',
    'assets/css/bootstrap-datetimepicker.min.css',
    'assets/css/material-dashboard.css',
    'assets/css/font-awesome.min.css',
    'assets/css/portal.css'
];

const commonScripts = [
    'assets/js/jquery-3.1.1.min.js',
    'assets/js/jquery-ui.min.js',
    'assets/js/moment.min.js',
    'assets/js/bootstrap.min.js',
    'assets/js/material.min.js',
    'assets/js/perfect-scrollbar.jquery.min.js',
    'assets/js/jquery.validate.min.js',
    'assets/js/chartist.min.js',
    'assets/js/jquery.bootstrap-wizard.js',
    'assets/js/bootstrap-notify.js',
    'assets/js/jquery-jvectormap.js',
    'assets/js/nouislider.min.js',
    'assets/js/jquery.select-bootstrap.js',
    'assets/js/jquery.datatables.js',
    'assets/js/sweetalert2.js',
    'assets/js/jasny-bootstrap.min.js',
    'assets/js/fullcalendar.min.js',
    'assets/js/jquery.tagsinput.js',
    'assets/js/material-dashboard.js',

    'components/angular/angular.js',
    'components/angular-aria/angular-aria.js',
    'components/angular-animate/angular-animate.js',
    'components/angular-ui-router/release/angular-ui-router.js',
    'components/ngstorage/ngStorage.js',
    'components/angular-jwt/dist/angular-jwt.js',
    'components/angular-xeditable/dist/js/xeditable.js',
    'components/eonasdan-bootstrap-datetimepicker/build/js/bootstrap-datetimepicker.min.js',
    'components/ng-file-upload/ng-file-upload-all.min.js',
    'components/file-saver/FileSaver.min.js'
];
const misAppScripts = [
    'mis/app/app.js',
    'mis/app/config.js',
    'mis/app/directives/file.directive.js',
    'mis/app/services/auth.service.js',
    'mis/app/services/inquiry.service.js',
    'mis/app/services/notification.service.js',
    'mis/app/services/fileUpload.service.js',
    'mis/app/services/studentfee.service.js',

    'mis/app/controllers/user/login.controller.js',
    'mis/app/controllers/home.controller.js',
    'mis/app/controllers/menu.controller.js',
    'mis/app/controllers/student.controller.js',
    'mis/app/controllers/center.controller.js',
    'mis/app/controllers/staff.controller.js',
    'mis/app/controllers/program.controller.js',
    'mis/app/controllers/centerfeemanagement.controller.js',
    'mis/app/controllers/studentfeemanagement.controller.js',
    'mis/app/controllers/studentattendance.controller.js',
    'mis/app/controllers/feeslip.controller.js',
    'mis/app/controllers/feepayment.controller.js',
    'mis/app/controllers/staffattendance.controller.js',
    'mis/app/controllers/usermanagement.controller.js',
    'mis/app/controllers/nav.controller.js',
    'mis/app/controllers/studentmessage.controller.js',
    'mis/app/controllers/staffmessage.controller.js',
    'mis/app/controllers/rolemanagement.controller.js',
    'mis/app/controllers/importdata.controller.js',
    'mis/app/controllers/groupactivity.controller.js',
    'mis/app/controllers/salary.controller.js',
    'mis/app/controllers/monthlysalary.controller.js',
    'mis/app/controllers/staffleaves.controller.js',
    'mis/app/controllers/support.controller.js',
    'mis/app/controllers/holiday.controller.js',
    'mis/app/controllers/sharingsheet.controller.js',
    'mis/app/controllers/foodmenu.controller.js',
    'mis/app/controllers/hygiene.controller.js',
    'mis/app/controllers/studentapprovals.controller.js',
    'mis/app/controllers/studentapproval.controller.js',
    'mis/app/controllers/staffapproval.controller.js',
    'mis/app/controllers/staffapprovals.controller.js',
    'mis/app/controllers/inquiry/inquiry.controller.js',
    'mis/app/controllers/inquiry/inquiries.controller.js',
    'mis/app/controllers/inquiry/followup.controller.js',
    'mis/app/controllers/report/feereport.controller.js',
    'mis/app/controllers/report/studentattendancereport.controller.js',
    'mis/app/controllers/report/staffattendancereport.controller.js',
    'mis/app/controllers/report/inquiryreport.controller.js',
    'mis/app/controllers/report/collectionfeereport.controller.js',
    'mis/app/controllers/report/staffsalarymonthlyreport.controller.js',
    'mis/app/controllers/report/staffreport.controller.js',
    'mis/app/controllers/staffattendancelogs.controller.js',
    'mis/app/controllers/staffleaves.controller.js',
    'mis/app/controllers/gallery/gallery.controller.js'
];
const ppAppScripts = [
    'pp/app/app.js',
    'pp/app/config.js',

    'pp/app/services/auth.service.js',

    'pp/app/controllers/login.controller.js',
    'pp/app/controllers/user.controller.js',
    'pp/app/controllers/nav.controller.js',
    'pp/app/controllers/profile.controller.js',
    'pp/app/controllers/attendance.controller.js',
    'pp/app/controllers/fee.controller.js',
    'pp/app/controllers/feesuccess.controller.js',
    'pp/app/controllers/feefailure.controller.js',
    'pp/app/controllers/activity.controller.js',
    'pp/app/controllers/support.controller.js',
    'pp/app/controllers/foodmenu.controller.js',
    'pp/app/controllers/logout.controller.js',
    'pp/app/controllers/sharingsheet.controller.js',
    'pp/app/controllers/gallery.controller.js'
];
const checkoutAppScripts = [
    "pp/checkout/app.js",
    "pp/checkout/config.js",

    "pp/checkout/controller/checkout.controller.js",
    "pp/checkout/controller/failure.controller.js",
    "pp/checkout/controller/success.controller.js"
];

function getPpCssStream() {
    return gulp.src(ppCss)
        .pipe(concat(uuid() + ".css"))
        .pipe(gulp.dest('dist/css'));
}

function getMisCssStream() {
    return gulp.src(misCss)
        .pipe(concat(uuid() + ".css"))
        .pipe(gulp.dest('dist/css'));
}

function getCommonScriptsStream() {
    return gulp.src(commonScripts)
        .pipe(concat(uuid() + ".js"))
        .pipe(uglify({mangle: false})
            .on('error', function (e) {
                console.log(e);
            }))
        .pipe(gulp.dest('dist/js'));
}

function getMisScriptsStream() {
    return gulp.src(misAppScripts)
        .pipe(concat(uuid() + ".js"))
        .pipe(uglify({mangle: false})
            .on('error', function (e) {
                console.log(e);
            }))
        .pipe(gulp.dest('dist/js'));
}

function getPpScriptsStream() {
    return gulp.src(ppAppScripts)
        .pipe(concat(uuid() + ".js"))
        .pipe(uglify({mangle: false})
            .on('error', function (e) {
                console.log(e);
            }))
        .pipe(gulp.dest('dist/js'));
}

function getCheckoutScriptsStream() {
    return gulp.src(checkoutAppScripts)
        .pipe(concat(uuid() + ".js"))
        .pipe(uglify({mangle: false})
            .on('error', function (e) {
                console.log(e);
            }))
        .pipe(gulp.dest('dist/js'));
}

gulp.task('copy-fonts', function () {
    return gulp.src('fonts/**.*')
        .pipe(gulp.dest('dist/fonts'));
});

gulp.task('build-checkout-index', function () {
    var ppCssStream = getPpCssStream();
    var commonScriptsStream = getCommonScriptsStream();
    var checkoutScriptsStream = getCheckoutScriptsStream();
    return gulp.src('pp/gulp-template/checkout.html')
        .pipe(inject(series([ppCssStream, commonScriptsStream, checkoutScriptsStream])))
        .pipe(gulp.dest('pp'));
});

gulp.task('build-mis-index', function () {
    var misCssStream = getMisCssStream();
    var commonScriptsStream = getCommonScriptsStream();
    var misScriptsStream = getMisScriptsStream();
    return gulp.src('mis/gulp-template/index.html')
        .pipe(inject(series([misCssStream, commonScriptsStream, misScriptsStream], {read: false})))
        .pipe(gulp.dest('mis'));
});

gulp.task('build-pp-index', function () {
    var ppCssStream = getPpCssStream();
    var commonScriptsStream = getCommonScriptsStream();
    var ppScriptsStream = getPpScriptsStream();
    return gulp.src('pp/gulp-template/index.html')
        .pipe(inject(series([ppCssStream, commonScriptsStream, ppScriptsStream], {read: false})))
        .pipe(gulp.dest('pp'));
});

gulp.task('delete-dist', function () {
    return gulp.src(['dist'], {read: false})
        .pipe(clean());
});


gulp.task('clean', function () {
    runSequence(
        'delete-dist'
    );
});

gulp.task('build', function () {
    runSequence(
        'clean',
        'copy-fonts',
        'build-pp-index',
        'build-mis-index',
        'build-checkout-index'
    );
});
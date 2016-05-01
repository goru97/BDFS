'use strict';

var singApp = angular.module('singApp', [
    // common essential modules
    'ngCookies',
    //'ngAnimate',
    'ngStorage',
    'ngResource',
    'ngFileUpload',

    'ui.router',
    'ui.router.util',
    'ui.jq',
    'ui.event',
    'ui.bootstrap',

    // application libs
    'app.controllers',
    'app.services',
    'app.directives'

]);

singApp.constant('ServerUrl', 'http://localhost:4000');

singApp.config(function($stateProvider, $urlRouterProvider, $httpProvider, $locationProvider, $provide){


    $locationProvider.html5Mode(true);

    $httpProvider.interceptors.push('AuthenticationInterceptor');

    //Set cookies sent by the sever on the client
    $httpProvider.defaults.withCredentials = true;

    // For any unmatched url, send to dashboard
    $urlRouterProvider.otherwise("/app/about/");

    //http://stackoverflow.com/questions/21714655/angular-js-angular-ui-router-reloading-current-state-refresh-data
    $provide.decorator('$state', function($delegate, $stateParams) {
        $delegate.forceReload = function() {
            return $delegate.go($delegate.current, $stateParams, {
                reload: true,
                inherit: false,
                notify: true
            });
        };
        return $delegate;
    });

    $stateProvider
        .state('app', {
            abstract: true,
            url: '/app',
            templateUrl: 'views/app.html'
        })

        // loading page templates dynamically for demo
        .state('app.page', {
            url: '/:page/:child',
            params: {
                page: {},
                child: { value: null }
            },
            resolve: {
                deps: ['scriptLoader', function(scriptLoader){
                    return scriptLoader;
                }]
            },
          /* The controller waits for every one of the above items to be completely resolved before instantiation.  For example, the controller will
         not instantiate until scripltLoader has been resolved. Then those objects are injected into the controller and available for use.  */
         /* used a template provider function which can be injected, has access to locals, and must return template HTML,*/
            templateProvider: function ($http, $stateParams, scriptLoader) {
                //console.log('$http.get(\'views/' + $stateParams.page + '('+ $stateParams.child +')'+ '.html\')');

                if($stateParams.page === 'lookup'){
                    return $http.get('views/lookupViews/' + $stateParams.page + ( /*optional param*/ $stateParams.child ? "_" + $stateParams.child : "") + '.html')
                        .then(function(response) {
                            //  console.log('response: '+ response.data)
                            return scriptLoader.loadScriptTagsFromData(response.data);
                        })
                        .then(function(responseData){
                            return responseData;
                        });
                }else{
                    return $http.get('views/' + $stateParams.page + ( /*optional param*/ $stateParams.child ? "_" + $stateParams.child : "") + '.html')
                        .then(function(response) {
                            //  console.log('response: '+ response.data)
                            return scriptLoader.loadScriptTagsFromData(response.data);
                        })
                        .then(function(responseData){
                            return responseData;
                        });
                }
            },
            /*  used the controllerProvider to dynamically return a controller function or string for you*/
            controllerProvider: function($stateParams){
                if($stateParams.page == 'contact'){
                    var ctrlName = $stateParams.page + "Controller as "+ $stateParams.page;
                    return ctrlName;
                }

            },
            data : {requireAuthentication: true }
        })
        .state('signup', {
            url: '/signup',
            templateUrl: 'views/signup.html',
            controller: 'AuthenticationCtrl',
            data: {}
        })
        .state('verifyEmail', {
            url: '/verify/:token',
            templateUrl: 'views/EmailVerified.html',
            controller: 'verifyTokenCtrl',
            data: {}
        })

        //separate state for login & error pages
        .state('login', {
            url: '/login',
            templateUrl: 'views/login.html',
            controller: 'AuthenticationCtrl',
            data : {}
        })

        .state('error', {
            url: '/error',
            templateUrl: 'views/error.html',
            data : {}
        })
});


singApp.controller('contactController', function(){
 var vm = this;
 vm.message = 'One Washington Sq, San Jose, CA 95192';
 });

singApp.value('uiJqDependencies', {
    'mapael': [
        'bower_components/raphael/raphael-min.js',
        'bower_components/jQuery-Mapael/js/jquery.mapael.js'
    ],
    'easyPieChart': [
        'bower_components/jquery.easy-pie-chart/dist/jquery.easypiechart.min.js'
    ],
    'autosize': [
        'bower_components/jquery-autosize/jquery.autosize.min.js'
    ],
    'wysihtml5': [
        'bower_components/bootstrap3-wysihtml5/lib/js/wysihtml5-0.3.0.min.js',
        'bower_components/bootstrap3-wysihtml5/src/bootstrap3-wysihtml5.js'
    ],
    'select2': [
        'bower_components/select2/select2.min.js'
    ],
    'markdown': [
        'bower_components/markdown/lib/markdown.js',
        'bower_components/bootstrap-markdown/js/bootstrap-markdown.js'
    ],
    'datetimepicker': [
        'bower_components/moment/min/moment.min.js',
        'bower_components/eonasdan-bootstrap-datetimepicker/build/js/bootstrap-datetimepicker.min.js'
    ],
    'colorpicker': [
        'bower_components/mjolnic-bootstrap-colorpicker/dist/js/bootstrap-colorpicker.min.js'
    ],
    'inputmask': [
        'bower_components/jasny-bootstrap/js/inputmask.js'
    ],
    'fileinput': [
        'bower_components/holderjs/holder.js',
        'bower_components/jasny-bootstrap/js/fileinput.js'
    ],
    'slider': [
        'bower_components/seiyria-bootstrap-slider/dist/bootstrap-slider.min.js'
    ],
    'parsley': [
        'bower_components/parsleyjs/dist/parsley.min.js'
    ],
    'sortable': [
        'bower_components/jquery-ui/ui/core.js',
        'bower_components/jquery-ui/ui/widget.js',
        'bower_components/jquery-ui/ui/mouse.js',
        'bower_components/jquery-ui/ui/sortable.js',
        'bower_components/jquery-ui-touch-punch/jquery.ui.touch-punch.min.js'
    ],
    'draggable': [
        'bower_components/jquery-ui/ui/core.js',
        'bower_components/jquery-ui/ui/widget.js',
        'bower_components/jquery-ui/ui/mouse.js',
        'bower_components/jquery-ui/ui/draggable.js'
    ],
    'nestable': [
        'bower_components/jquery.nestable/jquery.nestable.js'
    ],
    'vectorMap': [
        'bower_components/jvectormap/jquery-jvectormap-1.2.2.min.js',
        'bower_components/jvectormap-world/index.js'
    ],
    'sparkline': [
        'bower_components/jquery.sparkline/index.js'
    ],
    'magnificPopup': [
        'bower_components/magnific-popup/dist/jquery.magnific-popup.min.js'
    ]
});

singApp.run(function ($rootScope, $location, AuthenticationModel, $state, $window) {

    $rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
        if (!AuthenticationModel.isSignedIn()) {
            if (toState.data.requireAuthentication === true) {
                $location.path('/login');
            }
        }
        else if (toState === undefined || toState.data.requireAuthentication === undefined) {
                $location.path('/app/about/');    //$state.go('/app/dashboard');  ... $state.go() not working
            }

       /* $scope.loginPage = toState.name == 'login';
        $scope.errorPage = toState.name == 'error';
        $(document).trigger('sn:loaded', [event, toState, toParams, fromState, fromParams]);*/
       // $(document).trigger('sn:loaded', [event, toState, toParams, fromState, fromParams]);
    });

    $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams){
        if (!AuthenticationModel.isSignedIn() && toState.name == 'trial') {
            $location.path('/trial');
        }
        else if (!AuthenticationModel.isSignedIn() && toState.name != 'signup' && toState.name != 'login' && toState.name != 'verifyEmail') {
            $state.go('login', {reload: true});
        }
    });

});


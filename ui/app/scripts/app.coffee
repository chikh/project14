'use strict'

###*
 # @ngdoc overview
 # @name uiApp
 # @description
 # # uiApp
 #
 # Main module of the application.
###
angular
  .module('uiApp', [
    'ngCookies',
    'ngMessages',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch'
  ])
  .config ($routeProvider) ->
    $routeProvider
      .when '/',
        templateUrl: 'views/main.html'
        controller: 'MainCtrl'
      .when '/store',
        templateUrl: 'views/store.html'
        controller: 'StoreCtrl'
      .when '/signup',
        templateUrl: 'views/signup.html'
        controller: 'SignupCtrl'
      .when '/cart',
        templateUrl: 'views/cart.html'
        controller: 'CartCtrl'
      .otherwise
        redirectTo: '/'


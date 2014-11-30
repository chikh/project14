'use strict'

###*
 # @ngdoc function
 # @name uiApp.controller:StoreCtrl
 # @description
 # # StoreCtrl
 # Controller of the uiApp
###
angular.module('uiApp')
  .controller 'StoreCtrl', ($scope) ->
    $scope.awesomeThings = [
      'HTML5 Boilerplate'
      'AngularJS'
      'Karma'
    ]

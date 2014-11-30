'use strict'

###*
 # @ngdoc function
 # @name uiApp.controller:CartCtrl
 # @description
 # # CartCtrl
 # Controller of the uiApp
###
angular.module('uiApp')
  .controller 'CartCtrl', ($scope, storeRest) ->
    $scope.makeOrder = ->
      alert 'todo'

    $scope.isNotEmpty = -> $scope.$parent.productsInCart and $scope.$parent.productsInCart.length > 0

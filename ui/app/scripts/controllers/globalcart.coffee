'use strict'

###*
 # @ngdoc function
 # @name uiApp.controller:GlobalcartCtrl
 # @description
 # # GlobalcartCtrl
 # Controller of the uiApp
###
angular.module('uiApp')
  .controller 'GlobalcartCtrl', ($scope) ->
    $scope.productsInCart = []

    $scope.remove = (index) -> $scope.productsInCart.splice index, 1

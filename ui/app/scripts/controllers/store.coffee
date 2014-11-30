'use strict'

###*
 # @ngdoc function
 # @name uiApp.controller:StoreCtrl
 # @description
 # # StoreCtrl
 # Controller of the uiApp
###
angular.module('uiApp')
  .controller 'StoreCtrl', ($scope, storeRest) ->
    $scope.products = []

    storeRest.listProducts() .then (products) -> $scope.products = products

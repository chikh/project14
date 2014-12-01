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
      storeRest.makeOrder
        products: $scope.$parent.productsInCart.map((product) ->
          productId: product.id,
          preOrderedQuantity: product.preOrderQuantity
        )
      .then (data) ->
        $scope.$parent.productsInCart = [];
        delete $scope.errorMessage
        $scope.successMessage = data;
      .catch (error) ->
        delete $scope.successMessage
        $scope.errorMessage = error;

    $scope.isNotEmpty = -> $scope.$parent.productsInCart and $scope.$parent.productsInCart.length > 0

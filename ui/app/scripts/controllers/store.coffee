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

    $scope.isNotEmpty = -> $scope.products and $scope.products.length > 0

    storeRest.listProducts() .then (products) ->
      products.map(applyCurrentCartQuantity)
      $scope.products = products

    applyCurrentCartQuantity = (product) ->
      existing = findExistingInCart(product)
      product.quantity = product.quantity - existing.preOrderQuantity if existing

    findExistingInCart = (product) ->
      index = $scope.$parent.productsInCart.map((e) -> e.id).indexOf(product.id)
      alreadyExistingInCart = $scope.$parent.productsInCart[index] if (index >= 0)

    $scope.checkQuantityCorrectness = (product) ->
      product.preOrderInfo and product.preOrderInfo.quantity > 0 and product.quantity >= product.preOrderInfo.quantity

    $scope.addToCart = (product) ->
      preOrderQuantity = product.preOrderInfo.quantity
      delete product.preOrderInfo
      product.quantity = product.quantity - preOrderQuantity
      alreadyExistingInCart = findExistingInCart(product)
      if (alreadyExistingInCart)
        alreadyExistingInCart.preOrderQuantity = preOrderQuantity + alreadyExistingInCart.preOrderQuantity
      else
        product.preOrderQuantity = preOrderQuantity
        $scope.$parent.productsInCart.push(product)

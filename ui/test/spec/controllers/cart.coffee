'use strict'

describe 'Controller: CartCtrl', ->

  # load the controller's module
  beforeEach module 'uiApp'

  CartCtrl = {}
  scope = {}

  # Initialize the controller and a mock scope
  beforeEach inject ($controller, $rootScope) ->
    scope = $rootScope.$new()
    CartCtrl = $controller 'CartCtrl', {
      $scope: scope
    }

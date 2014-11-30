'use strict'

describe 'Controller: GlobalcartCtrl', ->

  # load the controller's module
  beforeEach module 'uiApp'

  GlobalcartCtrl = {}
  scope = {}

  # Initialize the controller and a mock scope
  beforeEach inject ($controller, $rootScope) ->
    scope = $rootScope.$new()
    GlobalcartCtrl = $controller 'GlobalcartCtrl', {
      $scope: scope
    }

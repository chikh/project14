'use strict'

describe 'Controller: StoreCtrl', ->

  # load the controller's module
  beforeEach module 'uiApp'

  StoreCtrl = {}
  scope = {}

  # Initialize the controller and a mock scope
  beforeEach inject ($controller, $rootScope) ->
    scope = $rootScope.$new()
    StoreCtrl = $controller 'StoreCtrl', {
      $scope: scope
    }

  it 'should attach a list of awesomeThings to the scope', ->
    expect(scope.awesomeThings.length).toBe 3

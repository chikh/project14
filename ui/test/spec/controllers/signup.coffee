'use strict'

describe 'Controller: SignupCtrl', ->

  # load the controller's module
  beforeEach module 'uiApp'

  SignupCtrl = {}
  scope = {}

  # Initialize the controller and a mock scope
  beforeEach inject ($controller, $rootScope) ->
    scope = $rootScope.$new()
    SignupCtrl = $controller 'SignupCtrl', {
      $scope: scope
    }

  it 'should attach a list of awesomeThings to the scope', ->
    expect(scope.awesomeThings.length).toBe 3

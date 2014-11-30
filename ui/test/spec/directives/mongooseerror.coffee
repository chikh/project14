'use strict'

describe 'Directive: mongooseError', ->

  # load the directive's module
  beforeEach module 'uiApp'

  scope = {}

  beforeEach inject ($controller, $rootScope) ->
    scope = $rootScope.$new()

  it 'should make hidden element visible', inject ($compile) ->
    element = angular.element '<mongoose-error></mongoose-error>'
    element = $compile(element) scope
    expect(element.text()).toBe 'this is the mongooseError directive'

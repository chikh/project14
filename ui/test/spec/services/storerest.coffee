'use strict'

describe 'Service: storeRest', ->

  # load the service's module
  beforeEach module 'uiApp'

  # instantiate service
  storeRest = {}
  beforeEach inject (_storeRest_) ->
    storeRest = _storeRest_

  it 'should do something', ->
    expect(!!storeRest).toBe true

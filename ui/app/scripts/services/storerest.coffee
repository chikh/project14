'use strict'

class StoreRest

  constructor: (@$http, @$q, @$log) ->

  listProducts: () ->
    deferred = @$q.defer()

    @$http.get("/products")
    .success((data, status, headers) =>
      @$log.debug("Successfully listed products - status #{status}")
      deferred.resolve(data)
    )
    .error((data, status, headers) =>
      @$log.error("Failed to list products - status #{status}")
      deferred.reject(data);
    )
    deferred.promise

  createUser: (user) ->
    @$log.debug "createUser #{angular.toJson(user, true)}"
    deferred = @$q.defer()

    @$http.post('/user', user)
    .success((data, status, headers) =>
      @$log.debug("Successfully created User - status #{status}")
      deferred.resolve(data)
    )
    .error((data, status, headers) =>
      @$log.error("Failed to create user - status #{status}")
      deferred.reject(data);
    )
    deferred.promise

  makeOrder: (order) ->
    @$log.debug "make order #{angular.toJson(order, true)}"
    deferred = @$q.defer()

    @$http.post('/order', order)
    .success((data, status, headers) =>
      @$log.debug("Successfully made order - status #{status}")
      deferred.resolve(data)
    )
    .error((data, status, headers) =>
      @$log.error("Failed to make order - status #{status}")
      deferred.reject(data);
    )
    deferred.promise

###*
 # @ngdoc service
 # @name uiApp.storeRest
 # @description
 # # storeRest
 # Service in the uiApp.
###
angular.module('uiApp')
.service 'storeRest', StoreRest

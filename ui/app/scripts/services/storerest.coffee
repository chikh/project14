'use strict'

class StoreRest

  constructor: (@$http, @$q, @$log) ->

  listProducts: () ->
    @$log.debug "listUsers()"
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

###*
 # @ngdoc service
 # @name uiApp.storeRest
 # @description
 # # storeRest
 # Service in the uiApp.
###
angular.module('uiApp')
.service 'storeRest', StoreRest

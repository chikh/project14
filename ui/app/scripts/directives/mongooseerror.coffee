'use strict'

###*
 # @ngdoc directive
 # @name uiApp.directive:mongooseError
 # @description
 # # mongooseError
###
angular.module('uiApp')
.directive 'mongooseError', ->
  restrict: 'A'
  require: 'ngModel'
  link: (scope, element, attrs, ngModel) ->
    element.on 'keydown', ->
      ngModel.$setValidity 'mongoose', true

angular.module('chatter.exceptionHandler', [])
    .factory('exceptionHandler', function ($state, $rootScope) {
        var service = {};

        service.handleRestError = function (error) {
            if (error.status !== undefined && error.status === 401) {
                if ($rootScope.user == null || $rootScope.user == undefined || $rootScope.user == "") {
                    $state.go("login");
                }
            }
        };

        return service;
    });
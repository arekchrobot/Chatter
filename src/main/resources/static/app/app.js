angular.module("chatter", [
    "ngResource",
    "ui.router",
    "ngIdle",

    "chatter.directives",

    "chatter.exceptionHandler",
    "chatter.authServices",
    "chatter.authController"
])
    .constant("REST_API_PREFIX", "/api/")
    .controller("baseController", function ($scope) {

    $scope.appId = 123;
}).config(function($resourceProvider) {
    $resourceProvider.defaults.actions = {
        save: {method: 'POST', headers: {'Content-Type' : "application/json"}},
        get:    {method: 'GET'},
        getAll: {method: 'GET', isArray:true},
        update: {method: 'PUT'},
        delete: {method: 'DELETE'}
    };
});
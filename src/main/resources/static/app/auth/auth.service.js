angular.module("chatter.authServices", [])
    .factory("authRestService", function ($http, REST_API_PREFIX, exceptionHandler) {
        var service = {};

        service.baseObjcetUrl = "auth/";

        service.authenticate = function (credentials, successFunction, failureFunction) {
            $http.post(REST_API_PREFIX + this.baseObjcetUrl + "login", credentials)
                .then(successFunction, failureFunction);
        };

        service.logout = function (successFunction) {
            $http.post(REST_API_PREFIX + this.baseObjcetUrl + "signout", {})
                .then(successFunction, exceptionHandler.handleRestError)
        };

        service.isLogged = function (successFunction, failureFunction) {
            $http.get(REST_API_PREFIX + this.baseObjcetUrl + "logged", {})
                .then(successFunction, failureFunction)
        };

        service.register = function (registerData, successFunction, failureFunction) {
            $http.post(REST_API_PREFIX + this.baseObjcetUrl + "register", registerData)
                .then(successFunction, failureFunction)
        };

        return service;
    }).factory("authService", function () {
        var service = {};

        service.initScope = function ($scope) {
            $scope.forms = {};
            $scope.credentials = {};
            $scope.registerData = {};
            $scope.loginError = false;
            $scope.template = {};
            $scope.template.isLogin = true;
            $scope.registerError = false;
        };

        service.loginError = function ($scope, returnedData) {
            $scope.credentials = {};
            $scope.loginError = true;
            $scope.loginErrorMsg = returnedData.message;
        };

        service.registerClearFormWithSuccess = function ($scope) {
            $scope.forms.registerForm.$setPristine();
            $scope.forms.registerForm.$setUntouched();
            $scope.registerError = false;
            $scope.registerData = {};
            $scope.registerSuccess = true;
            $scope.registerSuccessMsg = "Your account has been created";
            $scope.template.isLogin = true;
        };

        service.registerFormError = function($scope, returnedData) {
            $scope.registerError = true;
            $scope.registerSuccess = false;
            $scope.registerErrorMsg = returnedData.message;
        };

        return service;
    });
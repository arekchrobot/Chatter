angular.module("chatter.authController", []).config(function ($stateProvider) {
    $stateProvider.state("login", {
        url: "/login",
        templateUrl: "html/auth/login.html"
    });
}).controller("authController", function ($rootScope, $scope, $state, authRestService, authService) {

    authService.initScope($scope);

    var loggedError = function (returnedData) {
        $rootScope.user = null;
    };

    $scope.login = function () {
        authRestService.authenticate($scope.credentials,
            function (returnedData) {
                $rootScope.user = returnedData.data;
                $scope.credentials = {};
                $state.go("chat");
            }, function (returnedData) {
                authService.loginError($scope, returnedData.data);
            });
    };

    authRestService.isLogged(
        function (returnedData) {
            $rootScope.user = returnedData.data;
            $rootScope.$broadcast('establishNotificationWebSocket');
        }, loggedError);

    $scope.logout = function () {
        authRestService.logout(
            function (returnedData) {
                $rootScope.user = null;
                $state.go("login");
            }, function (returnedData) {
                $rootScope.user = null;
                $state.go("login");
            });
    };

    $scope.register = function () {
        authRestService.register($scope.registerData.user,
            function (returnedData) {
                authService.registerClearFormWithSuccess($scope);
            }, function (returnedData) {
                authService.registerFormError($scope, returnedData.data);
            });
    };
});
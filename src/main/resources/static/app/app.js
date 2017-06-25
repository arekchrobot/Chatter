angular.module("chatter", [
    "ngResource",
    "ui.router",
    "ngIdle",
    "luegg.directives",

    "chatter.directives",

    "chatter.exceptionHandler",
    "chatter.authServices",
    "chatter.authController",

    "chatter.chatServices",
    "chatter.chatController"
])
.constant("REST_API_PREFIX", "/api/")
.config(function (IdleProvider, KeepaliveProvider) {

    IdleProvider.idle(10 * 60); //10 minutes idle
    IdleProvider.timeout(30); //after 30 seconds idle, time the user out
    KeepaliveProvider.interval(5 * 60); // 5 minute keep-alive ping

}).run(function ($rootScope, $state, Idle) {

    Idle.watch();
    $rootScope.$on('IdleTimeout', function () {
        $rootScope.user = null;
        $state.go("login");
    });
});

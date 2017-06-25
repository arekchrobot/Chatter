angular.module("chatter.chatController", []).config(function ($stateProvider) {
    $stateProvider.state("chat", {
        url: "/chat",
        templateUrl: "html/chat/chat.html",
        controller: "chatController"
    });
}).controller("chatController", function ($rootScope, $scope, chatRestService, chatService) {


    chatRestService.getAllUsers(function(returnedData) {
        $scope.users = returnedData.data;
    });

    $scope.selectedUser = {};
    $scope.selectedChat = {};

    $scope.chat = {};

    $scope.openChat = function(currentUser) {
        chatService.getOrCreateChat($rootScope.user, currentUser, $scope);
    };

    $scope.sendMessage = function() {
        chatRestService.sendMessage($scope.chat.message, $scope.selectedUser.username, function(returnedData){
            $scope.selectedChat.messages.push(returnedData.data);
            $scope.chat.message = "";
        });
    }
});
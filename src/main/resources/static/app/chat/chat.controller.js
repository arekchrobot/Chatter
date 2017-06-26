angular.module("chatter.chatController", []).config(function ($stateProvider) {
    $stateProvider.state("chat", {
        url: "/chat",
        templateUrl: "html/chat/chat.html",
        controller: "chatController"
    });
}).controller("chatController", function ($rootScope, $scope, chatRestService, chatService, notificationWebSocketService) {

    chatRestService.getAllUsers(function(returnedData) {
        $scope.users = returnedData.data;
    });

    chatService.initScope($scope);

    notificationWebSocketService.receive().then(null, null, function(data){
        chatService.addMessageToCorrectChat($rootScope.user.chats, data);
    });

    notificationWebSocketService.initialize($rootScope.user.socketToken);

    $scope.openChat = function(currentUser) {
        chatService.getOrCreateChat($rootScope.user, currentUser, $scope);
    };

    $scope.sendMessage = function() {
        chatRestService.sendMessage($scope.chat.message, $scope.selectedUser.username, function(returnedData){
            $scope.selectedChat.messages.push(returnedData.data);
            $scope.chat.message = "";
        });
    };

    $scope.closeConnection = function() {
        notificationWebSocketService.closeConnection();
    }
});
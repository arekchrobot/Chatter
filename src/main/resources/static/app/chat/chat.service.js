angular.module("chatter.chatServices", [])
    .factory("chatRestService", function ($http, exceptionHandler, REST_API_PREFIX) {
        var service = {};

        service.baseObjcetUrl = "message/";

        service.sendMessage = function(message, receiver, successFunction) {
            $http.post(REST_API_PREFIX + this.baseObjcetUrl + "send/" + receiver, message, {
                headers: {'Content-Type': 'text/plain'}
            }).then(successFunction, exceptionHandler.handleRestError);
        };

        service.getAllUsers = function(successFunction) {
            $http.get(REST_API_PREFIX + "user/", {})
                .then(successFunction, exceptionHandler.handleRestError)
        };

        return service;
    }).factory("chatService", function() {
        var service = {};

        service.initScope = function($scope) {
            $scope.selectedUser = {};
            $scope.selectedChat = {};

            $scope.chat = {};
        };

        service.getOrCreateChat = function(loggedUser, currentUser, $scope) {
            var chatFound = false;
            angular.forEach(loggedUser.chats, function(chat) {
                if(chat.name.indexOf(currentUser.username) !== -1) {
                    $scope.selectedChat = chat;
                    $scope.selectedUser = currentUser;
                    chatFound = true;
                }
            });

            if (!chatFound) {
                $scope.selectedChat = {};
                $scope.selectedChat.name = loggedUser.username + "_" + currentUser.username;
                $scope.selectedChat.messages = [];
                $scope.selectedUser = currentUser;

                loggedUser.chats.push($scope.selectedChat);
            }
        };

        service.addMessageToCorrectChat = function(chats, message) {
            angular.forEach(chats, function(chat){
                if (chat.name == message.chat.name) {
                    chat.messages.push(message);
                }
            });
        };

        return service;
    });
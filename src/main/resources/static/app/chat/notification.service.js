angular.module("chatter.notificationServices", [])
    .factory("notificationWebSocketService", function ($q) {
        var service = {};

        var listener = $q.defer();
        var socket = {
            ws: null,
            client: null
        };
        var connectionToken;

        service.SOCKET_ENDPOINT_URL = "/ws";
        service.MESSAGE_NOTFICATION_ENDPOINT = "/topic/message/receive/";

        service.receive = function() {
            return listener.promise;
        };

        var getJsonMessage = function(data) {
            var message = JSON.parse(data);

            return message;
        };

        var startListener = function() {
            socket.client.subscribe(service.MESSAGE_NOTFICATION_ENDPOINT + connectionToken, function(returnedData){
                listener.notify(getJsonMessage(returnedData.body));
            });
        };

        function connect() {
            if(socket.ws.readyState !== SockJS.OPEN) {
                setTimeout(connect, 100); //checking for established connection
                return;
            }
            console.log("establishing connect");
            socket.client.connect({}, startListener());
        }

        function init() {
            socket.ws = new SockJS(service.SOCKET_ENDPOINT_URL);
            socket.client = Stomp.over(socket.ws);
            socket.client.debug = null; //disable logging to console
        }

        init();

        service.initialize = function(token) {
            if (token == null || token == undefined) {

            } else {
                connectionToken = token;

                init();

                connect();
            }
        };

        return service;
    });
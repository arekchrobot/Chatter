describe('chatController', function() {
    var chatController, scope, rootScope, httpBackend, chatRestService, chatService;

    beforeEach(module('chatter'));

    beforeEach(inject(function(_$rootScope_, $controller, _chatRestService_, _chatService_, _$httpBackend_, $templateCache){
        chatService = _chatService_;
        chatRestService = _chatRestService_;

        scope = _$rootScope_.$new();
        rootScope = _$rootScope_;

        httpBackend = _$httpBackend_;

        $templateCache.put('html/chat/chat.html', '');

        chatController = function() {
            return $controller("chatController", {
                '$rootScope': rootScope,
                '$scope': scope,
                'chatRestService': chatRestService,
                'chatService': chatService
            });
        }
    }));

    it('should send message and add it to selected chat, then clear message', function() {
        //given
        httpBackend
            .expect('GET', '/api/user/')
            .respond(200, {content: 'ok'});

        httpBackend
            .expect('POST', '/api/message/send/Phil')
            .respond(200, {content: 'ok'});

        rootScope.user = {};
        rootScope.user.socketToken = "testToken";

        var controller = chatController();

        scope.chat = {};
        scope.chat.message = "to be removed";

        scope.selectedChat = {};
        scope.selectedChat.messages = [{content:'first'}];

        scope.selectedUser = {};

        scope.selectedUser.username = "Phil";

        //when
        scope.sendMessage();

        httpBackend.flush();

        //then
        expect(scope.selectedChat.messages.length).toBe(2);
        expect(scope.chat.message).toBe("");
    });
});
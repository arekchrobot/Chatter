describe('chatService', function(){

    var chatService;

    beforeEach(module('chatter'));

    beforeEach(inject(function(_chatService_){
        chatService = _chatService_;
    }));

    it('should get existing chat', function(){
        //given
        var loggedUser = {
            username: 'test'
        };
        loggedUser.chats = [];

        var chat = {
            name: 'test_asd',
            id: 1,
            messages: []
        };

        var currentUser = {
            username: 'asd'
        };

        loggedUser.chats.push(chat);

        var scope = {};

        //when
        chatService.getOrCreateChat(loggedUser, currentUser, scope);

        //then
        expect(scope.selectedChat.id).toBe(chat.id);
    });

    it('should create empty chat', function() {
        //given
        var loggedUser = {
            username: 'test'
        };
        loggedUser.chats = [];

        var chat = {
            name: 'test_asd',
            id: 1,
            messages: [{}, {}]
        };

        var currentUser = {
            username: 'Daniel'
        };

        loggedUser.chats.push(chat);

        var scope = {};

        //when
        chatService.getOrCreateChat(loggedUser, currentUser, scope);

        //then
        expect(scope.selectedChat.id).toBeUndefined();
        expect(scope.selectedChat.name).toBe('test_Daniel');
        expect(scope.selectedChat.messages.length).toBe(0);
        expect(loggedUser.chats.length).toBe(2);
    })
});
describe('authController', function () {
    var authController, scope, httpBackend, state, rootScope, authRestService;

    beforeEach(module('chatter'));

    beforeEach(inject(function (_$rootScope_, $controller, _$state_, _authRestService_, _authService_, _$httpBackend_, $templateCache) {
        scope = _$rootScope_.$new();

        httpBackend = _$httpBackend_;

        state = _$state_;

        rootScope = _$rootScope_;

        $templateCache.put('html/auth/login.html', '');

        authRestService = _authRestService_;

        authController = function () {
            return $controller('authController', {
                '$rootScope': rootScope,
                '$scope': scope,
                '$state': state,
                'authRestService': authRestService,
                'authService': _authService_
            });
        }
    }));

    it('should properly login user', function () {
        //given
        httpBackend
            .expect('GET', '/api/auth/logged')
            .respond(401, null);

        httpBackend
            .expect('POST', '/api/auth/login')
            .respond(200, {data: {username: "username"}});

        scope.credentials = {
            username: "username",
            password: "pass"
        };

        var controller = authController();

        //when
        scope.login();

        httpBackend.flush();

        //then
        expect(scope.credentials.username).not.toBeDefined();
        expect(rootScope.user).toBeTruthy();
    });

    it('should error login user', function () {
        //given
        httpBackend
            .expect('GET', '/api/auth/logged')
            .respond(401, null);

        httpBackend
            .expect('POST', '/api/auth/login')
            .respond(400, {message: "bad request"});


        scope.credentials = {
            username: "username",
            password: "pass"
        };

        var controller = authController();

        //when
        scope.login();

        httpBackend.flush();

        //then
        expect(scope.credentials.username).not.toBeDefined();
        expect(scope.loginErrorMsg).toBe("bad request");
        expect(scope.loginError).toBe(true);
        expect(rootScope.user).not.toBeTruthy();
    });

    it('should set user to null after logout', function () {
        //given
        rootScope.user = {};

        httpBackend
            .expect('GET', '/api/auth/logged')
            .respond(401, null);

        httpBackend
            .expect('POST', '/api/auth/signout')
            .respond(200, true);

        var controller = authController();

        //when
        scope.logout();

        httpBackend.flush();

        //then
        expect(rootScope.user).not.toBeTruthy();
    });
});
describe('authService', function(){
    var authService;

    beforeEach(module('chatter'));

    beforeEach(inject(function (_authService_){
        authService = _authService_;
    }));

    it('should set proper init values', function(){
        //given
        var scope = {};

        //when
        authService.initScope(scope);

        //then
        expect(scope.forms).toBeTruthy();
        expect(scope.template.isLogin).toBe(true);
    });

    it('should properly set register success message and isLogin flag', function(){
        //given
        var scope = {};
        scope.forms = {};
        scope.template = {};
        scope.forms.registerForm = {
            $setPristine : function(){},
            $setUntouched: function(){}
        };
        scope.template.isLogin = false;

        //when
        authService.registerClearFormWithSuccess(scope);

        //then
        expect(scope.registerSuccessMsg).toBe("Your account has been created");
        expect(scope.template.isLogin).toBe(true);
    });

    it('should properly set error message for register', function() {
        //given
        var scope = {};
        var returnedData = {
            message:"test msg"
        };

        //when
        authService.registerFormError(scope, returnedData);

        //then
        expect(scope.registerError).toBe(true);
        expect(scope.registerSuccess).toBe(false);
        expect(scope.registerErrorMsg).toBe("test msg");
    });

    it('should clear crednetials after login error and display message', function(){
        //given
        var scope = {};
        scope.credentials = {
            username: "username",
            password: "pass"
        };
        var returnedData = {
            message:"test msg"
        };

        //when
        authService.loginError(scope, returnedData);

        //then
        expect(scope.credentials.username).not.toBeDefined();
        expect(scope.credentials.password).not.toBeDefined();
        expect(scope.loginErrorMsg).toBe("test msg");
    });
});
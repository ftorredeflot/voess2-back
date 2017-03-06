'use strict';

describe('Controller Tests', function() {

    describe('UserFavVideo Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockUserFavVideo, MockUser, MockVideo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockUserFavVideo = jasmine.createSpy('MockUserFavVideo');
            MockUser = jasmine.createSpy('MockUser');
            MockVideo = jasmine.createSpy('MockVideo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'UserFavVideo': MockUserFavVideo,
                'User': MockUser,
                'Video': MockVideo
            };
            createController = function() {
                $injector.get('$controller')("UserFavVideoVoessDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'voess2App:userFavVideoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});

'use strict';

describe('Controller Tests', function() {

    describe('UserWatchedVideo Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockUserWatchedVideo, MockUser, MockVideo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockUserWatchedVideo = jasmine.createSpy('MockUserWatchedVideo');
            MockUser = jasmine.createSpy('MockUser');
            MockVideo = jasmine.createSpy('MockVideo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'UserWatchedVideo': MockUserWatchedVideo,
                'User': MockUser,
                'Video': MockVideo
            };
            createController = function() {
                $injector.get('$controller')("UserWatchedVideoVoessDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'voess2App:userWatchedVideoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});

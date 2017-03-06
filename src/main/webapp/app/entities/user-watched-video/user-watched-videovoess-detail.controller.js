(function() {
    'use strict';

    angular
        .module('voess2App')
        .controller('UserWatchedVideoVoessDetailController', UserWatchedVideoVoessDetailController);

    UserWatchedVideoVoessDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UserWatchedVideo', 'User', 'Video'];

    function UserWatchedVideoVoessDetailController($scope, $rootScope, $stateParams, previousState, entity, UserWatchedVideo, User, Video) {
        var vm = this;

        vm.userWatchedVideo = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('voess2App:userWatchedVideoUpdate', function(event, result) {
            vm.userWatchedVideo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

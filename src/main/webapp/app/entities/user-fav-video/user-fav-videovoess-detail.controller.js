(function() {
    'use strict';

    angular
        .module('voess2App')
        .controller('UserFavVideoVoessDetailController', UserFavVideoVoessDetailController);

    UserFavVideoVoessDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UserFavVideo', 'User', 'Video'];

    function UserFavVideoVoessDetailController($scope, $rootScope, $stateParams, previousState, entity, UserFavVideo, User, Video) {
        var vm = this;

        vm.userFavVideo = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('voess2App:userFavVideoUpdate', function(event, result) {
            vm.userFavVideo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

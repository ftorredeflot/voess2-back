(function() {
    'use strict';

    angular
        .module('voess2App')
        .controller('FriendshipVoessDetailController', FriendshipVoessDetailController);

    FriendshipVoessDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Friendship', 'User'];

    function FriendshipVoessDetailController($scope, $rootScope, $stateParams, previousState, entity, Friendship, User) {
        var vm = this;

        vm.friendship = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('voess2App:friendshipUpdate', function(event, result) {
            vm.friendship = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

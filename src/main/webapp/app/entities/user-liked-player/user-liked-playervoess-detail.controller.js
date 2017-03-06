(function() {
    'use strict';

    angular
        .module('voess2App')
        .controller('UserLikedPlayerVoessDetailController', UserLikedPlayerVoessDetailController);

    UserLikedPlayerVoessDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UserLikedPlayer', 'User', 'Player'];

    function UserLikedPlayerVoessDetailController($scope, $rootScope, $stateParams, previousState, entity, UserLikedPlayer, User, Player) {
        var vm = this;

        vm.userLikedPlayer = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('voess2App:userLikedPlayerUpdate', function(event, result) {
            vm.userLikedPlayer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

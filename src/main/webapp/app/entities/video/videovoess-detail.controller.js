(function() {
    'use strict';

    angular
        .module('voess2App')
        .controller('VideoVoessDetailController', VideoVoessDetailController);

    VideoVoessDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Video', 'Game', 'Team', 'Tournament', 'Player'];

    function VideoVoessDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Video, Game, Team, Tournament, Player) {
        var vm = this;

        vm.video = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('voess2App:videoUpdate', function(event, result) {
            vm.video = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

(function() {
    'use strict';

    angular
        .module('voess2App')
        .controller('TournamentVoessDetailController', TournamentVoessDetailController);

    TournamentVoessDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Tournament', 'Team', 'Video'];

    function TournamentVoessDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Tournament, Team, Video) {
        var vm = this;

        vm.tournament = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('voess2App:tournamentUpdate', function(event, result) {
            vm.tournament = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

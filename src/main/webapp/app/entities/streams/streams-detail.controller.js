(function() {
    'use strict';

    angular
        .module('voess2App')
        .controller('StreamsDetailController', StreamsDetailController);

    StreamsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Streams'];

    function StreamsDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Streams) {
        var vm = this;

        vm.streams = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('voess2App:streamsUpdate', function(event, result) {
            vm.streams = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

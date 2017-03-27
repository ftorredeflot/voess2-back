(function() {
    'use strict';

    angular
        .module('voess2App')
        .controller('StreamsController', StreamsController);

    StreamsController.$inject = ['$scope', '$state', 'DataUtils', 'Streams'];

    function StreamsController ($scope, $state, DataUtils, Streams) {
        var vm = this;

        vm.streams = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            Streams.query(function(result) {
                vm.streams = result;
                vm.searchQuery = null;
            });
        }
    }
})();

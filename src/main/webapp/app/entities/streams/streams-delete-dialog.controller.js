(function() {
    'use strict';

    angular
        .module('voess2App')
        .controller('StreamsDeleteController',StreamsDeleteController);

    StreamsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Streams'];

    function StreamsDeleteController($uibModalInstance, entity, Streams) {
        var vm = this;

        vm.streams = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Streams.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

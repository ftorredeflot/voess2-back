(function() {
    'use strict';

    angular
        .module('voess2App')
        .controller('UserFavVideoVoessDeleteController',UserFavVideoVoessDeleteController);

    UserFavVideoVoessDeleteController.$inject = ['$uibModalInstance', 'entity', 'UserFavVideo'];

    function UserFavVideoVoessDeleteController($uibModalInstance, entity, UserFavVideo) {
        var vm = this;

        vm.userFavVideo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UserFavVideo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

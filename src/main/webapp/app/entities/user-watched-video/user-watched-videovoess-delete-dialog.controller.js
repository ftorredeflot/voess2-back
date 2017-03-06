(function() {
    'use strict';

    angular
        .module('voess2App')
        .controller('UserWatchedVideoVoessDeleteController',UserWatchedVideoVoessDeleteController);

    UserWatchedVideoVoessDeleteController.$inject = ['$uibModalInstance', 'entity', 'UserWatchedVideo'];

    function UserWatchedVideoVoessDeleteController($uibModalInstance, entity, UserWatchedVideo) {
        var vm = this;

        vm.userWatchedVideo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UserWatchedVideo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

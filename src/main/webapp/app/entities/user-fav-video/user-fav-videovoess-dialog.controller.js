(function() {
    'use strict';

    angular
        .module('voess2App')
        .controller('UserFavVideoVoessDialogController', UserFavVideoVoessDialogController);

    UserFavVideoVoessDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserFavVideo', 'User', 'Video'];

    function UserFavVideoVoessDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, UserFavVideo, User, Video) {
        var vm = this;

        vm.userFavVideo = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.videos = Video.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.userFavVideo.id !== null) {
                UserFavVideo.update(vm.userFavVideo, onSaveSuccess, onSaveError);
            } else {
                UserFavVideo.save(vm.userFavVideo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('voess2App:userFavVideoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startDateTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();

(function() {
    'use strict';

    angular
        .module('voess2App')
        .controller('UserWatchedVideoVoessDialogController', UserWatchedVideoVoessDialogController);

    UserWatchedVideoVoessDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserWatchedVideo', 'User', 'Video'];

    function UserWatchedVideoVoessDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, UserWatchedVideo, User, Video) {
        var vm = this;

        vm.userWatchedVideo = entity;
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
            if (vm.userWatchedVideo.id !== null) {
                UserWatchedVideo.update(vm.userWatchedVideo, onSaveSuccess, onSaveError);
            } else {
                UserWatchedVideo.save(vm.userWatchedVideo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('voess2App:userWatchedVideoUpdate', result);
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

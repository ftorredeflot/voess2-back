(function() {
    'use strict';

    angular
        .module('voess2App')
        .controller('VideoVoessDialogController', VideoVoessDialogController);

    VideoVoessDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Video', 'Game', 'Team', 'Tournament', 'Player'];

    function VideoVoessDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Video, Game, Team, Tournament, Player) {
        var vm = this;

        vm.video = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.games = Game.query();
        vm.teams = Team.query();
        vm.tournaments = Tournament.query();
        vm.players = Player.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.video.id !== null) {
                Video.update(vm.video, onSaveSuccess, onSaveError);
            } else {
                Video.save(vm.video, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('voess2App:videoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.videoDate = false;

        vm.setVideoBlob = function ($file, video) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        video.videoBlob = base64Data;
                        video.videoBlobContentType = $file.type;
                    });
                });
            }
        };

        vm.setVideoCover = function ($file, video) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        video.videoCover = base64Data;
                        video.videoCoverContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();

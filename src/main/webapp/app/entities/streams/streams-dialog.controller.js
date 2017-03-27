(function() {
    'use strict';

    angular
        .module('voess2App')
        .controller('StreamsDialogController', StreamsDialogController);

    StreamsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Streams'];

    function StreamsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Streams) {
        var vm = this;

        vm.streams = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.streams.id !== null) {
                Streams.update(vm.streams, onSaveSuccess, onSaveError);
            } else {
                Streams.save(vm.streams, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('voess2App:streamsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setStreamerPhoto = function ($file, streams) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        streams.streamerPhoto = base64Data;
                        streams.streamerPhotoContentType = $file.type;
                    });
                });
            }
        };

    }
})();

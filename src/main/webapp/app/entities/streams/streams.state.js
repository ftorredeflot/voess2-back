(function() {
    'use strict';

    angular
        .module('voess2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('streams', {
            parent: 'entity',
            url: '/streams',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'voess2App.streams.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/streams/streams.html',
                    controller: 'StreamsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('streams');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('streams-detail', {
            parent: 'entity',
            url: '/streams/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'voess2App.streams.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/streams/streams-detail.html',
                    controller: 'StreamsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('streams');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Streams', function($stateParams, Streams) {
                    return Streams.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'streams',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('streams-detail.edit', {
            parent: 'streams-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/streams/streams-dialog.html',
                    controller: 'StreamsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Streams', function(Streams) {
                            return Streams.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('streams.new', {
            parent: 'streams',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/streams/streams-dialog.html',
                    controller: 'StreamsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                streamerName: null,
                                streamerUrl: null,
                                streamerPhoto: null,
                                streamerPhotoContentType: null,
                                streamerState: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('streams', null, { reload: 'streams' });
                }, function() {
                    $state.go('streams');
                });
            }]
        })
        .state('streams.edit', {
            parent: 'streams',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/streams/streams-dialog.html',
                    controller: 'StreamsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Streams', function(Streams) {
                            return Streams.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('streams', null, { reload: 'streams' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('streams.delete', {
            parent: 'streams',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/streams/streams-delete-dialog.html',
                    controller: 'StreamsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Streams', function(Streams) {
                            return Streams.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('streams', null, { reload: 'streams' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

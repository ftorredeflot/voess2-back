(function() {
    'use strict';

    angular
        .module('voess2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('LeagueOfLegends', {
            parent: 'entity',
            url: '/LeagueOfLegends',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'voess2App.game.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/games/LeagueOfLegends/LeagueOfLegends.html',
                    controller: 'LeagueOfLegendsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('game');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
            /*asidjaoisdjao posible error */
        .state('LeagueOfLegends-detail', {
            parent: 'entity',
            url: '/LeagueOfLegends/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'voess2App.game.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/game/gamevoess-detail.html',
                    controller: 'GameVoessDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('game');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Game', function($stateParams, Game) {
                    return Game.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'gamevoess',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('gamevoess-detail.edit', {
            parent: 'gamevoess-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/game/gamevoess-dialog.html',
                    controller: 'GameVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Game', function(Game) {
                            return Game.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('gamevoess.new', {
            parent: 'gamevoess',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/game/gamevoess-dialog.html',
                    controller: 'GameVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                gameName: null,
                                gamePicture: null,
                                gamePictureContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('gamevoess', null, { reload: 'gamevoess' });
                }, function() {
                    $state.go('gamevoess');
                });
            }]
        })
        .state('gamevoess.edit', {
            parent: 'gamevoess',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/game/gamevoess-dialog.html',
                    controller: 'GameVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Game', function(Game) {
                            return Game.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('gamevoess', null, { reload: 'gamevoess' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('gamevoess.delete', {
            parent: 'gamevoess',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/game/gamevoess-delete-dialog.html',
                    controller: 'GameVoessDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Game', function(Game) {
                            return Game.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('gamevoess', null, { reload: 'gamevoess' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

(function() {
    'use strict';

    angular
        .module('voess2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('user-watched-videovoess', {
            parent: 'entity',
            url: '/user-watched-videovoess',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'voess2App.userWatchedVideo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-watched-video/user-watched-videosvoess.html',
                    controller: 'UserWatchedVideoVoessController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userWatchedVideo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('user-watched-videovoess-detail', {
            parent: 'entity',
            url: '/user-watched-videovoess/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'voess2App.userWatchedVideo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-watched-video/user-watched-videovoess-detail.html',
                    controller: 'UserWatchedVideoVoessDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userWatchedVideo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'UserWatchedVideo', function($stateParams, UserWatchedVideo) {
                    return UserWatchedVideo.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'user-watched-videovoess',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('user-watched-videovoess-detail.edit', {
            parent: 'user-watched-videovoess-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-watched-video/user-watched-videovoess-dialog.html',
                    controller: 'UserWatchedVideoVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserWatchedVideo', function(UserWatchedVideo) {
                            return UserWatchedVideo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-watched-videovoess.new', {
            parent: 'user-watched-videovoess',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-watched-video/user-watched-videovoess-dialog.html',
                    controller: 'UserWatchedVideoVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                startDateTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('user-watched-videovoess', null, { reload: 'user-watched-videovoess' });
                }, function() {
                    $state.go('user-watched-videovoess');
                });
            }]
        })
        .state('user-watched-videovoess.edit', {
            parent: 'user-watched-videovoess',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-watched-video/user-watched-videovoess-dialog.html',
                    controller: 'UserWatchedVideoVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserWatchedVideo', function(UserWatchedVideo) {
                            return UserWatchedVideo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-watched-videovoess', null, { reload: 'user-watched-videovoess' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-watched-videovoess.delete', {
            parent: 'user-watched-videovoess',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-watched-video/user-watched-videovoess-delete-dialog.html',
                    controller: 'UserWatchedVideoVoessDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UserWatchedVideo', function(UserWatchedVideo) {
                            return UserWatchedVideo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-watched-videovoess', null, { reload: 'user-watched-videovoess' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

(function() {
    'use strict';

    angular
        .module('voess2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('user-fav-videovoess', {
            parent: 'entity',
            url: '/user-fav-videovoess',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'voess2App.userFavVideo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-fav-video/user-fav-videosvoess.html',
                    controller: 'UserFavVideoVoessController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userFavVideo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('user-fav-videovoess-detail', {
            parent: 'entity',
            url: '/user-fav-videovoess/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'voess2App.userFavVideo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-fav-video/user-fav-videovoess-detail.html',
                    controller: 'UserFavVideoVoessDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userFavVideo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'UserFavVideo', function($stateParams, UserFavVideo) {
                    return UserFavVideo.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'user-fav-videovoess',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('user-fav-videovoess-detail.edit', {
            parent: 'user-fav-videovoess-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-fav-video/user-fav-videovoess-dialog.html',
                    controller: 'UserFavVideoVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserFavVideo', function(UserFavVideo) {
                            return UserFavVideo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-fav-videovoess.new', {
            parent: 'user-fav-videovoess',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-fav-video/user-fav-videovoess-dialog.html',
                    controller: 'UserFavVideoVoessDialogController',
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
                    $state.go('user-fav-videovoess', null, { reload: 'user-fav-videovoess' });
                }, function() {
                    $state.go('user-fav-videovoess');
                });
            }]
        })
        .state('user-fav-videovoess.edit', {
            parent: 'user-fav-videovoess',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-fav-video/user-fav-videovoess-dialog.html',
                    controller: 'UserFavVideoVoessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserFavVideo', function(UserFavVideo) {
                            return UserFavVideo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-fav-videovoess', null, { reload: 'user-fav-videovoess' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-fav-videovoess.delete', {
            parent: 'user-fav-videovoess',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-fav-video/user-fav-videovoess-delete-dialog.html',
                    controller: 'UserFavVideoVoessDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UserFavVideo', function(UserFavVideo) {
                            return UserFavVideo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-fav-videovoess', null, { reload: 'user-fav-videovoess' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

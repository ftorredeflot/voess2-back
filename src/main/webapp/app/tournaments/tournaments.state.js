// (function() {
//     'use strict';
//
//     angular
//         .module('voess2App')
//         .config(stateConfig);
//
//     stateConfig.$inject = ['$stateProvider'];
//
//     function stateConfig($stateProvider) {
//         $stateProvider.state('tournaments', {
//             parent: 'app',
//             url: '/tournaments',
//             data: $stateProvider,
//             views: {
//                 'content@': {
//                     templateUrl: 'app/tournaments/tournaments.html',
//                     controller: 'TournamentVoessController',
//                     controllerAs: 'vm'
//                 }
//             },
//             resolve: {
//                 mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
//                     $translatePartialLoader.addPart('home');
//                     return $translate.refresh();
//                 }]
//             }
//         });
//     }
// })();
(function () {
    'use strict';

    angular
        .module('voess2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('AllTournamentVoessController', {
                parent: 'app',
                url: '/tournaments',
                views: {
                    'content@': {
                        templateUrl: 'app/tournaments/tournaments.html',
                        controller: 'TournamentVoessController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('tournament');
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('home');
                        return $translate.refresh();
                    }]
                }
            })
            //TODO
            .state('tournamentsvoess-detail', {
                        parent: 'app',
                        url: '/tournaments/{id}',
                        views: {
                            'content@': {
                                templateUrl: 'app/tournaments/tournamentsvoess-detail.html',
                                controller: 'TournamentVoessDetailController',
                                controllerAs: 'vm'
                            }
                        },
                        resolve: {
                            translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                                $translatePartialLoader.addPart('tournament');
                                return $translate.refresh();
                            }],
                            entity: ['$stateParams', 'Tournament', function($stateParams, Tournament) {
                                return Tournament.get({id : $stateParams.id}).$promise;
                            }],
                            previousState: ["$state", function ($state) {
                                var currentStateData = {
                                    name: $state.current.name || 'AllTournamentVoessController',
                                    params: $state.params,
                                    url: $state.href($state.current.name, $state.params)
                                };
                                return currentStateData;
                            }]
                        }
                    })
    }

})();

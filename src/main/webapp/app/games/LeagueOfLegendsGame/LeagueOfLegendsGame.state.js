(function() {
    'use strict';

    angular
        .module('voess2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('LeagueOfLegendsGame', {
            parent: 'app',
            url: '/games/LeagueOfLegendsGame',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/games/LeagueOfLegends/LeagueOfLegendsGame.html',
                    controller: 'HomeControler',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('home');
                    return $translate.refresh();
                }]
            }
        });
    }
})();



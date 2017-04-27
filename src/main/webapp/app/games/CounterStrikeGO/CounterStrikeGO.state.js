(function() {
    'use strict';

    angular
        .module('voess2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('CounterStrikeGO', {
            parent: 'app',
            url: '/games/CounterStrikeGO',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/games/CounterStrikeGO/CounterStrikeGO.html',
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

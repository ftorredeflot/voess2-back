(function() {
    'use strict';

    angular
        .module('voess2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('tournaments', {
            parent: 'app',
            url: '/tournaments',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/tournaments/tournaments.html',
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

(function() {
    'use strict';

    angular
        .module('voess2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('livestreamss', {
            parent: 'app',
            url: '/livestreams',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/livestreams/livestreams.html',
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

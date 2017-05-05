/**
 * Created by X2382383C on 03/05/2017.
 */
(function() {
    'use strict';
    angular
        .module('voess2App')
        .config(stateConfig);
    stateConfig.$inject = ['$stateProvider'];
    function stateConfig($stateProvider) {
        $stateProvider.state('listavideos', {
            parent: 'app',
            url: '/listavideos',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/listavideos/listavideos.html',
                    controller: 'listavideoscontroller',
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

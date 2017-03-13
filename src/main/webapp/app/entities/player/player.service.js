(function() {
    'use strict';
    angular
        .module('voess2App')
        .factory('Player', Player);

    Player.$inject = ['$resource'];

    function Player ($resource) {
        var resourceUrl =  'api/players/:id';
        var urls =  'api/user-exts/:id';
        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'patata': { method: 'GET', isArray: true, url:urls},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

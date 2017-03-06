(function() {
    'use strict';
    angular
        .module('voess2App')
        .factory('UserWatchedVideo', UserWatchedVideo);

    UserWatchedVideo.$inject = ['$resource', 'DateUtils'];

    function UserWatchedVideo ($resource, DateUtils) {
        var resourceUrl =  'api/user-watched-videos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startDateTime = DateUtils.convertDateTimeFromServer(data.startDateTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

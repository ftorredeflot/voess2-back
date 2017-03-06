(function() {
    'use strict';
    angular
        .module('voess2App')
        .factory('UserFavVideo', UserFavVideo);

    UserFavVideo.$inject = ['$resource', 'DateUtils'];

    function UserFavVideo ($resource, DateUtils) {
        var resourceUrl =  'api/user-fav-videos/:id';

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

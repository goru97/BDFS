/**
 * Created by mjain.
 */


singApp.controller('verifyTokenCtrl', function ($scope, $http, $state, $stateParams, ServerUrl, AuthenticationModel, $location) {

    var token = $stateParams.token;
        return $http.post(ServerUrl + '/verify', {token: token}).success(function(data)
        {
            $location.path('/login');
        }).error(function (data) {
            AuthenticationModel.errorMessage = data;
        });

});


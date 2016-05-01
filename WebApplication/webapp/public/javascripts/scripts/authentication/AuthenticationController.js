'use strict';

singApp.controller('AuthenticationCtrl', function ($scope, $http, $location, $window, ServerUrl, AuthenticationModel, $state) {

	$scope.username = null;
	$scope.password = null;
	$scope.name = null;

	$scope.AuthenticationModel = AuthenticationModel;

	$scope.signIn = function (username, password) {
		return $http.post(ServerUrl + '/api/auth/signin', {
			username: username,
			password: password
		}).success(function(data) {
            AuthenticationModel.setUser(data.user);
            $state.go('app.page', {page: 'about', child: null});
		}).error(function (data) {
			AuthenticationModel.removeUser();
			AuthenticationModel.errorMessage = data;
		});
	};

	$scope.signUp = function (username, password, name) {
		return $http.post(ServerUrl + '/api/auth/signup', {
			username: username,
			password: password,
			name: name
		}).success(function(data) {
			AuthenticationModel.setUser(data.user);
			$state.go('app.page', {page: 'about', child: null});
		}).error(function (data) {
            alert(data);
			AuthenticationModel.removeUser();
			AuthenticationModel.errorMessage = data;
		});
	};


});




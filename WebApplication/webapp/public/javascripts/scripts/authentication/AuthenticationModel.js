'use strict';

singApp.factory('AuthenticationModel', function ($http, $cookies) {

	this.user = $cookies.getObject('user');
	this.errorMessage = null;


	this.isSignedIn = function() {
		return !!this.user;
	};

	this.setUser = function(user) {
		this.errorMessage = null;
		this.user = user;
        var now = new Date();
        var exp = new Date(now.getFullYear(), now.getMonth(), now.getDate()+1);  //expiration to 1 day
        $cookies.putObject('user', user, {expires: exp, path: '/'});
	};

	this.removeUser = function() {
		this.user = null;
        $cookies.remove('user', {path: '/'});
	};

	return this;

});
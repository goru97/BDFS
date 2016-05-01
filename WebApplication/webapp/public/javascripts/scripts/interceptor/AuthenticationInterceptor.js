'use strict';

singApp.factory('AuthenticationInterceptor', function ($q, $injector) {
	return {
        request: function (config) {
            config.headers = config.headers || {};

           /* if ($localStorage.token) {
                config.headers.apitoken = $localStorage.token;
            }*/
            return config;
        },




		response: function (response) {
			// Bypass the success.

            //$timeout(function(){
            //    console.log($cookies.getAll());
            //});

            if(response.status == 403){
                alert('API token in valid.');
                var AuthenticationCtrl = $injector.get('AuthenticationCtrl');
                AuthenticationCtrl.signOut();
            }

			return response;
		},



		responseError: function (response) {
			// Sign out if the user is no longer authorized.
			if (response.status == 401) {
				var AuthenticationCtrl = $injector.get('AuthenticationCtrl');
				AuthenticationCtrl.signOut();
			}
			
			return $q.reject(response);     //https://docs.angularjs.org/api/ng/service/$q
		}
	};
});
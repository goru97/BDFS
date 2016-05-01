/**
 * Created by mjain
 */

exports = module.exports = function(app) {

	// Authentication
	app.post('/api/auth/signin', require('./SignInView').init);
	app.post('/api/auth/signup', require('./SignUpView').init);
	app.get('/api/auth/signout', require('./SignOutView').init);

	// Methods
	app.post('/verify', require('./VerifyEmail').verifyToken);

};
/**
 * Created by mjain
 */

exports = module.exports = function(app, mongoose) {
	require('./UserSchema')(app, mongoose);
	require('./EmailTokenSchema');
};
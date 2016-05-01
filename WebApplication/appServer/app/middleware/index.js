/**
 * Created by mjain
 */

exports = module.exports = function(app) {
	require('./CORSMiddleware')(app);
    require('./RedirectMiddleware')(app);
}
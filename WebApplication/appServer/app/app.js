/**
 * Created by mjain
 */

// External includes
var env = process.argv[2],
    express = require('express'),
    config = require('./../config'),
    http = require('http'),
    path = require('path'),
    mongoose = require('mongoose'),
    cookieParser = require('cookie-parser'),
    bodyParser = require('body-parser'),
    passport = require('passport'),
    session = require('express-session'),
    clientSession = require('client-sessions'),
    mongoStore = require('connect-mongo/es5')(session),
    logger = require('morgan'),
    methodOverride = require('method-override'),
    cluster = require('cluster')
    producer = require('./routes/kafka');

// Instantiate app
var app = express();

// General app config stuff
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.disable('x-powered-by');
//app.set('client-url', 'http://' + env + ':7000');
app.set('client-url', 'http://104.130.20.82:7000');

// Password encryption
app.set('crypto-key', config.cryptoKey);

// Setup mongoose
app.set('mongodb_uri', 'mongodb://localhost/hids');
app.db = mongoose.connect(app.get('mongodb_uri'));

app.set('port', process.env.PORT || 3100);

// Middlewares
app.use(logger('dev')); // log all requests to the console
app.use(bodyParser.json());

function defaultContentTypeMiddleware (req, res, next) {
    req.headers['content-type'] = req.headers['content-type'] || 'application/json';
    next();
}
app.use(defaultContentTypeMiddleware);

app.use(bodyParser.urlencoded({extended: true})); // use body parser so we can grab information from POST requests
app.use(cookieParser());
app.use(methodOverride());

// session secret
// refer: https://github.com/expressjs/session#options
app.use(session({
    secret: config.sessionSecret,
    resave: true,  //Forces the session to be saved back to the session store, even if the session was never modified during the request
    saveUninitialized: false,  //Forces a session that is "uninitialized" to be saved to the store. A session is uninitialized when it is new but not modified
    store: new mongoStore({url: app.get('mongodb_uri')})
}));

//https://stormpath.com/blog/everything-you-ever-wanted-to-know-about-node-dot-js-sessions/
app.use(clientSession({
    cookieName: 'apisession',
    secret: config.sessionSecret,
    duration: 30 * 60 * 1000,
    activeDuration: 5 * 60 * 1000,
    httpOnly: true
}));

//app.set('etag', false);
app.use(passport.initialize());
app.use(passport.session());

// Internal includes
var schemas = require('./schema/index')(app, mongoose),
    middlewares = require('./middleware/index')(app),
    routes = require('./routes/Controller')(app),
    strategies = require('./passport/index')(app, passport);
var apiRouter = require('./routes/secureRoutes');
var documents = require('./routes/documents');

app.use('/secure-api', apiRouter);
//app.use('/documents', documents);
app.use('/v2.0', producer);


// Start it all up
if (cluster.isMaster) {
    console.log('Master Process started.');

    for (var i = 0; i < 4; i++)
        cluster.fork();
} else {
    http.createServer(app).listen(app.get('port'), function () {
        console.log('Worker Process: Web Server for HIDS listening on port ' + app.get('port'));
    });
}


//single threaded server
/*http.createServer(app).listen(app.get('port'), function () {
 console.log('Web Server for HIDS listening on port ' + app.get('port'));
 });*/

exports = module.exports = app;


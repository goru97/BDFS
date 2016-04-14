    var express = require('express');
    var path = require('path');
    var logger = require('morgan');
    var cookieParser = require('cookie-parser');
    var bodyParser = require('body-parser');
    var cluster = require('cluster');
    var argv = require('optimist').argv;

    var routes = require('./routes/httpScheduler');

    var app = express();
    app.set('views', path.join(__dirname, 'views'));
    app.set('view engine', 'ejs');
    app.use(logger('dev'));
    app.use(bodyParser.json());
    app.use(bodyParser.urlencoded({ extended: false }));
    app.use(cookieParser());

    app.set('port', process.env.PORT || 9050);

    app.use('/', routes);


    var threads = argv.t;

    if(cluster.isMaster) {

        var numWorkers;
        if(threads == undefined)
            numWorkers  = require('os').cpus().length;
        else
            numWorkers = threads;

        console.log('Master cluster setting up ' + numWorkers + ' workers...');

        for(var i = 0; i < numWorkers; i++) {
            cluster.fork();
        }

        cluster.on('online', function(worker) {
            console.log('Worker ' + worker.process.pid + ' is online');
        });

        cluster.on('exit', function(worker, code, signal) {
            console.log('Worker ' + worker.process.pid + ' died with code: ' + code + ', and signal: ' + signal);
            console.log('Starting a new worker');
            cluster.fork();
        });
    } else {
        //var app = require('express')();
        app.all('/*', function(req, res) {res.send('process ' + process.pid + ' says hello!').end();})

        var server = app.listen(8000, function() {
            console.log('Process ' + process.pid + ' is listening to all incoming requests');
        });
    }


    exports = module.exports = app;
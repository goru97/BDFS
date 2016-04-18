var express = require('express');
var router = express.Router();
var every = require('every-moment'); // https://www.npmjs.com/package/every-moment
var http = require('http');
var argv = require('optimist').argv;

router.use(function (req, res, next) {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Headers', 'Content-Type,X-Requested-With');
    res.header('Access-Control-Allow-Credentials', 'true');
    next();
});


var domain = argv._[0];
var port = argv._[1];
var reqPath = argv._[2];
var interval = argv.i;

var getDataFromURL = function (timerObj) {

        return http.get({
                host: domain,
                port: port,
                path: '/' + reqPath

            }, function (response) {
                // Continuously update stream with data
                var body = '';
                var contentType = response.headers['content-type'].toLowerCase();
                response.on('data', function (d) {
                    body += d;
                });
                response.on('end', function () {

                    // Data reception is done, do whatever with it!
                    if (contentType.indexOf("application/json") !== -1){
                    var parsed = JSON.parse(body);
                    }
                    else {
                     parsed = body;
                     }
                    console.log("Process " + process.pid + " received: " + parsed);

                    /*callback({
                     email: parsed.email,
                     password: parsed.pass
                     });*/
                });
            })
            .on('error', function (error) {
                console.log("Exception occured: " + error );
                timerObj.stop();
                console.log("Timer stopped.")
            })
            .end();
};


var timerService = function () {

    if(interval == undefined)
        interval = 5;

    var timer = every(interval, 'seconds', function () {
        //console.log(this.duration);
        getDataFromURL(timer);
    });
};

timerService();


module.exports = router;
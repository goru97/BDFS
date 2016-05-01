/**
 * Created by mjain
 */

var jwt = require('jsonwebtoken');
var express = require('express');
var router = express.Router();
var config = require('../../config.json');
var secretKey = config.privateKey;

//middlware to use for all requests  // route middleware to verify a token
router.use(function (req, res, next) {
    // check header or url parameters or post parameters for token
    var token = req.body.token || req.query.token || req.headers['apitoken'] || req.session.apitoken;

    // verify the token
    if (token) {
        jwt.verify(token, secretKey, function (err, decoded) {
            if (err) {
                return res.json({success: false, message: 'Failed to authenticate token.'});
            } else {
                // if everything is good, save to request for use in other routes
                req.decoded = decoded;
                //console.log(decoded);

                next(); // make sure we go to the next routes and don't stop here
            }
        });
    } else {
        // if there is no token
        // return an HTTP response of 403 (access forbidden) and an error message
        return res.status(403).send({
            success: false,
            message: 'No token provided. Please Authenticate at \'/api/auth/signin\' with username & password'
        });
    }
});

// for other api routes. the authenticated routes
router.get('/', function (req, res) {
    res.json({message: "Welcome to HIDS API"});
});

module.exports = router;




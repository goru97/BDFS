/**
 * Created by mjain
 */

    var express = require('express');
    var app = express();
    var userModel = require('../schema/UserSchema');
    var verificationTokenModel = require('../schema/EmailTokenSchema');


    exports.verifyToken = function (req, res) {
        var token = req.body.token;

        var verifyUser = function (token, done) {
            verificationTokenModel.findOne({token: token}, function (err, doc) {
                if (err) return done(err);
                if (!doc.username) res.end("<h1>Email verification link has expired.!!</h1>");

                userModel.findOne({username: doc.username}, function (err, user) {
                    if (err) return done(err);
                    user["verified"] = true;
                    user.save(function (err) {
                        done(user, err);
                    });
                });
            });
        };

        verifyUser(token, function (user, err) {
            if (err) {
                console.log("email is not verified");
                res.end("<h1>Bad Request</h1>");
            } else {
                console.log("email verified");
                res.status(200).send("Email verified..!!");
            }
        });
    };




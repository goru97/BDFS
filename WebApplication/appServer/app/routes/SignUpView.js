/**
 * Created by mjain
 */

var config = require('../../config.json');
var sendgridKey = config.sendgridApiKey;
var User = require('../schema/UserSchema');
var verificationTokenModel = require('../schema/EmailTokenSchema');


exports.init = function (req, res) {

    var username = req.body.username,
        password = req.body.password,
        name = req.body.name;


    var sendVerificationEmail = function (token) {
        // using SendGrid's Node.js Library
        // https://github.com/sendgrid/sendgrid-nodejs
        var sendgrid = require("sendgrid")(sendgridKey);


        var clientUrl = req.app.get('client-url');
        var link = clientUrl + "/verify/" + token;
        var htmlContent = "Hello " + name + ",<br><br> Thank you for creating an account with HIDS." +
            "<br><br> Please Click on the link to verify your email.<br>" +
            "<br> <a href=" + link + ">Click here to verify</a>" +
            "<br><br>Please use google chrome browser for better experience.";

        var payload = {
            to: username,
            from: 'mayur.jain@sjsu.edu',
            subject: 'Please confirm your HIDS account',
            html: htmlContent
        };

        sendgrid.send(payload, function (err, responseJson) {
            if (err) {
                console.error(err);
            }
            else {
                console.log(responseJson);
                return res.status(500).send('Account verification link sent on email. Please verify your email.');
            }

        });
    };

    var createUser = function () {
        User.create({
            username: username,
            password: User.encryptPassword(password),
            name: name
        }, function (err, doc) {
            if (err) return res.send(500, err);

            User.findOne({username: username}, function (err, doc) {
                //before sign-in verify the email address
                var verificationToken = new verificationTokenModel({username: doc.username});
                verificationToken.createVerificationToken(function (err, token) {
                    if (err) return console.log("Couldn't create verification token", err);
                    sendVerificationEmail(token);
                });
            });
        });
    };

    var duplicateUserCheck = function () {
        User.findOne({username: username}, function (err, doc) {
            if (err) return res.send(500, err);
            if (doc) return res.status(400).send('Username already taken.');

            createUser();
        });
    };

    var validate = function () {
        if (!username) return res.send(400, 'Username required');
        if (!password) return res.send(400, 'Password required');

        if (!/^[a-zA-Z0-9\-\_\@\.]+$/.test(username)) {
            return res.send(400, 'Username only use letters, numbers, \'-\', \'_\'');
        }

        duplicateUserCheck();
    };

    validate();
};



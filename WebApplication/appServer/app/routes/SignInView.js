/**
 * Created by mjain
 */

var jwt = require('jsonwebtoken');
var config = require('../../config.json');
var secretKey = config.privateKey;

exports.init = function (req, res) {

    var passport = req._passport.instance;
    var username = req.body.username;
    var password = req.body.password;

    attemptLogin = function () {
        passport.authenticate('local', function (err, user, info) {
            if (err) {
                console.log(err);
                return res.status(400).send(err);
            }

            if (!user) {
                return res.status(400).send('Username and password combination not found.');
            } else {

                req.login(user, function (err) {
                    if (err) return res.send(500, err);
                    user.password = undefined;
                    user._id = undefined;
                    user.__v = undefined;
                    user.verified = undefined;
                    user.created_at = undefined;

                    var jwtToken = jwt.sign(
                        {name: user.name, username: user.username},
                        secretKey,
                        {expiresIn: 60 * 60 * 24} // 24 hours
                    );

                    req.session.apitoken = jwtToken;

                    res.header('apitoken', jwtToken);
                    res.status(200).send({user: user, success: true, message: 'Token generated', token: jwtToken});
                });
            }
        })(req, res);
    };

    var validate = function () {
        if (!username) return res.status(400).send('Username required');
        if (!password) return res.status(400).send('Password required');
        attemptLogin();
    };

    validate();
};


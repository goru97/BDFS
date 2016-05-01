/**
 * Created by mjain
 */

exports.init = function (req, res) {
    req.logout();
    req.apisession.reset();
    res.sendStatus(200);
};
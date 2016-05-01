/**
 * Created by mjain
 */

var mongoose = require('mongoose');

UserSchema = new mongoose.Schema({
    username: {type: String, required: true},
    name: {type: String},
    password: {type: String},
    verified: false,
    created_at: {type: Date, default: Date.now}
});

UserSchema.statics.encryptPassword = function (password) {
    return require('crypto').createHmac('sha512', 'k3yb0ardc4t').update(password).digest('hex');
};

exports = module.exports = mongoose.model('User', UserSchema);




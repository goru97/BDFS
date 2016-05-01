/**
 * Created by mjain
 */

var uuid = require('node-uuid');
var mongoose = require('mongoose');
var User = require('./UserSchema');


// Verification token model
var verificationTokenSchema = new mongoose.Schema({
    //_userId: {type: mongoose.Schema.ObjectId, required: true, ref: 'User'},
    username: {type: String, required: true, ref: 'User', unique: true},
    token: {type: String, required: true},
    createdAt: {type: Date, required: true, default: Date.now, expires: '4h'}
});

//function to set the token and save the model, and then return the token in a callback.

verificationTokenSchema.methods.createVerificationToken = function (done) {
    var verificationToken = this;
    var token = uuid.v4();
    verificationToken.set('token', token);
    verificationToken.save( function (err) {
        if (err) return done(err);
        //console.log('Verification token: ' + token);
        return done(null, token);
    });
};

//Create the and export the model:
var verificationTokenModel = mongoose.model('VerificationToken', verificationTokenSchema);
exports.verificationTokenModel = verificationTokenModel;

exports =  module.exports = mongoose.model('VerificationToken', verificationTokenSchema);




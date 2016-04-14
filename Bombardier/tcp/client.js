/**
 * Created by mjain on 4/14/16.
 */

var net = require('net');
var argv = require('optimist').argv;

var host = argv.h;
var port = argv.p;

var client = new net.Socket();
client.connect(port, host, function() {
    console.log('Connected');
    client.write('Hello, server! -- from client.');
});

client.on('data', function(data) {
    console.log('Received: ' + data);
    // kill client after server's response
    //client.destroy();
});

client.on('close', function() {
    console.log('TCP Connection closed');
});


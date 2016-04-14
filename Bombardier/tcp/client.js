/**
 * Created by mjain on 4/14/16.
 */

var net = require('net');

var client = new net.Socket();
client.connect(1337, '127.0.0.1', function() {
    console.log('Connected');
    client.write('Hello, server!' + '\r\n -- from client.');
});

client.on('data', function(data) {
    console.log('Received: ' + data);
    // kill client after server's response
    //client.destroy();
});

client.on('close', function() {
    console.log('TCP Connection closed');
});


/**
 * Created by mjain on 4/14/16.
 */

var net = require('net');
var argv = require('optimist').argv;

var host = argv.h;
var port = argv.p;

var server = net.createServer(function(socket) {
    socket.write('Ack from server\r\n');
    socket.pipe(socket);
});

server.listen(port, host, function(){
    console.log('Process ' + process.pid + ' is listening to all incoming requests');
});
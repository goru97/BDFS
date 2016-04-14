/**
 * Created by mjain on 4/14/16.
 */

var net = require('net');

var server = net.createServer(function(socket) {
    socket.write('Ack from server\r\n');
    socket.pipe(socket);
});

server.listen(1337, '127.0.0.1', function(){
    console.log('Process ' + process.pid + ' is listening to all incoming requests');
});
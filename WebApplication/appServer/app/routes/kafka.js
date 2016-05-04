/**
 * Created by mjain
 */

var kafka = require('kafka-node'),
    Producer = kafka.Producer,
    client = new kafka.Client('localhost:2181','test-client'),
    producer = new Producer(client),
    producer_ready = false;

exports.producer = function(req, res){

        if(producer_ready === true){
            var payloads = [
                { topic: req.params.tenantId, messages: JSON.stringify(req.body), partition: 0 },
            ];

            producer.send(payloads, function(err, data){
                if (err) {
                    res.status(500).send(err);
                } else {
                    res.status(200).send('Message is queued...');
                }
            });
        }
        else {
            res.status(500).send('Producer not available');
        }
};


producer.on('ready', function(){
    console.log("Connected to Kafka");
    producer_ready = true;
});

producer.on('error', function(err){
    producer_ready = false;
    console.log("Error connecting Kafka");
});
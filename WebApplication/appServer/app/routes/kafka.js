/**
 * Created by gauravbajaj on 4/23/16.
 */
var express = require('express');
var router = express.Router();
var kafka = require('kafka-node'),
    Producer = kafka.Producer,
    client = new kafka.Client('localhost:2181','test-client'),
    producer = new Producer(client),
    producer_ready = false;

router.post('/:tenantId/ingest', function(req, res, next) {

    if(producer_ready === true){
        payloads = [
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
        res.status(500)
        res.send('Producer not available')
    }
});

module.exports = router;

producer.on('ready', function(){
    console.log("Connected to Kafka")
    producer_ready = true;
});

producer.on('error', function(err){
    producer_ready = false;
    console.log("Error connecting Kafka")
})

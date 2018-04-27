
const express = require('express')
const app = express()
var XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;
var requestBody = ""; 
var btoa = require('btoa')
var client = new XMLHttpRequest()
const amqp = require('amqplib/callback_api');
let channel;

var bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
 
console.log('connecting');
    amqp.connect('amqp://localhost', function(err, conn) {
      if (err) {
        console.log('Error connecting producer');
        console.log(err);
      }

      console.log('Producer connected to RabbitMQ');

      conn.createChannel(function(err, ch) {
        console.log('channel created');
        channel = ch;
      });
    });

app.get('/tickets', (req, res) => {
  
	var ex = 'ITSM';
        //var msg = process.argv.slice(2).join(' ');
        let payload = {
          method: 'get',
	  data: {}
        };

        channel.assertExchange(ex, 'fanout', { durable: false });
        channel.publish(ex, 'req_queue', new Buffer(JSON.stringify(payload)));
       
	console.log('[x] Sent %s', JSON.stringify(payload));
	var q = 'res_queue';

	channel.assertQueue(q, {durable: false});
	channel.prefetch(1);
	console.log('[x] Awaiting response');
	channel.consume(q, function reply(msg) {
		console.log('[x] Responding...');
  		res.send(msg.content.toString());

 // 		channel.ack(msg);
	});
})

app.post('/tickets', (req, res) => {
  let ex = 'ITSM';
  console.log(req.body);
  let payload = {method: 'post', data: req.body};
  channel.assertExchange(ex, 'fanout', { durable: false });
        channel.publish(ex, 'req_queue', new Buffer(JSON.stringify(payload)));
       
        console.log('[x] Sent %s', JSON.stringify(payload));
        var q = 'res_queue';

        channel.assertQueue(q, {durable: false});
        channel.prefetch(1);
        console.log('[x] Awaiting response');
        channel.consume(q, function reply(msg) {
                console.log('[x] Responding...');
console.log(msg.content.toString());
                res.send(msg.content.toString());

 //             channel.ack(msg);
        });
//res.send('sdf');
})

app.put('/tickets', (req, res) => {
  let ex = 'ITSM';
  console.log(req.body);
  let payload = {method: 'put', id: req.body.id, data: req.body};
  channel.assertExchange(ex, 'fanout', { durable: false });
        channel.publish(ex, 'req_queue', new Buffer(JSON.stringify(payload)));
       
        console.log('[x] Sent %s', JSON.stringify(payload));
        var q = 'res_queue';

        channel.assertQueue(q, {durable: false});
        channel.prefetch(1);
        console.log('[x] Awaiting response');
        channel.consume(q, function reply(msg) {
                console.log('[x] Responding...');
console.log(msg.content.toString());
		res.send(msg.content.toString());                

 //             channel.ack(msg);
        });
//res.send('sdf');
})

app.delete('/tickets', (req, res) => {
  let ex = 'ITSM';
  console.log(req.body);
  let payload = {method: 'delete', id: req.body.id};
  channel.assertExchange(ex, 'fanout', { durable: false });
        channel.publish(ex, 'req_queue', new Buffer(JSON.stringify(payload)));
       
        console.log('[x] Sent %s', JSON.stringify(payload));
        var q = 'res_queue';

        channel.assertQueue(q, {durable: false});
        channel.prefetch(1);
        console.log('[x] Awaiting response');
        channel.consume(q, function reply(msg) {
                console.log('[x] Responding...');
console.log(msg.content.toString());
                res.send('DELETED ticket with sys_id: ' + payload.id);

 //             channel.ack(msg);
        });
//res.send('sdf');
})

app.listen(3000, () => console.log('Server running on port 3000'))

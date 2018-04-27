var amqp = require('amqplib/callback_api');

// connect to RabbitMQ server
// create a channel, which is where most of the API for getting things done resides
// To send, we must declare a queue for us to send to; then we can publish a message to the queue
amqp.connect('amqp://localhost', function(err, conn) {
  conn.createChannel(function(err, ch) {
    var q = 'hello';

    ch.assertQueue(q, { durable: false });
    // Note: on Node 6 Buffer.from(msg) should be used
    ch.sendToQueue(q, new Buffer('Hello World!'));
    console.log(" [x] Sent 'Hello World!'");
  });

  // close the connection and exit
  setTimeout(function() {
    conn.close();
    process.exit(0);
  }, 500);
});

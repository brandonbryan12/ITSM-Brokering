var amqp = require('amqplib/callback_api');

// Note that we declare the queue here, as well.
// Because we might start the consumer before the publisher, we want to make sure the queue exists before we try to consume messages from it.
amqp.connect('amqp://localhost', function(err, conn) {
  conn.createChannel(function(err, ch) {
    var q = 'hello';

    ch.assertQueue(q, { durable: false });

    // We're about to tell the server to deliver us the messages from the queue.
    // Since it will push us messages asynchronously, we provide a callback that will be executed when RabbitMQ pushes messages to our consumer.
    // This is what Channel.consume does.
    console.log(' [*] Waiting for messages in %s. To exit press CTRL+C', q);
    ch.consume(
      q,
      function(msg) {
        console.log(' [x] Received %s', msg.content.toString());
      },
      { noAck: true }
    );
  });
});

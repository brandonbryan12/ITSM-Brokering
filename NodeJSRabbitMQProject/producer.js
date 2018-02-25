var amqp = require('amqplib/callback_api');

amqp.connect('amqp://localhost', function(err, conn) {
  conn.createChannel(function(err, ch) {
    var ex = 'ITSM';
    //var msg = process.argv.slice(2).join(' ');
    let payload = { acct: 'Walmart', method: 'create' };

    ch.assertExchange(ex, 'fanout', { durable: false });
    ch.publish(ex, '', new Buffer(JSON.stringify(payload)));
    console.log(' [x] Sent %s', JSON.stringify(payload));
  });

  setTimeout(function() {
    conn.close();
    process.exit(0);
  }, 500);
});

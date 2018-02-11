var amqp = require('amqplib/callback_api');

amqp.connect('amqp://localhost', function(err, conn) {
  conn.createChannel(function(err, ch) {
    var ex = 'direct_logs';
    var args = process.argv.slice(2);
    var msg = args.slice(1).join(' ') || 'Hello World!';
    var account = args.length > 0 ? args[0] : 'info';

    var service = getServiceFromAccount(account);

    ch.assertExchange(ex, 'direct', { durable: false });
    ch.publish(ex, service, new Buffer(msg));
    console.log(" [x] Sent %s: '%s'", service, msg);
  });

  setTimeout(function() {
    conn.close();
    process.exit(0);
  }, 500);
});

function getServiceFromAccount(account) {
  // Delete this and replace with http request
  switch (account) {
    case 'walmart':
      service = 'sn';
      break;
    case 'target':
      service = 'sf';
      break;
  }
}

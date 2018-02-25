const amqp = require('amqplib/callback_api');
const xmlhttp = require('xmlhttprequest');

module.exports = class Consumer {
  constructor(name) {
    this.consumerName = name;
    this.consumer = this;
  }

  connect() {
    amqp.connect('amqp://localhost', function(err, conn) {
      conn.createChannel(function(err, ch) {
        var ex = 'ITSM';

        ch.assertExchange(ex, 'fanout', { durable: false });

        ch.assertQueue('', { exclusive: true }, function(err, q) {
          console.log(
            ' [*] Waiting for messages in %s. To exit press CTRL+C',
            q.queue
          );
          ch.bindQueue(q.queue, ex, '');

          ch.consume(
            q.queue,
            function(msg) {
              let account = msg;
              httpGetAsync(
                'http://127.0.0.1:8080/Lookup?providerName=' +
                  account.content.toString(),
                onHTTPResponseFromLookup,
                ch,
                ex,
                msg
              );
            },
            { noAck: true }
          );
        });
      });
    });
  }
};

function onHTTPResponseFromLookup(responseText, ch, ex, msg) {
  console.log(' [x] %s', responseText);
}

function httpGetAsync(urlString, callback, ch, ex, msg) {
  var XMLHttpRequest = xmlhttp.XMLHttpRequest;
  var xmlHttp = new XMLHttpRequest();
  xmlHttp.onreadystatechange = function() {
    if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
      callback(xmlHttp.responseText, ch, ex, msg);
  };
  xmlHttp.open('GET', urlString, true);
  xmlHttp.send(null);
}

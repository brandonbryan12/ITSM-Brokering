var amqp = require('amqplib/callback_api');
var xmlhttp = require('xmlhttprequest');

amqp.connect('amqp://localhost', function(err, conn) {
  conn.createChannel(function(err, ch) {
    var ex = 'direct_logs';
    var args = process.argv.slice(2);
    var msg = args.slice(1).join(' ') || 'Hello World!';
    var account = args.length > 0 ? args[0] : 'info';
    httpGetAsync(
      'http://127.0.0.1:8080/Lookup?providerName=' + account,
      onHTTPResponseFromLookup,
      ch,
      ex,
      msg
    );
  });
  setTimeout(function() {
    conn.close();
    process.exit(0);
  }, 500);
});

function onHTTPResponseFromLookup(responseText, ch, ex, msg) {
  ch.assertExchange(ex, 'direct', { durable: false });
  ch.publish(ex, responseText, new Buffer(msg));
  console.log(' [x] Sent %s', responseText);
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

const amqp = require('amqplib/callback_api');
const xmlhttp = require('xmlhttprequest');

module.exports = class Consumer {
  constructor(name) {
    this.consumerName = name;
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

          var self = this;

          ch.consume(
            q.queue,
            function(msg) {
              //console.log(this.consumerName);
              let payload = JSON.parse(msg.content.toString());
              console.log(JSON.stringify(payload));
              httpGetAsync(
                'http://127.0.0.1:8080/Lookup?providerName=' + payload.acct,
                onHTTPResponseFromLookup,
                ch,
                ex,
                payload,
                'sn'
              );
            },
            { noAck: true }
          );
        });
      });
    });
  }
};

function onHTTPResponseFromLookup(responseText, ch, ex, payload, consumerName) {
  console.log(' [x] %s', responseText);
  console.log(consumerName);
  if (consumerName == responseText) {
    // translate
    translate(payload);
    // call
  }
}

function httpGetAsync(urlString, callback, ch, ex, msg, consumerName) {
  var XMLHttpRequest = xmlhttp.XMLHttpRequest;
  var xmlHttp = new XMLHttpRequest();
  xmlHttp.onreadystatechange = function() {
    if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
      callback(xmlHttp.responseText, ch, ex, msg, consumerName);
  };
  xmlHttp.open('GET', urlString, true);
  xmlHttp.send(null);
}

function translate(payload) {
  if (payload.method == 'create') {
    console.log('in');
    // if !callerid || !short_desc
    // return
    /*const attributes = ['company', 'locaiton', 'category', 'subcategory'];

    let requestBody = {
      caller_id: '',
      short_description: ''
    };

    for (let i = 0; i < attributes.length; i++) {
      if (attributes[i] in msg) {
        requestBody[attributes[i]] = msg[attributes[i]];
      }
    }

    var XMLHttpRequest = xmlhttp.XMLHttpRequest;
    var client = new XMLHttpRequest();
    client.open(
      'post',
      'https://dev30188.service-now.com/api/now/table/incident?sysparm_limit=1'
    );

    client.setRequestHeader('Accept', 'application/json');
    client.setRequestHeader('Content-Type', 'application/json');

    //Eg. UserName="admin", Password="admin" for this code sample.
    client.setRequestHeader(
      'Authorization',
      'Basic ' + btoa('admin' + ':' + 'admin')
    );

    client.onreadystatechange = function() {
      if ((this.readyState = this.DONE)) {
        console.log(this.status + this.response);
      }
    };
    client.send(JSON.stringify(requestBody));*/
  }
}

const amqp = require('amqplib/callback_api');
const xmlhttp = require('xmlhttprequest');

module.exports = class Consumer {
  constructor(name) {
    this.consumerName = name;
  }

  connect() {
    let consumerName = this.consumerName;
    amqp.connect('amqp://localhost', function(err, conn) {
      conn.createChannel(function(err, ch) {
        ch.consumerName = consumerName;

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
              let payload = JSON.parse(msg.content.toString());

              httpGetAsync(
                'http://127.0.0.1:8080/Lookup?providerName=' + payload.acct,
                onHTTPResponseFromLookup,
                ch,
                ex,
                payload
              );
            },
            { noAck: true, arguments: self }
          );
        });
      });
    });
  }

  translate(payload) {
    console.log('Translating incoming payload');
    let data = payload.data;
    if (payload.method == 'create') {
      if (
        !data.hasOwnProperty('caller_id') ||
        !data.hasOwnProperty('short_description')
      ) {
        console.log(
          'Caller ID or Short Description not provided. Exiting call'
        );
        return;
      }

      const attributes = [
        'company',
        'location',
        'category',
        'subcategory',
        'business_service',
        'cmdb_ci',
        'contact_type',
        'state',
        'impact',
        'urgency',
        'priority',
        'assignment_group',
        'assigned_to'
      ];

      let requestData = {
        method: payload.method,
        body: {
          aller_id: data.caller_id,
          short_description: data.short_description
        }
      };

      for (let i = 0; i < attributes.length; i++) {
        if (attributes[i] in data) {
          requestData.body[attributes[i]] = data[attributes[i]];
        }
      }

      return requestData;
    }
  }
};

function onHTTPResponseFromLookup(responseText, ch, ex, payload) {
  if (ch.consumerName == responseText) {
    // translate
    let requestData = translate(payload);
    // call
    callService(requestData);
  } else {
    console.log(
      'Ignoring. User is service %s while consumer is service %s',
      responseText,
      ch.consumerName
    );
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

function callService(requestData) {
  console.log(requestData);
}

function translate(payload) {
  console.log('Translating incoming payload');
  let data = payload.data;
  if (payload.method == 'create') {
    if (
      !data.hasOwnProperty('caller_id') ||
      !data.hasOwnProperty('short_description')
    ) {
      console.log('Caller ID or Short Description not provided. Exiting call');
      return;
    }

    const attributes = [
      'company',
      'location',
      'category',
      'subcategory',
      'business_service',
      'cmdb_ci',
      'contact_type',
      'state',
      'impact',
      'urgency',
      'priority',
      'assignment_group',
      'assigned_to'
    ];

    let requestData = {
      method: payload.method,
      body: {
        aller_id: data.caller_id,
        short_description: data.short_description
      }
    };

    for (let i = 0; i < attributes.length; i++) {
      if (attributes[i] in data) {
        requestData.body[attributes[i]] = data[attributes[i]];
      }
    }

    return requestData;
  }
}

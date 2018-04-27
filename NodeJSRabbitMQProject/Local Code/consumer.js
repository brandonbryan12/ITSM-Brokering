const amqp = require('amqplib/callback_api');
const xmlhttp = require('xmlhttprequest');
const express = require('express');
const app = express();

module.exports = class Consumer {
  constructor(name) {
    this.consumerName = name;
  }

  connect() {
    let consumerName = this.consumerName;
    let server = app.listen(5672, function() {
      console.log('Listening on port 5672');
    });

    server.on('error', function(e) {
      console.log(e);
      // put your code here
    });
    amqp.connect('amqp://localhost', function(err, conn) {
      if (err) {
        console.log(err);
      }

      /*conn.createChannel(function(err, ch) {
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

              //httpGetAsync(
                //'http://127.0.0.1:8080/Lookup?providerName=' + payload.acct,
                //onHTTPResponseFromLookup,
                //ch,
                //ex,
                //payload
              //);
              // translate
              let requestData = translate(payload);
              // call
              callService(requestData);
            },
            { noAck: true, arguments: self }
          );
        });
      });*/
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
  switch (requestData.method) {
    case 'put':
      restPut(requestData);
      break;
    case 'post':
      restPost(requestData);
      break;
    case 'get':
      restGet(requestData);
      break;
    case 'delete':
      restDelete(requestData);
      break;
  }
}

function translate(payload) {
  console.log('Translating incoming payload');
  let data = payload.data;

  switch (payload.method) {
    case 'post':
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
      break;

    case 'put':
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
        id: payload.id,
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
      break;

    case 'get':
      let requestData = {
        method: payload.method,
        body: {}
      };
      return requestData;
      break;

    case 'delete':
      let requestData = {
        method: payload.method,
        id: payload.id,
        body: {}
      };
      return requestData;
      break;
  }
}

function restGet(requestData) {
  var XMLHttpRequest = require('xmlhttprequest').XMLHttpRequest;
  var btoa = require('btoa');
  var client = new XMLHttpRequest();

  client.open(
    'get',
    'https://dev33012.service-now.com/api/now/table/incident?sysparm_limit=10000'
  );
  client.setRequestHeader('Accept', 'application/json');
  client.setRequestHeader('Content-Type', 'application/json');

  //Eg. UserName="admin", Password="admin" for this code sample.
  client.setRequestHeader(
    'Authorization',
    'Basic ' + btoa('admin' + ':' + 'SEseniorproject1!')
  );

  client.onreadystatechange = function() {
    if (this.readyState == this.DONE) {
      console.log(this.status);
      console.log(this.responseText);
    }
  };
  client.send(JSON.stringify(requestData.body));
}

function restPost(requestData) {
  var XMLHttpRequest = require('xmlhttprequest').XMLHttpRequest;
  var btoa = require('btoa');
  var client = new XMLHttpRequest();

  client.open(
    'post',
    'https://dev33012.service-now.com/api/now/table/incident?sysparm_limit=1'
  );
  client.setRequestHeader('Accept', 'application/json');
  client.setRequestHeader('Content-Type', 'application/json');

  //Eg. UserName="admin", Password="admin" for this code sample.
  client.setRequestHeader(
    'Authorization',
    'Basic ' + btoa('admin' + ':' + 'SEseniorproject1!')
  );

  client.onreadystatechange = function() {
    if (this.readyState == this.DONE) {
      console.log(this.status);
      console.log(this.responseText);
    }
  };
  client.send(JSON.stringify(requestData.body));
}

function restPut(requestData) {
  var XMLHttpRequest = require('xmlhttprequest').XMLHttpRequest;
  var btoa = require('btoa');
  var client = new XMLHttpRequest();

  client.open(
    'put',
    'https://dev33012.service-now.com/api/now/table/incident/' +
      requestData.id +
      '?state=2'
  );
  client.setRequestHeader('Accept', 'application/json');
  client.setRequestHeader('Content-Type', 'application/json');

  //Eg. UserName="admin", Password="admin" for this code sample.
  client.setRequestHeader(
    'Authorization',
    'Basic ' + btoa('admin' + ':' + 'SEseniorproject1!')
  );

  client.onreadystatechange = function() {
    if (this.readyState == this.DONE) {
      console.log(this.status);
      console.log(this.responseText);
    }
  };
  client.send(JSON.stringify(requestData.body));
}

function restDelete(requestData) {
  var XMLHttpRequest = require('xmlhttprequest').XMLHttpRequest;
  var btoa = require('btoa');
  var client = new XMLHttpRequest();

  client.open(
    'delete',
    'https://dev33012.service-now.com/api/now/table/incident/' + requestData.id
  );
  client.setRequestHeader('Accept', 'application/json');
  client.setRequestHeader('Content-Type', 'application/json');

  //Eg. UserName="admin", Password="admin" for this code sample.
  client.setRequestHeader(
    'Authorization',
    'Basic ' + btoa('admin' + ':' + 'SEseniorproject1!')
  );

  client.onreadystatechange = function() {
    if (this.readyState == this.DONE) {
      console.log(this.status);
      console.log(this.responseText);
    }
  };
  client.send(JSON.stringify(requestData.body));
}

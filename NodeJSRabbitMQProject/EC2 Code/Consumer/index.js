const xmlhttp = require('xmlhttprequest');
const amqp = require('amqplib/callback_api');
const consumerName = 'SN';

amqp.connect('amqp://consumer:pass@34.201.43.65', function(err, conn) {
      if (err) {
        console.log("ERROR");
	console.log(err);
      }
  console.log('connected');

conn.createChannel(function(err, ch) {
	console.log(err);
        ch.consumerName = consumerName;

        var ex = 'ITSM';

        ch.assertExchange(ex, 'fanout', { durable: false });

        ch.assertQueue('req_queue', { exclusive: false }, function(err, q) {
console.log(err);
          console.log(
            ' [*] Waiting for messages in %s. To exit press CTRL+C',
            q.queue
          );
          ch.bindQueue(q.queue, ex, '');

//          var self = this;

          ch.consume(
            q.queue,
            function(msg){
              let payload = JSON.parse(msg.content.toString());
              console.log(payload);
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
              callService(requestData, ch, q);


	

		
               },
            { noAck: true}
          );
        });
      });
});

function callService(requestData, ch, q) {
  switch (requestData.method) {
    case 'put':
      restPut(requestData, ch, q);
      break;
    case 'post':
      restPost(requestData, ch, q);
      break;
    case 'get':
      restGet(requestData, ch, q);
      break;
    case 'delete':
      restDelete(requestData, ch, q);
      break;
  }
}

function translate(payload) {
  console.log('Translating incoming payload');
  let data = payload.data;
  let requestData;
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
      requestData = {
        method: payload.method,
        body: {
          caller_id: data.caller_id,
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
      

      requestData = {
        method: payload.method,
        id: payload.id,
        body: {
          caller_id: data.caller_id,
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
      requestData = {
        method: payload.method,
        body: {}
      };
      return requestData;
      break;

    case 'delete':
      requestData = {
        method: payload.method,
        id: payload.id,
        body: {}
      };
      return requestData;
      break;
  }
}

function respond(res, ch, q) {
console.log(res);
ch.sendToQueue('res_queue',
      new Buffer(res),
      { correlationId: '123', replyTo: q.queue });
}

function restGet(requestData, ch, q) {
  var XMLHttpRequest = require('xmlhttprequest').XMLHttpRequest;
  var btoa = require('btoa');
  var client = new XMLHttpRequest();

  client.open(
    'get',
    'https://dev43297.service-now.com/api/now/table/incident?sysparm_limit=10000'
  );
  client.setRequestHeader('Accept', 'application/json');
  client.setRequestHeader('Content-Type', 'application/json');

  //Eg. UserName="admin", Password="admin" for this code sample.
  client.setRequestHeader(
    'Authorization',
    'Basic ' + btoa('admin' + ':' + 'Shadow12!')
  );

  client.onreadystatechange = function() {
    if (this.readyState == this.DONE) {
      console.log(this.status);
      respond(this.responseText, ch, q);
    }
  };
  client.send(JSON.stringify(requestData.body));
}

function restPost(requestData, ch, q) {
  var XMLHttpRequest = require('xmlhttprequest').XMLHttpRequest;
  var btoa = require('btoa');
  var client = new XMLHttpRequest();

  client.open(
    'post',
    'https://dev43297.service-now.com/api/now/table/incident?sysparm_limit=1'
  );
  client.setRequestHeader('Accept', 'application/json');
  client.setRequestHeader('Content-Type', 'application/json');

  //Eg. UserName="admin", Password="admin" for this code sample.
  client.setRequestHeader(
    'Authorization',
    'Basic ' + btoa('admin' + ':' + 'Shadow12!')
  );

  client.onreadystatechange = function() {
    if (this.readyState == this.DONE) {
      console.log(this.status);
      respond(this.responseText, ch, q);
    }
  };
  client.send(JSON.stringify(requestData.body));
}

function restPut(requestData, ch, q) {
  var XMLHttpRequest = require('xmlhttprequest').XMLHttpRequest;
  var btoa = require('btoa');
  var client = new XMLHttpRequest();

  client.open(
    'put',
    'https://dev43297.service-now.com/api/now/table/incident/' +
      requestData.id +
      '?state=2'
  );
  client.setRequestHeader('Accept', 'application/json');
  client.setRequestHeader('Content-Type', 'application/json');

  //Eg. UserName="admin", Password="admin" for this code sample.
  client.setRequestHeader(
    'Authorization',
    'Basic ' + btoa('admin' + ':' + 'Shadow12!')
  );

  client.onreadystatechange = function() {
    if (this.readyState == this.DONE) {
      console.log(this.status);
      respond(this.responseText, ch, q);
    }
  };
  client.send(JSON.stringify(requestData.body));
}

function restDelete(requestData, ch, q) {
  var XMLHttpRequest = require('xmlhttprequest').XMLHttpRequest;
  var btoa = require('btoa');
  var client = new XMLHttpRequest();

  client.open(
    'delete',
    'https://dev43297.service-now.com/api/now/table/incident/' + requestData.id
  );
  client.setRequestHeader('Accept', 'application/json');
  client.setRequestHeader('Content-Type', 'application/json');

  //Eg. UserName="admin", Password="admin" for this code sample.
  client.setRequestHeader(
    'Authorization',
    'Basic ' + btoa('admin' + ':' + 'Shadow12!')
  );

  client.onreadystatechange = function() {
    if (this.readyState == this.DONE) {
      console.log(this.status);
      respond(this.responseText, ch, q);
    }
  };
  client.send(JSON.stringify(requestData.body));
}

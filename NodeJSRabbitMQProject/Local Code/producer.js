var amqp = require('amqplib/callback_api');

module.exports = class Producer {
  connect() {
    console.log('connecting');
    amqp.connect('amqp://localhost', function(err, conn) {
      if (err) {
        console.log('Error connecting producer');
        console.log(err);
      }

      console.log('Producer connected to RabbitMQ');

      conn.createChannel(function(err, ch) {
        var ex = 'ITSM';
        //var msg = process.argv.slice(2).join(' ');
        let payload = {
          acct: 'Walmart',
          method: 'post',
          data: {
            caller_id: '1',
            category: 'network',
            subcategory: 'email',
            business_service: 'Client Services',
            cmdb_ci: 'b0c4030ac0a800090152e7a4564ca36c',
            contact_type: 'phone',
            state: '1',
            impact: '3',
            urgency: '3',
            priority: '5',
            assignment_group: '287ebd7da9fe198100f92cc8d1d2154e',
            assigned_to: '',
            short_description: 'Short desc'
          }
        };

        ch.assertExchange(ex, 'fanout', { durable: false });
        ch.publish(ex, '', new Buffer(JSON.stringify(payload)));
        console.log(' [x] Sent %s', JSON.stringify(payload));
      });

      setTimeout(function() {
        conn.close();
        process.exit(0);
      }, 500);
    });
  }
};

const assert = require('assert');
const XMLHttpRequest = require('xmlhttprequest').XMLHttpRequest;
const btoa = require('btoa');
let ticket_sys_id;

describe('Ticket', () => {
  it('is translated', () => {
    //assert(true);
    const Consumer = require('./../consumer');

    const consumer = new Consumer('sn');

    let payload = {
      acct: 'Walmart',
      method: 'create',
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
    assert.ok(consumer.translate(payload));
  });

  it('is created', done => {
    var requestBody =
      "{'short_description':'Unable to connect to office wifi','urgency':'2','impact':'2'}";
    var client = new XMLHttpRequest();

    client.open(
      'POST',
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
        const res = JSON.parse(this.responseText);
        ticket_sys_id = res.result.sys_id;
        assert(this.status == 201);
        done();
      }
    };
    client.send(requestBody);
  });

  it('is read', done => {
    var requestBody = '';
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
        assert(this.status == 200);
        done();
      }
    };
    client.send(requestBody);
  });

  it('is updated', () => {
    var requestBody =
      "{'state':'2','short_description':'PUT TESTING IS WORKING AND HAS BEEN MODIFIED','urgency':'2','impact':'2'}";
    var client = new XMLHttpRequest();

    client.open(
      'put',
      'https://dev33012.service-now.com/api/now/table/incident/' +
        ticket_sys_id +
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
        assert(this.status == 200);
        done();
      }
    };
    client.send(requestBody);
  });

  it('is deleted', () => {
    var requestBody = '';
    var client = new XMLHttpRequest();

    client.open(
      'delete',
      'https://dev33012.service-now.com/api/now/table/incident/' + ticket_sys_id
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
    client.send(requestBody);
  });
});

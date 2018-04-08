const assert = require('assert');

describe('RabbitMQ', () => {
  it('creates producer', () => {
    const Producer = require('./../producer');

    const producer = new Producer();

    assert.ok(producer);
  });

  it('creates consumer', () => {
    const Consumer = require('./../consumer');

    const consumer = new Consumer('sn');

    assert.ok(consumer);
  });
});

package org.baggerspion.adder_service

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject
import io.vertx.kafka.client.consumer.KafkaConsumer
import io.vertx.kafka.client.producer.KafkaProducer
import io.vertx.kafka.client.producer.KafkaProducerRecord

class MainVerticle : AbstractVerticle() {
  override fun start(startFuture: Future<Void>) {
    val config = mutableMapOf<String, String>()
    config.put("bootstrap.servers", "localhost:9092");
    config.put("key.deserializer", "io.vertx.kafka.client.serialization.StringDeserializer");
    config.put("value.deserializer", "io.vertx.kafka.client.serialization.JsonObjectDeserializer");
    config.put("group.id", "my_group");
    config.put("auto.offset.reset", "earliest");
    config.put("enable.auto.commit", "false");

    val config_result = mutableMapOf<String, String>()
    config_result.put("bootstrap.servers", "localhost:9092")
    config_result.put("key.serializer", "io.vertx.kafka.client.serialization.StringSerializer")
    config_result.put("value.serializer", "io.vertx.kafka.client.serialization.JsonObjectSerializer")
    config_result.put("group.id", "my_group")
    config_result.put("auto.offset.reset", "earliest")
    config_result.put("enable.auto.commit", "false")

    val consumer = KafkaConsumer.create<String, JsonObject>(vertx, config, String::class.java, JsonObject::class.java)
    val producer = KafkaProducer.create<String, JsonObject>(vertx, config_result, String::class.java, JsonObject::class.java)

    consumer.handler() { handler ->
      val result = JsonObject()
        .put("uid", handler.value().getString("uid"))
        .put("result", handler.value().getInteger("operand1") + handler.value().getInteger("operand2"))

      val record = KafkaProducerRecord.create<String, JsonObject>("adder_result_queue", result)
      producer.write(record)
    }

    consumer.subscribe("adder_queue") { sub ->
      if (sub.succeeded()) {
        println("Consumer subscribed")
      } else {
        println("Consumer subscription failed: ${sub.cause()}")
      }
    }
  }
}

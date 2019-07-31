package org.baggerspion.adder_service

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject
import io.vertx.kafka.client.consumer.KafkaConsumer

class MainVerticle : AbstractVerticle() {
  override fun start(startFuture: Future<Void>) {
    val config = mutableMapOf<String, String>()
    config.put("bootstrap.servers", "localhost:9092");
    config.put("key.deserializer", "io.vertx.kafka.client.serialization.StringDeserializer");
    config.put("value.deserializer", "io.vertx.kafka.client.serialization.JsonObjectDeserializer");
    config.put("group.id", "my_group");
    config.put("auto.offset.reset", "earliest");
    config.put("enable.auto.commit", "false");

    val consumer = KafkaConsumer.create<String, JsonObject>(vertx, config, String::class.java, JsonObject::class.java)
    consumer.handler() { handler ->
      val uid = handler.value().getString("uid")
      val result = handler.value().getInteger("operand1") + handler.value().getInteger("operand2")
      println("Request: $uid, Result: $result")
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

package org.baggerspion.numbers_service

import io.vertx.core.AbstractVerticle
import io.vertx.core.AsyncResult;
import io.vertx.core.Future
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject
import io.vertx.kafka.client.consumer.KafkaConsumer
import io.vertx.kafka.client.producer.KafkaProducer
import io.vertx.kafka.client.producer.KafkaProducerRecord
import io.vertx.kafka.client.producer.RecordMetadata
import java.util.*

class MainVerticle : AbstractVerticle() {
  override fun start(startFuture: Future<Void>) {
    val config = mutableMapOf<String, String>()
    config.put("bootstrap.servers", "localhost:9092")
    config.put("key.serializer", "io.vertx.kafka.client.serialization.StringSerializer")
    config.put("value.serializer", "io.vertx.kafka.client.serialization.JsonObjectSerializer")
    config.put("group.id", "my_group")
    config.put("auto.offset.reset", "earliest")
    config.put("enable.auto.commit", "false")

    val config_result = mutableMapOf<String, String>()
    config_result.put("bootstrap.servers", "localhost:9092")
    config_result.put("key.serializer", "io.vertx.kafka.client.serialization.StringSerializer")
    config_result.put("value.serializer", "io.vertx.kafka.client.serialization.JsonObjectSerializer")
    config_result.put("group.id", "my_group")
    config_result.put("auto.offset.reset", "earliest")
    config_result.put("enable.auto.commit", "false")

    val producer = KafkaProducer.create<String, JsonObject>(vertx, config, String::class.java, JsonObject::class.java)
    val consumer = KafkaConsumer.create<String, JsonObject>(vertx, config_result, String::class.java, JsonObject::class.java)

    consumer.handler() { handler ->
      println("Request: ${handler.value().getString("uid")}. Result: ${handler.value().getInteger("result")}.")
    }

    consumer.subscribe("adder_result_queue") { sub ->
      if (sub.succeeded()) {
        println("Consumer subscribed")
      } else {
        println("Consumer subscription failed: ${sub.cause()}")
      }
    }

    vertx.setPeriodic(3000) { _ ->
      val uid = UUID.randomUUID().toString()
      val op1 = (1..10).shuffled().first()
      val op2 = (1..10).shuffled().first()

      val obj = JsonObject()
        .put("uid", uid)
        .put("operand1", op1)
        .put("operand2", op2)

      val record = KafkaProducerRecord.create<String, JsonObject>("adder_queue", obj)
      producer.send(record) { resp: AsyncResult<RecordMetadata> ->
        if (resp.succeeded()) {
          val meta: RecordMetadata = resp.result()
          println("Message " + record.value() + " written on topic=" + meta.getTopic() +
            ", partition=" + meta.getPartition() +
            ", offset=" + meta.getOffset())
        } else {
          println("Failed to post: ${record.value()}")
        }
      }
    }
  }
}

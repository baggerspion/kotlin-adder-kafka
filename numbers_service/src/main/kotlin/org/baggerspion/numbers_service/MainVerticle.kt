package org.baggerspion.numbers_service

import io.vertx.core.AbstractVerticle
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject
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

    val producer = KafkaProducer.create<String, JsonObject>(vertx, config, String::class.java, JsonObject::class.java)

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
          println("Status: Success, Message: ${record.value()}, Topic: ${meta.getTopic()}, Partition: ${meta.getPartition()}, Offset: ${meta.getOffset()}")
        } else {
          println("Status: Fail, Message: ${record.value()}")
        }
      }
    }
  }
}

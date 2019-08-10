package org.baggerspion.numbers_service

import io.vertx.core.AbstractVerticle
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kafka.client.producer.KafkaProducer
import io.vertx.kafka.client.producer.KafkaProducerRecord
import io.vertx.kafka.client.producer.RecordMetadata

import java.util.UUID

class MainVerticle : AbstractVerticle() {
  override fun start(startFuture: Future<Void>) {
    // Setup the router
    val router = Router.router(vertx)
    router.route().handler(BodyHandler.create())
    router.post("/add").handler(this::handleAdd)

    // Start the web server
    vertx.createHttpServer().requestHandler(router).listen(8080)
  }

  private fun handleAdd(routingContext: RoutingContext) {
    // Get the Json
    val obj = routingContext.getBodyAsJson()

    // Kafka client configuration
    val config = mutableMapOf<String, String>()
    config.put("bootstrap.servers", "localhost:9092")
    config.put("key.serializer", "io.vertx.kafka.client.serialization.StringSerializer")
    config.put("value.serializer", "io.vertx.kafka.client.serialization.JsonObjectSerializer")
    config.put("group.id", "my_group")
    config.put("auto.offset.reset", "earliest")
    config.put("enable.auto.commit", "false")

    // Add UUID to the object before sending to bus
    val uid = UUID.randomUUID().toString()
    obj.put("uid", uid)

    // Place the object on the bus
    val producer = KafkaProducer.create<String, JsonObject>(vertx, config, String::class.java, JsonObject::class.java)
    val record = KafkaProducerRecord.create<String, JsonObject>("adder", obj)

    // Send response
    val response = routingContext.response()
    producer.send(record) { resp: AsyncResult<RecordMetadata> ->
      if (resp.succeeded()) {
        response.putHeader("content-type", "application/json").end(obj.encodePrettily())
      } else {
        response.setStatusCode(400).end()
      }
    }
  }
}

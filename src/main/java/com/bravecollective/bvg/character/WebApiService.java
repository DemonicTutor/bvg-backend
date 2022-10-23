package com.bravecollective.bvg.character;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.web.api.service.ServiceRequest;
import io.vertx.ext.web.api.service.ServiceResponse;
import io.vertx.ext.web.api.service.WebApiServiceGen;

@WebApiServiceGen
@VertxGen
public interface WebApiService {

  static void create(final Vertx vertx) {
    WebApiServiceImpl.create(vertx);
  }

  Future<ServiceResponse> readCharacter(ServiceRequest request);
}

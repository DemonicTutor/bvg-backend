package com.bravecollective.bvg.character;

import com.bravecollective.bvg.esi.EsiClient;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.web.api.service.ServiceRequest;
import io.vertx.ext.web.api.service.ServiceResponse;
import io.vertx.rxjava3.SingleHelper;
import io.vertx.serviceproxy.ServiceBinder;

final class WebApiServiceImpl implements WebApiService {

  static void create(final Vertx vertx) {
    final var service = new WebApiServiceImpl(EsiClient.create(vertx));
    new ServiceBinder(vertx).setAddress("bvg.character").register(WebApiService.class, service);
  }

  private final EsiClient client;

  private WebApiServiceImpl(final EsiClient client) {
    this.client = client;
  }

  @Override
  public Future<ServiceResponse> readCharacter(final ServiceRequest request) {
    return SingleHelper.toFuture(client.foo(227509970).map(ServiceResponse::completedWithJson));
  }
}

package com.bravecollective.bvg;

import com.bravecollective.bvg.rxjava3.character.WebApiService;
import io.reactivex.rxjava3.core.Completable;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.openapi.RouterBuilderOptions;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.handler.CorsHandler;
import io.vertx.rxjava3.ext.web.handler.StaticHandler;
import io.vertx.rxjava3.ext.web.openapi.RouterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.handler.codec.http.HttpHeaderNames.AUTHORIZATION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.vertx.core.http.HttpMethod.*;

public class MainVerticle extends AbstractVerticle {

  private static final Logger logger = LoggerFactory.getLogger(MainVerticle.class);
  // https://www.bravecollective.com/
  // https://developers.eveonline.com/applications
  // https://docs.esi.evetech.net/
  // https://esi.evetech.net/ui/#/

  @Override
  public Completable rxStart() {
    WebApiService.create(vertx);

    final var router = Router.router(vertx);
    return RouterBuilder.create(vertx, "openapi.yaml")
      .flatMapCompletable(
        routerBuilder -> {
          router.route("/api/*")
            .subRouter(routerBuilder
              .setOptions(new RouterBuilderOptions().setContractEndpoint(RouterBuilderOptions.STANDARD_CONTRACT_ENDPOINT))
              .mountServicesFromExtensions()
              .createRouter()
            );
          router.route(RouterBuilderOptions.STANDARD_CONTRACT_ENDPOINT + "/*").handler(context -> context.reroute("/api" + RouterBuilderOptions.STANDARD_CONTRACT_ENDPOINT));
          router.route("/openapi-ui/*").handler(StaticHandler.create("META-INF/resources/openapi-ui").setCachingEnabled(false));
          router.route().handler(CorsHandler.create(".*")
            .allowedMethod(GET)
            .allowedMethod(PATCH)
            .allowedMethod(PUT)
            .allowedMethod(POST)
            .allowedMethod(DELETE)
            .allowedMethod(HEAD)
            .allowedMethod(OPTIONS)
            .allowedHeader(CONTENT_TYPE.toString())
            .allowedHeader(AUTHORIZATION.toString())
            .allowCredentials(true)
            .allowPrivateNetwork(true)
            .maxAgeSeconds(60 * 60 * 24));

          return vertx.createHttpServer(new HttpServerOptions().setPort(8080).setHost("localhost"))
            .requestHandler(router)
            .listen()
            .doOnSuccess(server -> logger.info("clustered:{}, nativeTransport:{}, serving: {}", vertx.isClustered(), vertx.isNativeTransportEnabled(), "http://localhost.noenv.com:" + server.actualPort() + "/openapi-ui"))
            .ignoreElement();
        }
      );
  }
}

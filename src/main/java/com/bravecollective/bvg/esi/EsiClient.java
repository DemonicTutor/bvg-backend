package com.bravecollective.bvg.esi;

import io.reactivex.rxjava3.core.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.ext.web.client.HttpResponse;
import io.vertx.rxjava3.ext.web.client.WebClient;
import io.vertx.rxjava3.ext.web.codec.BodyCodec;
import io.vertx.rxjava3.uritemplate.UriTemplate;

public final class EsiClient {

  private static final String SERVER = "https://esi.evetech.net";

  /*
    authorize_url: "/v2/oauth/authorize",
    token_url: "/v2/oauth/token",
    site: "https://login.eveonline.com/"

    name: raw_info["name"],
    character_id: raw_info["character_id"],
    expires_on: raw_info["expires_on"],
    scopes: raw_info["scopes"],
    token_type: raw_info["token_type"],
    character_owner_hash: raw_info["owner"]

    @raw_info ||= JWT.decode(access_token.token, nil, false).find { |element| element.keys.include?("scp") }.tap do |hash|
          hash["character_id"] = hash["sub"].split(":")[-1]
          hash["scopes"] = [*hash["scp"]].join(" ")
          hash["token_type"] = hash["sub"].split(":")[0].capitalize
          hash["expires_on"] = hash["exp"]
    end
   */

  private final WebClient client;

  private EsiClient(final WebClient client) {
    this.client = client;
  }

  public static EsiClient create(final io.vertx.core.Vertx vertx) {
    return create(Vertx.newInstance(vertx));
  }

  public static EsiClient create(final Vertx vertx) {
    return new EsiClient(WebClient.create(vertx, new WebClientOptions()));
  }

  public Single<JsonObject> foo(final Integer characterId) {
    return Single.zip(
        character(characterId),
        characterPortrait(characterId),
        (character, characterPortrait) -> character.put("portrait", characterPortrait)
      )
      .flatMap(
        character -> Single.just((Integer) character.remove("corporation_id"))
          .flatMap(
            corporationId -> Single.zip(
              corporation(corporationId),
              corporationIcons(corporationId),
              (corporation, corporationIcons) -> corporation.put("icons", corporationIcons)
            )
          )
          .map(corporation -> character.put("corporation", corporation))
      );
  }

  private Single<JsonObject> character(final Integer characterId) {
    return client.getAbs(UriTemplate.of("{+server}/latest/characters{/id}"))
      .setTemplateParam("server", SERVER)
      .setTemplateParam("id", characterId.toString())
      .as(BodyCodec.jsonObject())
      .rxSend()
      .map(HttpResponse::body);
  }

  private Single<JsonObject> characterPortrait(final Integer characterId) {
    return client.getAbs(UriTemplate.of("{+server}/latest/characters{/id}/portrait"))
      .setTemplateParam("server", SERVER)
      .setTemplateParam("id", characterId.toString())
      .as(BodyCodec.jsonObject())
      .rxSend()
      .map(HttpResponse::body);
  }

  private Single<JsonObject> corporation(final Integer corporationId) {
    return client.getAbs(UriTemplate.of("{+server}/latest/corporations{/id}"))
      .setTemplateParam("server", SERVER)
      .setTemplateParam("id", corporationId.toString())
      .as(BodyCodec.jsonObject())
      .rxSend()
      .map(HttpResponse::body);
  }

  private Single<JsonObject> corporationIcons(final Integer corporationId) {
    return client.getAbs(UriTemplate.of("{+server}/latest/corporations{/id}/icons"))
      .setTemplateParam("server", SERVER)
      .setTemplateParam("id", corporationId.toString())
      .as(BodyCodec.jsonObject())
      .rxSend()
      .map(HttpResponse::body);
  }
}

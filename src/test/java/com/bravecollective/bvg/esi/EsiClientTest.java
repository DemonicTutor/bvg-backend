package com.bravecollective.bvg.esi;


import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.rxjava3.CompletableHelper;
import io.vertx.rxjava3.core.Vertx;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
class EsiClientTest {

  @Test
  @Disabled
  @DisplayName("serve character")
  void shouldCharacter(final VertxTestContext context, final Vertx vertx) {
    EsiClient.create(vertx)
      .foo(227509970)
      .doOnSuccess(json -> context.verify(() -> Assertions.assertEquals(
        new JsonObject(
          """
          {
            "alliance_id" : 99010079,
            "birthday" : "2010-05-26T20:48:00Z",
            "bloodline_id" : 13,
            "description" : "<font size=\\"13\\" color=\\"#99ffffff\\"></font><font size=\\"12\\" color=\\"#ffff0000\\"><b>For Pony!</b></font>",
            "gender" : "female",
            "name" : "lt cwalu",
            "race_id" : 4,
            "security_status" : 5.002404576,
            "portrait" : {
              "px128x128" : "https://images.evetech.net/characters/227509970/portrait?tenant=tranquility&size=128",
              "px256x256" : "https://images.evetech.net/characters/227509970/portrait?tenant=tranquility&size=256",
              "px512x512" : "https://images.evetech.net/characters/227509970/portrait?tenant=tranquility&size=512",
              "px64x64" : "https://images.evetech.net/characters/227509970/portrait?tenant=tranquility&size=64"
            },
            "corporation" : {
              "alliance_id" : 99010079,
              "ceo_id" : 2116142894,
              "creator_id" : 2112550682,
              "date_founded" : "2019-09-07T17:09:22Z",
              "description" : "<font size=\\"13\\" color=\\"#ff999999\\"></font><font size=\\"12\\" color=\\"#bfffffff\\">The Brave Collective's Highsecurity corporation.<br><br>Brave Empire is a non war-deccable corporation operating in High Sec, Low Sec &amp; Null space. Brave Empire is a full member of the nullsec based Brave Collective alliance with access to all of their space, services and resources - but with the comfort of a war-free highsec home so you can make the most of all space.  <br><br>We run fleets for all kinds of activities, from mining to gatecamps, lowsec roams and everything in between and have a diverse community of capsuleers with experience in all of it.<br><br>If you're interested in joining please stop by the </font><font size=\\"12\\" color=\\"#ff6868e1\\"><a href=\\"joinChannel:player_4620c1a1d35a11e9ac739abe94f5a43f//None//None\\">Brave Empire Public</a></font><font size=\\"12\\" color=\\"#bfffffff\\"> channel in-game and see </font><font size=\\"12\\" color=\\"#ffffe400\\"><a href=\\"https://wiki.bravecollective.com/public/corps/brave-empire/how-to-apply\\">How to Apply to Brave Empire</a></font><font size=\\"12\\" color=\\"#bfffffff\\"> for instructions on how to apply. </font>",
              "home_station_id" : 60014104,
              "member_count" : 1859,
              "name" : "Brave Empire",
              "shares" : 1000,
              "tax_rate" : 0.10000000149011612,
              "ticker" : "I-EVE",
              "url" : "https://wiki.bravecollective.com/public/corps/brave-empire/start",
              "icons" : {
                "px128x128" : "https://images.evetech.net/corporations/98613799/logo?tenant=tranquility&size=128",
                "px256x256" : "https://images.evetech.net/corporations/98613799/logo?tenant=tranquility&size=256",
                "px64x64" : "https://images.evetech.net/corporations/98613799/logo?tenant=tranquility&size=64"
              }
            }
          }
          """),
        json
      )))
      .ignoreElement()
      .subscribe(CompletableHelper.toObserver(context.succeedingThenComplete()));
  }
}

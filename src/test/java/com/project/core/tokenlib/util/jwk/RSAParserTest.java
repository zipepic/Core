package com.project.core.tokenlib.util.jwk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.junit.Test;
import tokenlib.util.jwk.RSAParser;
import tokenlib.util.jwk.SimpleJWK;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RSAParserTest {
  @Test
  public void testParseRSAKeyFromJson() throws JsonProcessingException {
    RSAKey p = RSAParser.parseRSAKeyFromJson("{\"kty\":\"RSA\",\"kid\":\"test\",\"n\":\"test\",\"e\":\"AQAB\"}");
    assertEquals("test", p.getKeyID());
    assertEquals("test", p.getModulus().toString());
    assertEquals("AQAB", p.getPublicExponent().toString());
    assertEquals("RSA", p.getKeyType().getValue());
  }
  @Test()
  public void testParseRSAKeyFromSimpleJWK() throws JsonProcessingException, ParseException {
    var simpleJWK = new SimpleJWK();
    simpleJWK.setKty("RSA");
    simpleJWK.setKid("kid");
    simpleJWK.setN("N");
    simpleJWK.setE("E");
    RSAKey p = RSAParser.parseRSAKeyFromSimpleJWK(simpleJWK);
    assertEquals("kid", p.getKeyID());
    assertEquals("N", p.getModulus().toString());
    assertEquals("E", p.getPublicExponent().toString());
    assertEquals("RSA", p.getKeyType().getValue());
  }
  @Test(expected = JsonProcessingException.class)
  public void testParseRSAKeyFromJsonException() throws JsonProcessingException {
    RSAKey p = RSAParser.parseRSAKeyFromJson("{\"kty\":\"RSA\",\"kid\":\"test\",\"n\":\"test\",\"e\"");
  }
  @Test(expected = JsonProcessingException.class)
  public void testParseRSAKeyFromSimpleJwkException() throws JsonProcessingException {
    var simpleJWK = new SimpleJWK();
    simpleJWK.setKty("RSA");
    simpleJWK.setKid("kid");
    simpleJWK.setN("N");
    RSAKey p = RSAParser.parseRSAKeyFromSimpleJWK(simpleJWK);
  }
}

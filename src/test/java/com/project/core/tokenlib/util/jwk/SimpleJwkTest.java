package com.project.core.tokenlib.util.jwk;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import tokenlib.util.jwk.SimpleJWK;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleJwkTest {
  @Test
  public void testParse() throws JsonProcessingException {
    var p = SimpleJWK.parse("{\"kty\":\"RSA\",\"kid\":\"test\",\"n\":\"test\",\"e\":\"AQAB\"}");
    assertEquals("test", p.getKid());
    assertEquals("test", p.getN());
    assertEquals("AQAB", p.getE());
    assertEquals("RSA", p.getKty());
  }
  @Test(expected = JsonProcessingException.class)
  public void testParseException() throws JsonProcessingException {
    var p = SimpleJWK.parse("{\"kty\",\"kid\":\"test\",\"n\":\"test\",\"e\"");
  }
  @Test
  public void testToJSONString() throws JsonProcessingException {
    var p = new SimpleJWK();
    p.setKty("RSA");
    p.setKid("test");
    p.setN("test");
    p.setE("AQAB");
    assertEquals("{\"kty\":\"RSA\",\"kid\":\"test\",\"n\":\"test\",\"e\":\"AQAB\"}", p.toJSONString());
  }
  @Test(expected = JsonProcessingException.class)
  public void testToJSONStringException() throws JsonProcessingException {
    var p = new SimpleJWK();
    p.setKty("RSA");
    p.setKid("test");
    p.setN("test");
    assertEquals("{\"kty\":\"RSA\",\"kid\":\"test\",\"n\":\"test\",\"e\":\"AQAB\"}", p.toJSONString());
  }
}

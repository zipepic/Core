package tokenlib.util.jwk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.util.Base64URL;

public final class RSAParser {
  public static RSAKey parseRSAKeyFromJson(String json) throws JsonProcessingException {

      ObjectMapper objectMapper = new ObjectMapper();
      var jwk = objectMapper.readValue(json, SimpleJWK.class);

      RSAKey rsaKey = new RSAKey.Builder(
        new Base64URL(jwk.getN()),
        new Base64URL(jwk.getE()))
        .keyID(jwk.getKid())
        .build();
      return rsaKey;

  }
  public static RSAKey parseRSAKeyFromSimpleJWK(SimpleJWK jwk) throws JsonProcessingException {
    if(jwk.getKty() == null || jwk.getKid() == null || jwk.getN() == null || jwk.getE() == null){
      throw new JsonProcessingException("Invalid SimpleJwk"){};
    }
    RSAKey rsaKey = new RSAKey.Builder(
      new Base64URL(jwk.getN()),
      new Base64URL(jwk.getE()))
      .keyID(jwk.getKid())
      .build();
    return rsaKey;

  }
}




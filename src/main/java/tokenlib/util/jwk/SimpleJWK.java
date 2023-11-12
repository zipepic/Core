package tokenlib.util.jwk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class SimpleJWK {
  @JsonProperty("kty")
  private String kty;

  @JsonProperty("kid")
  private String kid;

  @JsonProperty("e")
  private String e;

  @JsonProperty("n")
  private String n;

  public static SimpleJWK parse(String jwk) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.readValue(jwk, SimpleJWK.class);
    } catch (JsonProcessingException ex) {
      throw new RuntimeException(ex);
    }
  }
  public String toJSONString() {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.writeValueAsString(this);
    } catch (JsonProcessingException ex) {
      throw new RuntimeException("Error converting SimpleJwk to JSON string", ex);
    }
  }
}


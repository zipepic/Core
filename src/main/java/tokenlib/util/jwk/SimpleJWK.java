package tokenlib.util.jwk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class SimpleJWK {
  @JsonProperty(value = "kty",index = 1)
  private String kty;

  @JsonProperty(value = "kid",index = 2)
  private String kid;

  @JsonProperty(value = "e",index = 4)
  private String e;

  @JsonProperty(value ="n", index = 3)
  private String n;

  public static SimpleJWK parse(String jwk) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(jwk, SimpleJWK.class);
  }
  public String toJSONString() throws JsonProcessingException {
    if(kty == null || kid == null || n == null || e == null){
      throw new JsonProcessingException("Invalid SimpleJwk"){};
    }
    ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.writeValueAsString(this);
  }
}


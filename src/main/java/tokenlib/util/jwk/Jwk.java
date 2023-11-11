package tokenlib.util.jwk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class Jwk {
  private String kty;
  private String kid;
  private String e;
  private String n;
  public static Jwk parse(String jwk)  {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.readValue(jwk, Jwk.class);
    } catch (JsonProcessingException ex) {
      throw new RuntimeException(ex);
    }
  }
}

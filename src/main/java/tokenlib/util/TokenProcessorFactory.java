package tokenlib.util;

import com.nimbusds.jose.jwk.JWK;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.GenericQueryMessage;
import org.axonframework.queryhandling.QueryGateway;
import tokenlib.util.tokenenum.TokenTypes;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.List;

public class TokenProcessorFactory {
  private final SecretKeySpec secret;
  private final QueryGateway queryGateway;

  public TokenProcessorFactory(String secret, QueryGateway queryGateway) {
    this.queryGateway = queryGateway;
    byte[] secretKeyBytes = Base64.getDecoder().decode(secret);
    this.secret = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());
  }
  private JwkSetLoader loadJwkSetFromSource() {
    //TODO optimize this
    return new JwkSetLoader() {
      @Override
      public List<String> loadJwkSetFromSource(QueryGateway queryGateway)  {
        GenericQueryMessage<String,List<JWK>> query = new GenericQueryMessage<>("fetchJwkSet","fetchJwkSet", ResponseTypes.multipleInstancesOf(JWK.class));
        return queryGateway.query(query,ResponseTypes.multipleInstancesOf(String.class)).join();
      }
    };
  }
  public TokenProcessorContext generate(TokenTypes type){
    switch (type) {
      case JWT:
        return new TokenProcessorContext(new JwtManager(secret));
      case JWK:
        return new TokenProcessorContext(new JwkManager(loadJwkSetFromSource(),queryGateway));
      default:
        throw new IllegalArgumentException("Unknown token type");
    }
  }
  public TokenProcessorContext generate(int type){
    switch (type) {
      case 0:
        return new TokenProcessorContext(new JwtManager(secret));
      case 1:
        return new TokenProcessorContext(new JwkManager(loadJwkSetFromSource(),queryGateway));
      default:
        throw new IllegalArgumentException("Unknown token type");
    }
  }
}




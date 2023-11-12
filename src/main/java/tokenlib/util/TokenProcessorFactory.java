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
  private JwkSetLoader jwkSetLoader;

  public TokenProcessorFactory(String secret, QueryGateway queryGateway) {
    this.queryGateway = queryGateway;
    byte[] secretKeyBytes = Base64.getDecoder().decode(secret);
    this.secret = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());
  }
  public TokenProcessorFactory(QueryGateway queryGateway,JwkSetLoader jwkSetLoader){
    this.queryGateway = queryGateway;
    this.jwkSetLoader = jwkSetLoader;
    this.secret = null;
  }
  public TokenProcessorContext generate(TokenTypes type){
    switch (type) {
      case JWT:
        return new TokenProcessorContext(new JwtManager(secret));
      case JWK:
        return new TokenProcessorContext(new JwkManager(jwkSetLoader,queryGateway));
      default:
        throw new IllegalArgumentException("Unknown token type");
    }
  }
  public TokenProcessorContext generate(int type){
    switch (type) {
      case 0:
        return new TokenProcessorContext(new JwtManager(secret));
      case 1:
        return new TokenProcessorContext(new JwkManager(jwkSetLoader,queryGateway));
      default:
        throw new IllegalArgumentException("Unknown token type");
    }
  }
}




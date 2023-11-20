package tokenlib.util;

import org.axonframework.queryhandling.QueryGateway;
import tokenlib.util.lamdas.EventClassProvider;
import tokenlib.util.lamdas.JwkSetLoader;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public final class TokenProcessorFactory extends AbstractTokenFactory {
  private final SecretKeySpec secret;
  private final QueryGateway queryGateway;
  private JwkSetLoader jwkSetLoader;
  private Class<?> eventClassForJwt;
  private Class<?> eventClassForJwk;

  public void setEventClassForJwt(Class<?> eventClassForJwt) {
    this.eventClassForJwt = eventClassForJwt;
  }

  public void setEventClassForJwk(Class<?> eventClassForJwk) {
    this.eventClassForJwk = eventClassForJwk;
  }

  public TokenProcessorFactory(String secret, QueryGateway queryGateway) {
    this.queryGateway = queryGateway;
    byte[] secretKeyBytes = Base64.getDecoder().decode(secret);
    this.secret = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());
  }
  public TokenProcessorFactory(String secret, QueryGateway queryGateway, JwkSetLoader jwkSetLoader){
    this.queryGateway = queryGateway;
    this.jwkSetLoader = jwkSetLoader;
    byte[] secretKeyBytes = Base64.getDecoder().decode(secret);
    this.secret = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());
  }
  @Override
  protected TokenProcessorContext generateJwkManager() {
    return new TokenProcessorContext(new JwkManager(jwkSetLoader,()->{return eventClassForJwk;}, queryGateway));
  }

  @Override
  protected TokenProcessorContext generateJwtManager() {
    return new TokenProcessorContext(new JwtManager(secret,()->{return eventClassForJwt;}));
  }
}




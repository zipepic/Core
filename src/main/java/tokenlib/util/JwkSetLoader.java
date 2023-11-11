package tokenlib.util;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import org.axonframework.queryhandling.QueryGateway;
import tokenlib.util.jwk.Jwk;

import java.util.List;

@FunctionalInterface
public interface JwkSetLoader {
  List<String> loadJwkSetFromSource(QueryGateway queryGateway);
}

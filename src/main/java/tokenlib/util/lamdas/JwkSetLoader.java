package tokenlib.util.lamdas;

import org.axonframework.queryhandling.QueryGateway;
import tokenlib.util.jwk.SimpleJWK;

import java.util.List;

@FunctionalInterface
public interface JwkSetLoader {
  List<SimpleJWK> loadJwkSetFromSource(QueryGateway queryGateway);
}

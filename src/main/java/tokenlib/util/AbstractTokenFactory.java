package tokenlib.util;

import tokenlib.util.tokenenum.TokenTypes;

public abstract class AbstractTokenFactory {
  public TokenProcessorContext generate(Integer type){
    switch (type) {
      case 0:
        return generateJwtManager();
      case 1:
        return generateJwkManager();
      default:
        throw new IllegalArgumentException("Unknown token type");
    }
  }
  public TokenProcessorContext generate(TokenTypes type){
    switch (type) {
      case JWT:
        return generateJwtManager();
      case JWK:
        return generateJwkManager();
      default:
        throw new IllegalArgumentException("Unknown token type");
    }
  }
  protected abstract TokenProcessorContext generateJwkManager();
  protected abstract TokenProcessorContext generateJwtManager();
}

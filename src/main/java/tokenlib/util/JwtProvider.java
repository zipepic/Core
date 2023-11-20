package tokenlib.util;

import io.jsonwebtoken.JwtBuilder;

sealed interface JwtProvider extends TokenProcessingStrategy permits JwtManager{
  String generateSignedCompactToken(JwtBuilder jwt, String tokenId);
}

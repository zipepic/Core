package tokenlib.util.jwk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@Getter
@Setter
public class SimpleJWKSet {
    private List<SimpleJWK> keys;

}

package kr.api.lenders.service.value;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AuthResponse {
    String token;
}

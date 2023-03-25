package kr.api.lenders.service.value;

import lombok.Value;

@Value
public class OkResponse {
    boolean result = true;

    String message = "OK";
}

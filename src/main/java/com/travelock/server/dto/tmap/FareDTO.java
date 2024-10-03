package com.travelock.server.dto.tmap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FareDTO {
    @JsonProperty("regular.totalFare")
    private int totalFare; // 대중교통 요금
    @JsonProperty("regular.currency.symbol")
    private String symbol; // 금액 상징(₩)
    @JsonProperty("regular.currency.currency")
    private String currency; // 금액 단위(원)
    @JsonProperty("regular.currency.currencyCode")
    private String currencyCode; // 금액 단위 코드(KRW)
}

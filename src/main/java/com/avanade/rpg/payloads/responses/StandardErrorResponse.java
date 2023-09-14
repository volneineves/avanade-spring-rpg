package com.avanade.rpg.payloads.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StandardErrorResponse(@JsonProperty("codigo_de_status")
                                    Integer status,

                                    @JsonProperty("mensagem")
                                    String message,

                                    @JsonProperty("data_e_hora")
                                    String timestamp) {
}

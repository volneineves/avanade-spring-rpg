package com.avanade.rpg.payloads.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record StandardErrorResponse(@JsonProperty("codigo_de_status")
                                    @Schema(description = "The HTTP status code", example = "400")
                                    Integer status,

                                    @JsonProperty("mensagem")
                                    @Schema(description = "A message explaining the error", example = "Bad Request: Invalid parameter")
                                    String message,

                                    @JsonProperty("data_e_hora")
                                    @Schema(description = "Timestamp when the error occurred", example = "2023-09-14T18:34:29Z")
                                    String timestamp) {
}

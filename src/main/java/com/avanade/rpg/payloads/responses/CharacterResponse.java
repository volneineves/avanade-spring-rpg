package com.avanade.rpg.payloads.responses;

import com.avanade.rpg.enums.CharacterType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record CharacterResponse(@JsonProperty("id") UUID id,
                                @JsonProperty("nome") String name,
                                @JsonProperty("vida") Short health,
                                @JsonProperty("forca") Short strength,
                                @JsonProperty("defesa") Short defense,
                                @JsonProperty("agilidade") Short agility,
                                @JsonProperty("quantidade_de_dados") Short numDice,
                                @JsonProperty("faces_do_dado") Short faces,
                                @JsonProperty("tipo") CharacterType type
) {
}

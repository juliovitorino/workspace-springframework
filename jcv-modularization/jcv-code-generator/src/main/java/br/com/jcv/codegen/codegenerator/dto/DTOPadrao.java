package br.com.jcv.codegen.codegenerator.dto;

import br.com.jcv.codegen.codegenerator.constantes.UsuarioConstantes;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import java.util.Date;

import java.io.Serializable;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class DTOPadrao implements Serializable {

    @SerializedName("id")
    @JsonProperty("id")
    private Long id;

    @SerializedName("status")
    @JsonProperty("status")
    private String status;

    @SerializedName("dateCreated")
    @JsonProperty("dateCreated")
    private Date dateCreated;

    @SerializedName("dateUpdated")
    @JsonProperty("dateUpdated")
    private Date dateUpdated;
}

package br.com.jcv.codegen.codegenerator.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.*;
import br.com.jcv.codegen.codegenerator.constantes.UsuarioConstantes;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

/**
* UsuarioDTO - Data Transfer Object
*
* @author Usuario
* @since Mon Oct 02 19:21:57 BRT 2023
* @copyright(c), Julio Vitorino <julio.vitorino@gmail.com>
*/

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsuarioDTO extends DTOPadrao implements Serializable
{

    @SerializedName(UsuarioConstantes.ID)
    @JsonProperty(UsuarioConstantes.ID)
    private Long id;

    @SerializedName(UsuarioConstantes.NOME)
    @JsonProperty(UsuarioConstantes.NOME)
    private String nome;

    @SerializedName(UsuarioConstantes.IDADE)
    @JsonProperty(UsuarioConstantes.IDADE)
    private Long idade;

    @SerializedName(UsuarioConstantes.STATUS)
    @JsonProperty(UsuarioConstantes.STATUS)
    private String status;

    @SerializedName(UsuarioConstantes.DATECREATED)
    @JsonProperty(UsuarioConstantes.DATECREATED)
    private Date dateCreated;

    @SerializedName(UsuarioConstantes.DATEUPDATED)
    @JsonProperty(UsuarioConstantes.DATEUPDATED)
    private Date dateUpdated;


    @SerializedName("mensagemResponse")
    @JsonProperty("mensagemResponse")
    private MensagemResponse mensagemResponse;
}

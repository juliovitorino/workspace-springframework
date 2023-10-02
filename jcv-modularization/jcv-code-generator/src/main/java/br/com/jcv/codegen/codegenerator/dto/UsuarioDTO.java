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
* @since Mon Oct 02 16:54:35 BRT 2023
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

    @SerializedName(UsuarioConstantes.NOME)
    @JsonProperty(UsuarioConstantes.NOME)
    private String nome;

    @SerializedName(UsuarioConstantes.IDADE)
    @JsonProperty(UsuarioConstantes.IDADE)
    private Long idade;

    @SerializedName("mensagemResponse")
    @JsonProperty("mensagemResponse")
    private MensagemResponse mensagemResponse;
}

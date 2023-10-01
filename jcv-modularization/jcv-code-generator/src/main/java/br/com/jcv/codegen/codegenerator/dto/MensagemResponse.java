package br.com.jcv.codegen.codegenerator.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MensagemResponse implements Serializable {
  @NotNull
  @NotEmpty
  @SerializedName(MensagemConstantes.MSGCODE)
  @JsonProperty(MensagemConstantes.MSGCODE)
  private String msgcode;

  @NotNull
  @NotEmpty
  @SerializedName(MensagemConstantes.MENSAGEM)
  @JsonProperty(MensagemConstantes.MENSAGEM)
  private String mensagem;

}

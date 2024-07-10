package br.com.projeto.projeto_aws.entity;

import br.com.projeto.projeto_aws.enums.EventType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Envelope {

    private EventType eventType;
    private String data;
}

package br.com.projeto.projeto_aws.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductEvent {

    private Long productId;
    private String code;
    private String username;
}

package com.nathanielpautzke.bullseyeguy.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {

    private String id;
    private String name;
    private Price price;
}

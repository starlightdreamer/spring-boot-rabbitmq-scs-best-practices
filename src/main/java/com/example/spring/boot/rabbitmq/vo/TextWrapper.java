package com.example.spring.boot.rabbitmq.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Using @Builder (ex: TextWrapper.builder().text("stuff").build() ) is especially advised for classes that are likely to
 * have fields added/removed in the future. If using 'new', every instantiation must be modified if the
 * number of constructor parameters changes.
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class TextWrapper {

    private String text;

}

package com.reactive.Reactive.domain;


import com.reactive.Reactive.web.model.BeerStyleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created by jt on 2019-05-25.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Beer {

    @Id
    private Integer id;

    private Long version;

    private String beerName;
    private BeerStyleEnum beerStyle;
    private String upc;

    private Integer quantityOnHand;
    private BigDecimal price;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;
}

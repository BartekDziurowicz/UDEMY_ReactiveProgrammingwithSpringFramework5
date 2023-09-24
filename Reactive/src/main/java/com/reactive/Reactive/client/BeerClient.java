package com.reactive.Reactive.client;

import com.reactive.Reactive.model.BeerDto;
import com.reactive.Reactive.model.BeerPagedList;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Created by jt on 3/13/21.
 */
public interface BeerClient {

    Mono<BeerDto> getBeerById(UUID id, Boolean showInventoryOnHand);

    Mono<BeerPagedList> listBeers(Integer pageNumber, Integer pageSize, String beerName,
                                  String beerStyle, Boolean showInventoryOnhand);

    Mono<ResponseEntity> createBeer(BeerDto beerDto);

    Mono<ResponseEntity> updateBeer(BeerDto beerDto);

    Mono<ResponseEntity> deleteBeerById(UUID id);

    Mono<BeerDto> getBeerByUPC(String upc);
}

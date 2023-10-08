package com.reactive.Reactive.repositories;

import com.reactive.Reactive.domain.Beer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;


public interface BeerRepository extends ReactiveCrudRepository<Beer, Integer> {

    Mono<Beer> findByUpc(String upc);


//    Page<Beer> findAllByBeerName(String beerName, Pageable pageable);
//
//    Page<Beer> findAllByBeerStyle(BeerStyleEnum beerStyle, Pageable pageable);
//
//    Page<Beer> findAllByBeerNameAndBeerStyle(String beerName, BeerStyleEnum beerStyle, Pageable pageable);


}

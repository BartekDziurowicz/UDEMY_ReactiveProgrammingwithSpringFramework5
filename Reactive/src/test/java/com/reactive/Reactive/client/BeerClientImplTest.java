package com.reactive.Reactive.client;

import com.reactive.Reactive.config.WebClientConfig;
import com.reactive.Reactive.model.BeerPagedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BeerClientImplTest {

    BeerClientImpl beerClient;

    @BeforeEach
    void setUp() {
        beerClient = new BeerClientImpl(new WebClientConfig().webClient());
    }

    @Test
    @Disabled
    void listBeers() {
        //given
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null, null, null);
        //when
        BeerPagedList pagedList = beerPagedListMono.block();
        //then
        assertThat(pagedList).isNotNull();
        assertThat(pagedList.getContent().size()).isGreaterThan(0);
    }

    @Test
    void getBeerById() {
        //given

        //when

        //then

    }

    @Test
    void createBeer() {
        //given

        //when

        //then

    }

    @Test
    void updateBeer() {
        //given

        //when

        //then

    }

    @Test
    void deleteBeerById() {
        //given

        //when

        //then

    }

    @Test
    void getBeerByUPC() {
        //given

        //when

        //then

    }
}
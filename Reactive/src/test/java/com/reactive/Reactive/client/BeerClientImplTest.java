package com.reactive.Reactive.client;

import com.reactive.Reactive.config.WebClientConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BeerClientImplTest {

    BeerClientImpl beerClient;

    @BeforeEach
    void setUp() {
        beerClient = new BeerClientImpl(new WebClientConfig().webClient());
    }

    @Test
    void getBeerById() {
        //given

        //when

        //then

    }

    @Test
    void listBeers() {
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
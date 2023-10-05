package com.reactive.Reactive.web.controller;

import com.reactive.Reactive.bootstrap.BeerLoader;
import com.reactive.Reactive.services.BeerService;
import com.reactive.Reactive.web.model.BeerDto;
import com.reactive.Reactive.web.model.BeerPagedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebFluxTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    BeerService beerService;

    BeerDto validBeer;

    @BeforeEach
    void setUp() {
        validBeer = BeerDto.builder()
                .beerName("Test beer")
                .beerStyle("PALE_ALE")
                .upc(BeerLoader.BEER_1_UPC)
                .build();
    }

    @Test
    void getBeerById() {
        //given
        //when
        UUID beerId = UUID.randomUUID();
        given(beerService.getById(any(), any())).willReturn(validBeer);
        //then
        webTestClient.get()
                .uri("/api/v1/beer/" + beerId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BeerDto.class)
                .value(beerDto -> beerDto.getBeerName(), equalTo(validBeer.getBeerName()));
    }

    @Test
    void getBeerByUpc() {
        //given
        //when
        String upc = BeerLoader.BEER_1_UPC;
        given(beerService.getByUpc(any())).willReturn(validBeer);
        //then
        webTestClient.get()
                .uri("/api/v1/beerUpc/" + upc)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BeerDto.class)
                .value(beerDto -> beerDto.getUpc(), equalTo(validBeer.getUpc()));
    }

    @Test
    void getListBeers() {
        //given
        List<BeerDto> beerDtoList = Arrays.asList(validBeer);
        BeerPagedList beerPagedList = new BeerPagedList(beerDtoList, PageRequest.of(1,1), beerDtoList.size());
        //when
        given(beerService.listBeers(any(), any(), any(), any())).willReturn(beerPagedList);
        //then
        webTestClient.get()
                .uri("/api/v1/beer")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BeerPagedList.class);
    }
}
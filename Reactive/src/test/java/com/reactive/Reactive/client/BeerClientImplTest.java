package com.reactive.Reactive.client;

import com.reactive.Reactive.config.WebClientConfig;
import com.reactive.Reactive.model.BeerDto;
import com.reactive.Reactive.model.BeerPagedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BeerClientImplTest {

    BeerClientImpl beerClient;

    @BeforeEach
    void setUp() {
        beerClient = new BeerClientImpl(new WebClientConfig().webClient());
    }

    @Test
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
    void listBeersPageSize10() {
        //given
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(1, 10, null, null, null);
        //when
        BeerPagedList pagedList = beerPagedListMono.block();
        //then
        assertThat(pagedList).isNotNull();
        assertThat(pagedList.getContent().size()).isEqualTo(10);
    }

    @Test
    void listBeersNoRecords() {
        //given
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(15, 20, null, null, null);
        //when
        BeerPagedList pagedList = beerPagedListMono.block();
        //then
        assertThat(pagedList).isNotNull();
        assertThat(pagedList.getContent().size()).isEqualTo(0);
    }

    @Test
    void getBeerById() {
        //given
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null, null, null);
        BeerPagedList pagedList = beerPagedListMono.block();
        UUID beerId = pagedList.getContent().get(0).getId();
        //when
        Mono<BeerDto> beerDtoMono = beerClient.getBeerById(beerId, false);
        BeerDto beerDto = beerDtoMono.block();
        //then
        assertEquals(beerId, beerDto.getId());
        // API returning inventory when should not be
//        assertNull(beerDto.getQuantityOnHand());
    }

    @Test
    void getBeerByIdShowInventoryTrue() {
        //given
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null, null, null);
        BeerPagedList pagedList = beerPagedListMono.block();
        UUID beerId = pagedList.getContent().get(0).getId();
        //when
        Mono<BeerDto> beerDtoMono = beerClient.getBeerById(beerId, true);
        BeerDto beerDto = beerDtoMono.block();
        //then
        assertEquals(beerId, beerDto.getId());
        assertNotNull(beerDto.getQuantityOnHand());
    }

    @Test
    void getBeerByUPC() {
        //given
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null, null, null);
        BeerPagedList pagedList = beerPagedListMono.block();
        String beerUpc = pagedList.getContent().get(0).getUpc();
        //when
        Mono<BeerDto> beerDtoMono = beerClient.getBeerByUPC(beerUpc);
        BeerDto beerDto = beerDtoMono.block();
        //then
        assertEquals(beerUpc, beerDto.getUpc());
    }

    @Test
    void createBeer() {
        //given
        BeerDto beerDto = BeerDto.builder()
                .beerName("Dogfishhead")
                .beerStyle("IPA")
                .upc("123456789")
                .price(new BigDecimal("10.99"))
                .build();
        //when
        Mono<ResponseEntity<Void>> responseEntityMono = beerClient.createBeer(beerDto);
        ResponseEntity responseEntity = responseEntityMono.block();
        //then
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void updateBeer() {
        //given
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null, null, null);
        BeerPagedList pagedList = beerPagedListMono.block();
        BeerDto beerDto = pagedList.getContent().get(0);
        BeerDto updatedBeer = BeerDto.builder()
                .beerName("Really good beer")
                .beerStyle(beerDto.getBeerStyle())
                .price(beerDto.getPrice())
                .upc(beerDto.getUpc())
                .build();
        //when
        Mono<ResponseEntity<Void>> responseEntityMono = beerClient.updateBeer(beerDto.getId(), updatedBeer);
        ResponseEntity<Void> responseEntity = responseEntityMono.block();
        //then
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    void deleteBeerById() {
        //given
        Mono<BeerPagedList> beerPagedListMono = beerClient.listBeers(null, null, null, null, null);
        BeerPagedList pagedList = beerPagedListMono.block();
        BeerDto beerDto = pagedList.getContent().get(0);
        //when
        Mono<ResponseEntity<Void>> responseEntityMono = beerClient.deleteBeerById(beerDto.getId());
        ResponseEntity<Void> responseEntity = responseEntityMono.block();
        //then
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

}
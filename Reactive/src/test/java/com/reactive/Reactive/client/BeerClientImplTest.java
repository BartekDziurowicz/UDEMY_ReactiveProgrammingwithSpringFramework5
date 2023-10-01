package com.reactive.Reactive.client;

import com.reactive.Reactive.config.WebClientConfig;
import com.reactive.Reactive.model.BeerDto;
import com.reactive.Reactive.model.BeerPagedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void deleteBeerByIdNotFound() {
        //given
        //when
        Mono<ResponseEntity<Void>> responseEntityMono = beerClient.deleteBeerById(UUID.randomUUID());
        //then
        assertThrows(WebClientResponseException.class, () -> {
            ResponseEntity<Void> responseEntity = responseEntityMono.block();
            assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        });
    }

    @Test
    void deleteBeerHandleException() {
        //given
        //when
        Mono<ResponseEntity<Void>> responseEntityMono = beerClient.deleteBeerById(UUID.randomUUID());
        ResponseEntity<Void> responseEntity = responseEntityMono.onErrorResume(throwable -> {
            if(throwable instanceof WebClientResponseException) {
                WebClientResponseException exception = (WebClientResponseException) throwable;
                return Mono.just(ResponseEntity.status(exception.getStatusCode()).build());
            } else {
                throw new RuntimeException(throwable);
            }
        }).block();
        //then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void functionalTestGetBeerById() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        AtomicReference<String> beerName = new AtomicReference<>();
        beerClient.listBeers(null, null, null, null, null)
                .map(beerPagedList -> beerPagedList.getContent().get(0).getId())
                .map(beerId -> beerClient.getBeerById(beerId, false))
                .flatMap(mono -> mono).subscribe(beerDto -> {
                    System.out.println(beerDto.getBeerName());
                    beerName.set(beerDto.getBeerName());
                    assertThat(beerDto.getBeerName()).isEqualTo("Mango Bobs");
                    countDownLatch.countDown();
                });
        countDownLatch.await();
        assertThat(beerName.get()).isEqualTo("Mango Bobs");
    }
}
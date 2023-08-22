package com.reactive.Reactive.services;

import com.reactive.Reactive.domain.Movie;
import com.reactive.Reactive.domain.MovieEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovieService {

    Mono<Movie> getMovieById(String id);

    Flux<Movie> getAllMovies();

    Flux<MovieEvent> streamMovieEvents(String id);

}

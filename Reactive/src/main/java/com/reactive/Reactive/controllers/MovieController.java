package com.reactive.Reactive.controllers;

import com.reactive.Reactive.domain.Movie;
import com.reactive.Reactive.domain.MovieEvent;
import com.reactive.Reactive.services.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/{id}")
    Mono<Movie> getMovieById(@PathVariable String id) {
        return movieService.getMovieById(id);
    }

    @GetMapping
    @ResponseBody
    Flux<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping(value = "/{id}/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    Flux<MovieEvent> streamMovieEvents(@PathVariable String id) {
        return movieService.streamMovieEvents(id);
    };

}

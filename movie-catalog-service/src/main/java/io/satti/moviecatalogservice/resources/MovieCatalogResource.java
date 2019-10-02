package io.satti.moviecatalogservice.resources;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
// import org.springframework.web.reactive.function.client.WebClient;

import io.satti.moviecatalogservice.models.CatalogItem;
import io.satti.moviecatalogservice.models.Movie;
import io.satti.moviecatalogservice.models.Rating;
import io.satti.moviecatalogservice.models.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource{

    @Autowired
    private RestTemplate restTemplate;

   /*  @Autowired
    private WebClient.Builder webClientBuiler; */

    /**
     * Get all reated movies ids
     * for each movie id, call movie info serivce to get all info
     * @param userId
     * @return
     */
    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){

       /*  List<Rating> ratings = Arrays.asList(
            new Rating("1234", 5),
            new Rating("456", 4) */
            UserRating ratings = restTemplate.getForObject("http://rating-data-service/ratingsdata/users/" + userId, UserRating.class);

        return ratings.getUserRating().stream().map(rating -> {
            Movie movie = restTemplate.getForObject("http://movie-info-service/movies/"+rating.getMovieId(), Movie.class);



            return new CatalogItem(movie.getName(), "Desc", rating.getRating());
        })
        .collect(Collectors.toList());
    }

}


           /*  Movie movie = webClientBuiler.build()
                        .get()
                        .uri("http://localhost:8081/movies/"+rating.getMovieId())
                        .retrieve()
                        .bodyToMono(Movie.class)
                        .block(); */
package com.example.demo.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.models.CatalogItem;
import com.example.demo.models.Movie;
import com.example.demo.models.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
	@Autowired
	private RestTemplate restTemplate;


//	@Autowired
//	private WebClient.Builder builder;

	@RequestMapping("/{userId}")
	public List<CatalogItem>  getCatalog(@PathVariable String userId) { 
		 UserRating userRating = restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/" + userId, UserRating.class);

	        return userRating.getUserRating().stream()
	                .map(rating -> {
	                    Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
	                    return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
	                }) 
	                .collect(Collectors.toList());
	}
	
	@RequestMapping("/movietest")
	public String getCatalog() {
		Movie movie = restTemplate.getForObject("http://movie-info-service/movies/100", Movie.class);
		return movie.getName();
	}
}

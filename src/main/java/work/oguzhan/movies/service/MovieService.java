package work.oguzhan.movies.service;

import work.oguzhan.movies.repository.MovieRepository;
import work.oguzhan.movies.service.MovieService;
import work.oguzhan.movies.model.Movie;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieService {
	@Autowired
	private MovieRepository movieRepository;
	private static final Logger logger = LoggerFactory.getLogger(MovieService.class);
	
	public List<Movie> allMovies() {
		List<Movie> movies = movieRepository.findAll();
		logger.info("Movies retrieved: {}",movies);
		return movies;
	}
	
	public Optional<Movie> singleMovie(String imdbId) {
		return movieRepository.findMovieByImdbId(imdbId);
	}
}

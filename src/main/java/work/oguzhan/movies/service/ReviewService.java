package work.oguzhan.movies.service;

import work.oguzhan.movies.repository.ReviewRepository;
import work.oguzhan.movies.model.Review;
import work.oguzhan.movies.model.Movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	public Review createReview(String reviewBody, String imdbId) {
		Review review = reviewRepository.insert(new Review(reviewBody));
		
		mongoTemplate.update(Movie.class)
			.matching(Criteria.where("imdbId").is(imdbId))
			.apply(new Update().push("reviewIds").value(review))
			.first();
		
		return review;
	}
}

package com.rbbnbb.TediTry1.services;

import com.rbbnbb.TediTry1.domain.*;
import com.rbbnbb.TediTry1.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class RecommendationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RecommendedRentalsRepository recommendedRentalsRepository;
    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private SearchHistoryRepository searchHistoryRepository;

    private final Double alpha = 0.0002;
    private final Double beta = 0.02;
    private final Double epsilon = 0.001;
    private final Integer steps = 5000;
    private final Integer K = 3;
    private final Integer rentalsToRecommend = 5;

    public RecommendationService(){}

    private class RentalRating{
        private Rental rental;
        private Double expRating;

        public RentalRating() {}

        public RentalRating(Rental rental, Double expRating) {
            this.rental = rental;
            this.expRating = expRating;
        }

        public RentalRating(Double expRating){
            this.expRating = expRating;
        }

        public Rental getRental() {
            return rental;
        }

        public void setRental(Rental rental) {
            this.rental = rental;
        }

        public Double getExpRating() {
            return expRating;
        }

        public void setExpRating(Double expRating) {
            this.expRating = expRating;
        }
    }


    public void updateRecommendationTable(){
        List<User> allTenants = userRepository.findAll();
        allTenants.removeIf(u -> (!u.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().contains("TENANT")));
        for (User tenant: allTenants) {
            Set<Rental> rentals = recommend(tenant);
            Optional<RecommendedRentals> optional = recommendedRentalsRepository.findByTenant(tenant);
            if (optional.isEmpty()){
                recommendedRentalsRepository.save(new RecommendedRentals(tenant,rentals));
                continue;
            }
            RecommendedRentals recommendedRentals = optional.get();
            recommendedRentals.setRentals(rentals);
            recommendedRentalsRepository.save(recommendedRentals);
        }
    }
    private Set<Rental> recommend(User tenant) {
        List<Review> userReviews = new ArrayList<>(reviewRepository.findByReviewer(tenant));
        if (userReviews.isEmpty()) return recommendBasedOnSearchHistory(tenant);
        List<Booking> userBookings = new ArrayList<>(bookingRepository.findByBooker(tenant));
        return recommendBasedOnBookingsAndReviews(tenant,userBookings,userReviews);
    }

    private Set<Rental> recommendBasedOnBookingsAndReviews(User tenant, List<Booking> userBookings, List<Review> userReviews){
        List<Review> allReviews = reviewRepository.findAll();

        //Get the rental from each booking the user has made. Note: May include duplicate rentals if user booked the same rental twice
        List<Rental> userBookedRentals = new ArrayList<>();
        for (Booking b: userBookings) {
            userBookedRentals.add(b.getRental());
        }

        List<Rental> allRentals = new ArrayList<>(rentalRepository.findAll());
        List<User> allReviewers = new ArrayList<>(userRepository.findAll());
        //From all users, retain only all those who have reviewed a rental
        allReviewers.retainAll(allReviews.
                stream().
                map(Review::getReviewer).
                collect(Collectors.toList()));


        RentalRating[][] Ratings = new RentalRating[allReviewers.size()][allRentals.size()];
        RentalRating[][] UserFeatures = new RentalRating[Ratings.length][K];
        RentalRating[][] RentalFeatures = new RentalRating[K][Ratings[0].length];

        int userIndex = -1;

        Random rClass = new Random();

        //for all rows in R
        for (int i = 0; i < allReviewers.size(); i++) {
            User reviewer = allReviewers.get(i);

            //Row #i corresponds to user of interest
            if (reviewer.getId().equals(tenant.getId())) {
                userIndex = i;

                for (int j=0; j<allRentals.size(); j++){
                    Rental rental = allRentals.get(j);

                    //Fill the two matrices with random values
                    for (int k=0; k<K; k++) {
                        UserFeatures[i][k] = new RentalRating(rental, rClass.nextDouble());
                        RentalFeatures[k][j] = new RentalRating(rental, rClass.nextDouble());
                    }
                    //Create list of reviews by user of interest for this rental

                    //Get all user reviews
                    List<Review> userRentalReviews = new ArrayList<>(userReviews);

                    //Remove all that are not for this rental
                    userRentalReviews.removeIf(r -> !r.getRental().equals(rental));

                    //if no reviews, base rating on past bookings
                    if (userRentalReviews.isEmpty()){

                        //if no bookings, assign rating of 2
                        if (!userBookedRentals.contains(rental)){
                            Ratings[i][j] = new RentalRating(rental,2d);
                            continue;
                        }

                        //Base rating of booked rental
                        final double base = 2.3;
                        final double gainRate = 0.7;
                        int nBookings = 0;
                        for (Rental r: userBookedRentals) {
                            if (r.equals(rental)) nBookings++;
                        }
                        final double finalRating = Math.min(base + nBookings*gainRate,rentalsToRecommend);
                        Ratings[i][j] = new RentalRating(rental,finalRating);
                    }
                    else{ //Base rating on latest review
                        final double rating = getLastReviewRating(userRentalReviews);
                        Ratings[i][j] = new RentalRating(rental,rating);
                    }
                }
                continue;
            }

            //for all columns in Ratings
            for (int j=0; j < allRentals.size(); j++){
                Rental rental = allRentals.get(j);

                //Fill the two matrices with random values
                for (int k=0; k<K; k++) {
                    UserFeatures[i][k] = new RentalRating(rental, rClass.nextDouble());
                    RentalFeatures[k][j] = new RentalRating(rental, rClass.nextDouble());
                }

                //Get all reviews made on this rental
                List<Review> reviewList = new ArrayList<>(rental.getReviews());

                //Remove all reviews that were not made by current reviewer
                reviewList.removeIf(r -> !r.getReviewer().equals(reviewer));

                if (reviewList.isEmpty()){
                    Ratings[i][j] = new RentalRating(rental,2d);
                    continue;
                }
                //rating = rating made in the last review
                final double rating = getLastReviewRating(reviewList);
                Ratings[i][j] = new RentalRating(rental,rating);
            }
        }

        Object[] arrays = matrixFactorization(Ratings,UserFeatures,RentalFeatures);

        UserFeatures = (RentalRating[][]) arrays[0];
        RentalFeatures = (RentalRating[][]) arrays[1];

        RentalRating[][] dR = dotProduct(UserFeatures,RentalFeatures);

        List<RentalRating> rentalsOfInterest = new ArrayList<>();
        for (int j=0; j<dR[userIndex].length; j++) {
            if (!userBookedRentals.contains(dR[userIndex][j].getRental())){
                rentalsOfInterest.add(dR[userIndex][j]);
            }
        }

        rentalsOfInterest.sort(new Comparator<RentalRating>() {
            @Override
            public int compare(RentalRating r1, RentalRating r2) {
                //r2.compareTo(r1) because descending order is warranted
                return r2.getExpRating().compareTo(r1.getExpRating());
            }
        });


        //Get first {rentalsToRecommend} or all rentals of interest, whichever is lower
        int last = Math.min(rentalsOfInterest.size(), rentalsToRecommend);

        List<Rental> recommendedRentals = new ArrayList<>();
        rentalsOfInterest = rentalsOfInterest.subList(0,last);
        for (RentalRating info: rentalsOfInterest) {
            recommendedRentals.add(info.getRental());
            System.out.println("Recommending rental: "+ info.getRental().getTitle());
        }

        return new HashSet<>(recommendedRentals);

    }

    private Set<Rental> recommendBasedOnSearchHistory(User user){
        Optional<SearchHistory> optionalSearchHistory = searchHistoryRepository.findByUser(user);
        if (optionalSearchHistory.isEmpty()){ //No search history, recommend 5 most highly-rated rentals
            return recommendMostHighlyRated();
        }
        SearchHistory searchHistory = optionalSearchHistory.get();
        List<Search> searchList = searchHistory.getSearchList();

        List<Rental> allRentals = new ArrayList<>(rentalRepository.findAll());

        List<Review> allReviews = new ArrayList<>(reviewRepository.findAll());

        List<User> allReviewers = new ArrayList<>(userRepository.findAll());
        //From all users, retain only all those who have reviewed a rental
        allReviewers.retainAll(allReviews.
                stream().
                map(Review::getReviewer).
                collect(Collectors.toList()));

        RentalRating[][] Ratings = new RentalRating[allReviewers.size()+1][allRentals.size()];
        RentalRating[][] UserFeatures = new RentalRating[Ratings.length][K];
        RentalRating[][] RentalFeatures = new RentalRating[K][Ratings[0].length];
        Random random = new Random();
        final int userIndex = 0;

        final double rentalWeight = 50d;
        final double countryWeight = 1d;
        final double cityWeight = 3d;
        final double neighbourhoodWeight = 25d;
        double avgGuestsWeight = 1.5;
        double booleanWeight = 0.7;
        double wifiWeight = 0d, acWeight = 0d, heatingWeight = 0d, kitchenWeight = 0d, tvWeight = 0d, parkingWeight = 0d, elevatorWeight = 0d;

        double avgGuests = 0d;

        //Represents how many times the tenant "ticked" each respective box relative to the number of total searches
        //higher frequency -> stronger correlation -> attribute is more important
        double wifiFreq = 0d;
        double acFreq = 0d;
        double heatingFreq = 0d;
        double kitchenFreq = 0d;
        double tvFreq = 0d;
        double parkingFreq = 0d;
        double elevatorFreq = 0d;

        for (Search s: searchList) {
            Integer guests = s.getGuests();

            Boolean hasWiFi = s.getHasWiFi();
            Boolean hasAC = s.getHasAC();
            Boolean hasHeating = s.getHasHeating();
            Boolean hasKitchen = s.getHasKitchen();
            Boolean hasTV = s.getHasTV();
            Boolean hasParking = s.getHasParking();
            Boolean hasElevator = s.getHasElevator();

            avgGuests += guests / (double) searchList.size();

            if (Objects.nonNull(hasWiFi) && hasWiFi) wifiFreq += 1 / (double) searchList.size();
            if (Objects.nonNull(hasAC) && hasAC) acFreq += 1 / (double) searchList.size();
            if (Objects.nonNull(hasHeating) && hasHeating) heatingFreq += 1 / (double) searchList.size();
            if (Objects.nonNull(hasKitchen) && hasKitchen) kitchenFreq += 1 / (double) searchList.size();
            if (Objects.nonNull(hasTV) && hasTV) tvFreq += 1 / (double) searchList.size();
            if (Objects.nonNull(hasParking) && hasParking) parkingFreq += 1 / (double) searchList.size();
            if (Objects.nonNull(hasElevator) && hasWiFi) elevatorFreq += 1 / (double) searchList.size();
        }


        for (int j=0; j<allRentals.size(); j++) {
            Rental rental = allRentals.get(j);

            //Fill the two matrices with random values
            for (int k = 0; k < K; k++) {
                UserFeatures[userIndex][k] = new RentalRating(rental, random.nextDouble());
                RentalFeatures[k][j] = new RentalRating(rental, random.nextDouble());
            }

            double countryFreq = 0d;
            double cityFreq = 0d;
            double neighbourhoodFreq = 0d;


            int totalVisits = 0;
            int rentalVisits = 0;
            Map<Rental,Integer> rentalMap = searchHistory.getRentalMap();
            for(Map.Entry<Rental,Integer> entry : rentalMap.entrySet()){
                totalVisits += entry.getValue();
                if (entry.getKey().equals(rental)) rentalVisits = entry.getValue();
            }
            double rentalFreq;
            if (totalVisits != 0) rentalFreq = rentalVisits / (double) totalVisits;
            else rentalFreq = 0;

            //Count the frequency(percentage of searches where a value appears) for each value.
            //A for loop is used instead of Collections.frequency to improve time complexity by only iterating through the list once
            for (Search s: searchList) {
                String country = s.getCountry();
                String city = s.getCity();
                String neighbourhood = s.getNeighbourhood();

                Address address = rental.getAddress();
                if (Objects.nonNull(country) && country.equals(address.getCountry())) countryFreq += 1/(double)searchList.size();
                if (Objects.nonNull(city) && city.equals(address.getCity())) cityFreq += 1/(double)searchList.size();
                if (Objects.nonNull(neighbourhood) && neighbourhood.equals(address.getNeighbourhood())) neighbourhoodFreq += 1/(double)searchList.size();

            }

            if (rental.getMaxGuests() < avgGuests) avgGuestsWeight = -avgGuestsWeight;
            if (rental.getHasWiFi()) wifiWeight = wifiFreq; else wifiWeight = 1 - wifiFreq;
            if (rental.getHasAC()) acWeight = acFreq; else acWeight = 1 - acFreq;
            if (rental.getHasHeating()) heatingWeight = heatingFreq; else heatingWeight = 1 - heatingFreq;
            if (rental.getHasKitchen()) kitchenWeight = kitchenFreq; else kitchenWeight = 1 - kitchenFreq;
            if (rental.getHasTV()) tvWeight = tvFreq; else tvWeight = 1 - tvFreq;
            if (rental.getHasParking()) parkingWeight = parkingFreq; else parkingWeight = 1 - parkingFreq;
            if (rental.getHasElevator()) elevatorWeight = elevatorFreq; else elevatorWeight = 1 - elevatorFreq;


            double similarityScore = rentalWeight*rentalFreq
                    + countryWeight*countryFreq
                    + cityWeight*cityFreq
                    + neighbourhoodWeight*neighbourhoodFreq
                    + avgGuestsWeight
                    + booleanWeight * (wifiWeight + acWeight + heatingWeight + kitchenWeight + tvWeight + parkingWeight + elevatorWeight);

            double rating = ratingSigmoid(similarityScore);
            System.out.println("rating is " + rating);
            Ratings[userIndex][j] = new RentalRating(rental,rating);
        }

        //for all rows in R
        for (int i = 1; i < allReviewers.size() + 1; i++) {
            User reviewer = allReviewers.get(i - 1);

            //for all columns in R
            for (int j=0; j < allRentals.size(); j++){
                Rental rental = allRentals.get(j);

                //Fill the two matrices with random values
                for (int k=0; k<K; k++) {
                    UserFeatures[i][k] = new RentalRating(rental, random.nextDouble());
                    RentalFeatures[k][j] = new RentalRating(rental, random.nextDouble());
                }

                //Get all reviews made on this rental
                List<Review> reviewList = new ArrayList<>(rental.getReviews());

                //Remove all reviews that were not made by current reviewer
                reviewList.removeIf(r -> !r.getReviewer().equals(reviewer));

                if (reviewList.isEmpty()){
                    Ratings[i][j] = new RentalRating(rental,2d);
                    continue;
                }
                //rating = rating made in the last review
                final double rating = getLastReviewRating(reviewList);
                Ratings[i][j] = new RentalRating(rental,rating);
            }
        }

        Object[] arrays = matrixFactorization(Ratings,UserFeatures,RentalFeatures);
        UserFeatures = (RentalRating[][]) arrays[0];
        RentalFeatures = (RentalRating[][]) arrays[1];
        RentalRating[][] dR = dotProduct(UserFeatures,RentalFeatures);

        List<RentalRating> rentalsOfInterest = new ArrayList<>(Arrays.asList(dR[userIndex]));

        rentalsOfInterest.sort(new Comparator<RentalRating>() {
            @Override
            public int compare(RentalRating r1, RentalRating r2) {
                //r2.compareTo(r1) because descending order is warranted
                return r2.getExpRating().compareTo(r1.getExpRating());
            }
        });


        //Get first {rentalsToRecommend}, unless list doesn't contain enough
        int last = Math.min(rentalsOfInterest.size(), rentalsToRecommend);

        List<Rental> recommendedRentals = new ArrayList<>();
        rentalsOfInterest = rentalsOfInterest.subList(0,last);
        for (RentalRating info: rentalsOfInterest) {
            recommendedRentals.add(info.getRental());
            System.out.println("Recommending rental: "+ info.getRental().getTitle());
        }

        return new HashSet<>(recommendedRentals);
    }

    private Set<Rental> recommendMostHighlyRated(){
        List<Rental> rentalList = rentalRepository.findAll();

        final int minReviews = 20;

        rentalList.removeIf(r -> reviewRepository.countByRental(r) < minReviews);

        if (rentalList.isEmpty()) return new HashSet<>(rentalList);
        rentalList.sort(new Comparator<Rental>() {
            @Override
            public int compare(Rental r1, Rental r2) {
                //r2.compareTo(r1) because descending order is warranted
                return r2.getRating().compareTo(r1.getRating());
            }
        });
        int last = Math.min(rentalList.size(),rentalsToRecommend);
        return new HashSet<>(rentalList.subList(0,last));
    }


    //x simulates similarity with past searches, return value simulates expected rating
    //high similarity -> high rating, capped in range [1,5] to simulate star rating in reviews
    private Double ratingSigmoid(double x){
        final double lambda = 0.7; //True sigmoid when lambda = 1. Lambda --> inf => sign function, Lambda --> 0 => y=0.5
        final double offset = 5d; //Without it, sigmoid(0) would return 3. Now it returns 0.000... and sigmoid(offset) = 3
                                  //Offset essentially acts as protection against high scores with low x. higher offset -> higher x needed for good rating.
        final double newX = lambda*(- x + offset);
        double sigmoid = 1 / (1 + Math.exp(newX)); //ranges between 0 and 1
        return 4 * sigmoid + 1; //finally ranges between 1 and 5
    }

    private double getLastReviewRating(List<Review> reviewList){
        reviewList.sort(new Comparator<Review>() {
            @Override
            public int compare(Review r1, Review r2) {
                //r2.compare(r1) for descending order
                return r2.getIssuedAt().compareTo(r1.getIssuedAt());
            }
        });
        return reviewList.get(0).getStars();
    }

    private Object[] matrixFactorization(RentalRating[][] Ratings, RentalRating[][] UserFeatures, RentalRating[][] RentalFeatures){
        for (int s=0; s<steps; s++){
            for(int i=0; i<Ratings.length; i++){
                for(int j=0; j<Ratings[i].length; j++) {
                    double expRatingR = Ratings[i][j].getExpRating();
                    double eij = expRatingR - dotProduct(UserFeatures[i],getColumn(RentalFeatures,j));

                    for(int k=0; k<K; k++){
                        RentalRating p = UserFeatures[i][k];
                        RentalRating q = RentalFeatures[k][j];
                        double expRatingUserFeatures = p.getExpRating();
                        double expRatingRentalFeatures = q.getExpRating();
                        p.setExpRating(expRatingUserFeatures + alpha * (2 * eij * expRatingRentalFeatures - beta * expRatingUserFeatures));
                        q.setExpRating(expRatingRentalFeatures + alpha * (2 + eij * expRatingUserFeatures - beta * expRatingRentalFeatures));
                    }

                }
            }
            double e = 0d;

            for(int i=0; i<Ratings.length; i++){
                for(int j=0; j<Ratings[i].length; j++){
                    double calculatedValue = dotProduct(UserFeatures[i],getColumn(RentalFeatures,j)); //==R'
                    double difference = Ratings[i][j].getExpRating() - calculatedValue; //== R - R'
                            e += Math.pow(difference,2);
                    for(int k=0; k<K; k++){
                        double pikSquared = Math.pow(UserFeatures[i][k].getExpRating(),2); //UserFeatures[i][k]^2
                        double qkjSquared = Math.pow(RentalFeatures[k][j].getExpRating(),2); //RentalFeatures[k][j]^2
                        e += (beta / 2) * pikSquared + qkjSquared;
                    }
                }
            }
            if (e < epsilon){
                break;
            }
        }
        return new Object[]{UserFeatures,RentalFeatures};
    }


    private RentalRating[][] transpose(RentalRating[][] M){
        int rows = M.length;
        int columns = M[0].length;

        RentalRating[][] T = new RentalRating[columns][rows];

        for (int i=0; i<columns; i++){
            for(int j=0; j<rows; j++){
                T[i][j] = M[j][i];
            }
        }
        return T;
    }
    private Double dotProduct(RentalRating[] A, RentalRating[] B)
    {
        double product = 0d;
        final int n = A.length;

        // Loop for calculate dot product
        for (int i = 0; i < n; i++)
            product += A[i].getExpRating() * B[i].getExpRating();
        return product;
    }

    private RentalRating[][] dotProduct(RentalRating[][] A, RentalRating[][] B){
        RentalRating[][] D = new RentalRating[A.length][B[0].length];
        for (int i=0; i<A.length; i++) {
            for(int j=0; j<B[0].length; j++){
                D[i][j] = new RentalRating(0d);
            }
        }

        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < B[0].length; j++) {
                D[i][j].setRental(B[0][j].getRental());
                for (int k = 0; k < A[0].length; k++) {
                    D[i][j].setExpRating(D[i][j].getExpRating() + A[i][k].getExpRating() * B[k][j].getExpRating());
                }
            }
        }
        return D;
    }

    private RentalRating[] getColumn(RentalRating[][] A, int index){
        RentalRating[] column = new RentalRating[A.length];
        for(int i=0; i<column.length; i++){
            column[i] = A[i][index];
        }
        return column;
    }
}

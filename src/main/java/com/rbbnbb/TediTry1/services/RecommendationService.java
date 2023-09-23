package com.rbbnbb.TediTry1.services;

import com.rbbnbb.TediTry1.domain.*;
import com.rbbnbb.TediTry1.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SearchHistoryRepository searchHistoryRepository;

    private static final Double alpha = 0.0002;
    private static final Double beta = 0.02;
    private static final Double epsilon = 0.001;
    private static final Integer steps = 1000;

    private static final Integer rentalsToRecommend = 5;

    private RentalInfo[][] P;
    private RentalInfo[][] Q;

    public RecommendationService(){}

    private class RentalInfo{
        private Rental rental;
        private Double expRating;

        public RentalInfo() {}

        public RentalInfo(Rental rental, Double expRating) {
            this.rental = rental;
            this.expRating = expRating;
        }

        public RentalInfo(Double expRating){
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
    public List<Rental> recommend(User tenant) {

        List<Review> userReviews = new ArrayList<>(reviewRepository.findByReviewer(tenant));
//        if (userReviews.isEmpty()) return recommendBasedOnSearchHistory(tenant);
        List<Booking> userBookings = new ArrayList<>(bookingRepository.findByBooker(tenant));

        final int bookingWeight = 2;
        final int reviewWeight = 3;
        final int minimumThreshold = 0;
        if (bookingWeight*userBookings.size() + reviewWeight*userReviews.size() >= minimumThreshold)
            return recommendBasedOnBookingsAndReviews(tenant,userBookings,userReviews);
        return null;
//        return recommendBasedOnSearchHistory(tenant);

//        List<Review> allReviews = reviewRepository.findAll(); // ==R
//
//        List<User> allReviewers = new ArrayList<>(userRepository.findAll());
//        List<Rental> allRentals = new ArrayList<>(rentalRepository.findAll());
//
//        List<Rental> userBookedRentals = new ArrayList<>();
//        for (Booking b: userBookings) {
//            userBookedRentals.add(b.getRental());
//        }
//
//
//        //From all users, retain only all those who have reviewed a rental
//        allReviewers.retainAll(allReviews.
//                                stream().
//                                map(Review::getReviewer).
//                                collect(Collectors.toList()));
//
//
//        final int K = 2;
////        Array[][] P, Q;
////        Array[][] R = allReviews.stream()
////                .map(l -> l.getStars().stream().mapToInt(Integer::intValue).toArray())
////                .toArray(int[][]::new)b
//
//
//
//
//
//
//        RentalInfo[][] R = new RentalInfo[allReviewers.size()][allRentals.size()];
//        P = new RentalInfo[R.length][K];
//        Q = new RentalInfo[K][R[0].length];
//
//        int userIndex = -1;
//        List<Rental> alreadyReviewed = new ArrayList<>();
//        for (Review r: userReviews) {
//            alreadyReviewed.add(r.getRental());
//        }
//
//        Random rClass = new Random();
//        System.out.println("There are " + allReviewers.size() + " reviewers, " + allRentals.size() + " rentals and " + allReviews.size() + " reviews");
//
//        //for all rows in R
//        for (int i = 0; i < allReviewers.size(); i++) {
//           User reviewer = allReviewers.get(i);
//
//           //Row #i corresponds to user of interest
//           if (reviewer.equals(tenant)) userIndex = i;
//
//           //for all columns in R
//           for (int j=0; j < allRentals.size(); j++){
//               Rental rental = allRentals.get(j);
//
//               //Fill the two matrices with random values
//               for (int k=0; k<K; k++) {
//                   P[i][k] = new RentalInfo(rental, rClass.nextDouble());
//                   Q[k][j] = new RentalInfo(rental, rClass.nextDouble());
//               }
//
//               //Get all reviews made on this rental
//               List<Review> reviewList = new ArrayList<>(rental.getReviews());
//
//               //Remove all reviews that were not made by current reviewer
//               reviewList.removeIf(t -> !t.getReviewer().equals(reviewer));
//
//               if (reviewList.isEmpty()){
//                   R[i][j] = new RentalInfo(rental,3d); //Average of 1,2,3,4,5]
//                   continue;
//               }
//               //reviewList is not empty. Sort it by time of review descending, and get the (chronologically) last review.
//               reviewList.sort(new Comparator<Review>() {
//                   @Override
//                   public int compare(Review r1, Review r2) {
//                       //r2.compare(r1) for descending order
//                       return r2.getIssuedAt().compareTo(r1.getIssuedAt());
//                   }
//               });
//               Review r = reviewList.get(0);
//               R[i][j] = new RentalInfo(rental,(double)r.getStars());
//           }
//        }
//
//        System.out.println("before matrix factorization");
//        matrixFactorization(R,P,Q,K);
//        System.out.println("after matrix factorization");
//
//        RentalInfo[][] dR = dotProduct(this.P,this.Q);
//
//        List<RentalInfo> rentalsOfInterest = new ArrayList<>();
//        for (int j=0; j<dR[userIndex].length; j++) {
//            if (!alreadyReviewed.contains(dR[userIndex][j].getRental())){
//                rentalsOfInterest.add(dR[userIndex][j]);
//            }
//        }
//
//        System.out.println("before sorting");
//
//        rentalsOfInterest.sort(new Comparator<RentalInfo>() {
//            @Override
//            public int compare(RentalInfo r1, RentalInfo r2) {
//                //r2.compareTo(r1) because descending order is warranted
//                return r2.getExpRating().compareTo(r1.getExpRating());
//            }
//        });
//
//        System.out.println("after sorting");
//
//        //Get first 5 or all rentals of interest, whichever is lower
//        int last = Math.min(rentalsOfInterest.size(), 5);
//
//        List<Rental> recommendedRentals = new ArrayList<>();
//        rentalsOfInterest = rentalsOfInterest.subList(0,last);
//        for (RentalInfo info: rentalsOfInterest) {
//            recommendedRentals.add(info.getRental());
//            System.out.println("Recommending rental: "+ info.getRental().getTitle());
//        }
//
//        return recommendedRentals;

    }

    public List<Rental> recommendBasedOnBookingsAndReviews(User tenant, List<Booking> userBookings, List<Review> userReviews){

        List<Review> allReviews = reviewRepository.findAll(); // ==R

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


        final int K = 2;

        RentalInfo[][] R = new RentalInfo[allReviewers.size()][allRentals.size()];
        P = new RentalInfo[R.length][K];
        Q = new RentalInfo[K][R[0].length];

        int userIndex = -1;

        Random rClass = new Random();
        System.out.println("There are " + allReviewers.size() + " reviewers, " + allRentals.size() + " rentals and " + allReviews.size() + " reviews");

        //for all rows in R
        for (int i = 0; i < allReviewers.size(); i++) {
            User reviewer = allReviewers.get(i);

            //Row #i corresponds to user of interest
            if (reviewer.equals(tenant)) {
                userIndex = i;

                for (int j=0; j<allRentals.size(); j++){
                    Rental rental = allRentals.get(j);

                    //Fill the two matrices with random values
                    for (int k=0; k<K; k++) {
                        P[i][k] = new RentalInfo(rental, rClass.nextDouble());
                        Q[k][j] = new RentalInfo(rental, rClass.nextDouble());
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
                            R[i][j] = new RentalInfo(rental,2d);
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
                        R[i][j] = new RentalInfo(rental,finalRating);
                    }
                    else{ //Base rating on latest review
                        final double rating = getLastReviewRating(userRentalReviews);
                        R[i][j] = new RentalInfo(rental,rating);
                    }
                }
                continue;
            }

            //for all columns in R
            for (int j=0; j < allRentals.size(); j++){
                Rental rental = allRentals.get(j);

                //Fill the two matrices with random values
                for (int k=0; k<K; k++) {
                    P[i][k] = new RentalInfo(rental, rClass.nextDouble());
                    Q[k][j] = new RentalInfo(rental, rClass.nextDouble());
                }

                //Get all reviews made on this rental
                List<Review> reviewList = new ArrayList<>(rental.getReviews());

                //Remove all reviews that were not made by current reviewer
                reviewList.removeIf(r -> !r.getReviewer().equals(reviewer));

                if (reviewList.isEmpty()){
                    R[i][j] = new RentalInfo(rental,2d);
                    continue;
                }
                //rating = rating made in the last review
                final double rating = getLastReviewRating(reviewList);
                R[i][j] = new RentalInfo(rental,rating);
            }
        }

        System.out.println("before matrix factorization");
        matrixFactorization(R,P,Q,K);
        System.out.println("after matrix factorization");

        RentalInfo[][] dR = dotProduct(this.P,this.Q);

        List<RentalInfo> rentalsOfInterest = new ArrayList<>();
        for (int j=0; j<dR[userIndex].length; j++) {
            if (!userBookedRentals.contains(dR[userIndex][j].getRental())){
                rentalsOfInterest.add(dR[userIndex][j]);
            }
        }

        System.out.println("before sorting");

        rentalsOfInterest.sort(new Comparator<RentalInfo>() {
            @Override
            public int compare(RentalInfo r1, RentalInfo r2) {
                //r2.compareTo(r1) because descending order is warranted
                return r2.getExpRating().compareTo(r1.getExpRating());
            }
        });

        System.out.println("after sorting");

        //Get first {rentalsToRecommend} or all rentals of interest, whichever is lower
        int last = Math.min(rentalsOfInterest.size(), rentalsToRecommend);

        List<Rental> recommendedRentals = new ArrayList<>();
        rentalsOfInterest = rentalsOfInterest.subList(0,last);
        for (RentalInfo info: rentalsOfInterest) {
            recommendedRentals.add(info.getRental());
            System.out.println("Recommending rental: "+ info.getRental().getTitle());
        }

        return recommendedRentals;

    }

    public List<Rental> recommendBasedOnSearchHistory(User user){
        Optional<SearchHistory> optionalSearchHistory = searchHistoryRepository.findByUser(user);
        if (optionalSearchHistory.isEmpty()){ //No search history, recommend 5 most highly-rated rentals
            return recommendMostHighlyRated();
        }
        SearchHistory searchHistory = optionalSearchHistory.get();
        List<Search> searchList = searchHistory.getSearchList();
        List<Rental> rentalsVisited = searchHistory.getRentalList();

        List<Rental> allRentals = new ArrayList<>(rentalRepository.findAll());

        List<Review> allReviews = new ArrayList<>(reviewRepository.findAll());

        List<User> allReviewers = new ArrayList<>(userRepository.findAll());
        //From all users, retain only all those who have reviewed a rental
        allReviewers.retainAll(allReviews.
                stream().
                map(Review::getReviewer).
                collect(Collectors.toList()));

        final int K = 2;

        RentalInfo[][] R = new RentalInfo[allReviewers.size()+1][allRentals.size()];
        P = new RentalInfo[R.length][K];
        Q = new RentalInfo[K][R[0].length];
        Random random = new Random();
        final int userIndex = 0;

        //Notes
        //country => 90% appearances
        //city => 45% appearances
        //neighbourhood => 10% appearances
        //rental => 5% appearances
        //avgGuests => -2 OR 2

        final double rentalWeight = 100d;
        final double countryWeight = 1d;
        final double cityWeight = 5d;
        final double neighbourhoodWeight = 25d;
        final double avgGuestsWeight = 5d;

        for (int j=0; j<allRentals.size(); j++) {
            Rental rental = allRentals.get(j);

            //Fill the two matrices with random values
            for (int k = 0; k < K; k++) {
                P[userIndex][k] = new RentalInfo(rental, random.nextDouble());
                Q[k][j] = new RentalInfo(rental, random.nextDouble());
            }

            double rentalFreq = Collections.frequency(rentalsVisited, rental) / (double) rentalsVisited.size();
            double countryFreq = Collections.frequency(
                    searchList.
                            stream().
                            map(Search::getCountry).
                            toList(),
                    rental.
                            getAddress().
                            getCountry())
                    / (double) searchList.size();


            double cityFreq = Collections.frequency(
                    searchList.
                            stream().
                            map(Search::getCity).
                            toList(),
                    rental.
                            getAddress().
                            getCity())
                    / (double) searchList.size();

            double neighbourhoodFreq = Collections.frequency(
                    searchList.
                            stream().
                            map(Search::getNeighbourhood).
                            toList(),
                    rental.
                            getAddress().
                            getNeighbourhood())
                    / (double) searchList.size();

            //Average number of guests in searches
            double averageGuests = 0;
            OptionalDouble average = searchList.stream().mapToDouble(Search::getGuests).average();
            if (average.isPresent()) averageGuests = average.getAsDouble();
            double x = 0d;
        }

//
//
//            //Get all user reviews
//            List<Review> userRentalReviews = new ArrayList<>(userReviews);
//
//            //Remove all that are not for this rental
//            userRentalReviews.removeIf(r -> !r.getRental().equals(rental));
//
//            //if no reviews, base rating on past bookings
//            if (userRentalReviews.isEmpty()){
//
//                //if no bookings, assign rating of 2
//                if (!userBookedRentals.contains(rental)){
//                    R[i][j] = new RentalInfo(rental,2d);
//                    continue;
//                }
//
//                //Base rating of booked rental
//                final double base = 2.3;
//                final double gainRate = 0.7;
//                int nBookings = 0;
//                for (Rental r: userBookedRentals) {
//                    if (r.equals(rental)) nBookings++;
//                }
//                final double finalRating = Math.min(base + nBookings*gainRate,rentalsToRecommend);
//                R[i][j] = new RentalInfo(rental,finalRating);
//            }
//            else{ //Base rating on latest review
//                final double rating = getLastReviewRating(userRentalReviews);
//                R[i][j] = new RentalInfo(rental,rating);
//            }
//        }
//
//        //for all rows in R
//        for (int i = 1; i < allReviewers.size(); i++) {
//            User reviewer = allReviewers.get(i);
//
//            //for all columns in R
//            for (int j=0; j < allRentals.size(); j++){
//                Rental rental = allRentals.get(j);
//
//                //Fill the two matrices with random values
//                for (int k=0; k<K; k++) {
//                    P[i][k] = new RentalInfo(rental, rClass.nextDouble());
//                    Q[k][j] = new RentalInfo(rental, rClass.nextDouble());
//                }
//
//                //Get all reviews made on this rental
//                List<Review> reviewList = new ArrayList<>(rental.getReviews());
//
//                //Remove all reviews that were not made by current reviewer
//                reviewList.removeIf(r -> !r.getReviewer().equals(reviewer));
//
//                if (reviewList.isEmpty()){
//                    R[i][j] = new RentalInfo(rental,2d);
//                    continue;
//                }
//                //rating = rating made in the last review
//                final double rating = getLastReviewRating(reviewList);
//                R[i][j] = new RentalInfo(rental,rating);
//            }
//        }
//
//        System.out.println("before matrix factorization");
//        matrixFactorization(R,P,Q,K);
//        System.out.println("after matrix factorization");
//
//        RentalInfo[][] dR = dotProduct(this.P,this.Q);
//
//        List<RentalInfo> rentalsOfInterest = new ArrayList<>();
//        for (int j=0; j<dR[userIndex].length; j++) {
//            if (!userBookedRentals.contains(dR[userIndex][j].getRental())){
//                rentalsOfInterest.add(dR[userIndex][j]);
//            }
//        }
//
//        System.out.println("before sorting");
//
//        rentalsOfInterest.sort(new Comparator<RentalInfo>() {
//            @Override
//            public int compare(RentalInfo r1, RentalInfo r2) {
//                //r2.compareTo(r1) because descending order is warranted
//                return r2.getExpRating().compareTo(r1.getExpRating());
//            }
//        });
//
//        System.out.println("after sorting");
//
//        //Get first {rentalsToRecommend} or all rentals of interest, whichever is lower
//        int last = Math.min(rentalsOfInterest.size(), rentalsToRecommend);
//
//        List<Rental> recommendedRentals = new ArrayList<>();
//        rentalsOfInterest = rentalsOfInterest.subList(0,last);
//        for (RentalInfo info: rentalsOfInterest) {
//            recommendedRentals.add(info.getRental());
//            System.out.println("Recommending rental: "+ info.getRental().getTitle());
//        }
//
//        return recommendedRentals;

        return null;
    }

    private List<Rental> recommendMostHighlyRated(){
        List<Rental> rentalList = rentalRepository.findAll();

        final int minReviews = 20;

        rentalList.removeIf(r -> reviewRepository.countByRental(r) < minReviews);
        rentalList.sort(new Comparator<Rental>() {
            @Override
            public int compare(Rental r1, Rental r2) {
                //r2.compareTo(r1) because descending order is warranted
                return r2.getRating().compareTo(r1.getRating());
            }
        });
        int last = Math.min(rentalList.size(),rentalsToRecommend);
        return rentalList.subList(0,last);
    }

    //x simulates similarity with past searches, return value simulates expected rating
    //high similarity -> high rating, capped in range [1,5] to simulate star rating in reviews
    private Double sigmoidFunction(double x){
        final double lambda = 0.7; //True sigmoid when lambda = 1. Lambda --> inf => sign function, Lambda --> 0 => y=0.5
        final double offset = 5d; //Without it, sigmoid(0) would return 3. Now it returns 0.000... and sigmoid(offset) = 3
                                  //Offset essentially acts as protection against high scores with low x. higher offset -> higher x needed for good rating.
        final double newX = x - offset;
        double sigmoid = 1 / (1 + lambda * Math.exp(newX)); //ranges between 0 and 1
        return 4 * sigmoid + 1; //finally ranges between 1 and 5
    }

    private Double rentalSimilarity(Rental r1, Rental r2){
        double[] v1 = new double[100];
        double[] v2 = new double[100];

        v1[0] = r1.getBasePrice();
        v2[0] = r2.getBasePrice();

        v1[1] = r1.getChargePerPerson();
        v2[1] = r2.getChargePerPerson();

//        v1[2] =

        return 0.5;
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

    private void matrixFactorization(RentalInfo[][] R, RentalInfo[][] P, RentalInfo[][] Q, final int K){
        for (int s=0; s<steps; s++){
            for(int i=0; i<R.length; i++){
                for(int j=0; j<R[i].length; j++) {
                    double expRatingR = R[i][j].getExpRating();
                    double eij = expRatingR - dotProduct(P[i],getColumn(Q,j));

                    for(int k=0; k<K; k++){
                        RentalInfo p = P[i][k];
                        RentalInfo q = Q[k][j];
                        double expRatingP = p.getExpRating();
                        double expRatingQ = q.getExpRating();
                        p.setExpRating(expRatingP + alpha * (2 * eij * expRatingQ - beta * expRatingP)); //P[i][k] = P[i][k] + alpha * (2 * eij * Q[k][j] - beta * P[i][k])
                        q.setExpRating(expRatingQ + alpha * (2 + eij * expRatingP - beta * expRatingQ)); //Q[k][j] = Q[k][j] + alpha * (2 * eij * P[i][k] - beta * Q[k][j])
//                            P[i][k] = P[i][k] + alpha * (2 * eij * Q[k][j] - beta * P[i][k]);
//                            Q[k][j] = Q[k][j] + alpha * (2 * eij * P[i][k] - beta * Q[k][j]);
                    }

                }
            }
//            RentalInfo[][] eR = dotProduct(P,Q);
            double e = 0d;


            for(int i=0; i<R.length; i++){
                for(int j=0; j<R[i].length; j++){
                    double calculatedValue = dotProduct(P[i],getColumn(Q,j)); //==R'
                    double difference = R[i][j].getExpRating() - calculatedValue; //== R - R'
                            e += Math.pow(difference,2);
                    for(int k=0; k<K; k++){
                        double pikSquared = Math.pow(P[i][k].getExpRating(),2); //P[i][k]^2
                        double qkjSquared = Math.pow(Q[k][j].getExpRating(),2); //Q[k][j]^2
                        e += (beta / 2) * pikSquared + qkjSquared;
                    }
                }
            }
            if (e < epsilon){
                break;
            }
        }
        this.P = P;
        this.Q = Q;
    }


    private static RentalInfo[][] transpose(RentalInfo[][] M){
        int rows = M.length;
        int columns = M[0].length;

        RentalInfo[][] T = new RentalInfo[columns][rows];

        for (int i=0; i<columns; i++){
            for(int j=0; j<rows; j++){
                T[i][j] = M[j][i];
            }
        }
        return T;
    }
    private static Double dotProduct(RentalInfo[] A, RentalInfo[] B)
    {
        double product = 0d;
        final int n = A.length;

        // Loop for calculate dot product
        for (int i = 0; i < n; i++)
            product += A[i].getExpRating() * B[i].getExpRating();
        return product;
    }

    private RentalInfo[][] dotProduct(RentalInfo[][] A, RentalInfo[][] B){
        RentalInfo[][] D = new RentalInfo[A.length][B[0].length];
        for (int i=0; i<A.length; i++) {
            for(int j=0; j<B[0].length; j++){
                D[i][j] = new RentalInfo(0d);
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

    private static RentalInfo[] getColumn(RentalInfo[][] A, int index){
        RentalInfo[] column = new RentalInfo[A.length];
        for(int i=0; i<column.length; i++){
            column[i] = A[i][index];
        }
        return column;
    }

//    private RentalInfo[][] fill(int rows, int columns) {
//        Random rClass = new Random();
//
//        RentalInfo[][] array = new RentalInfo[rows][columns];
//
//        for(int i=0; i<rows; i++){
//            for(int j=0; j<columns; j++) {
//                array[i][j] = rClass.nextDouble();
//            }
//        }
//        return array;
//    }

    public RentalInfo[][] getP() {
        return P;
    }

    public void setP(RentalInfo[][] p) {
        P = p;
    }

    public RentalInfo[][] getQ() {
        return Q;
    }

    public void setQ(RentalInfo[][] q) {
        Q = q;
    }
}







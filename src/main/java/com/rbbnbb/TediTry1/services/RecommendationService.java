package com.rbbnbb.TediTry1.services;

import com.rbbnbb.TediTry1.domain.Rental;
import com.rbbnbb.TediTry1.domain.Review;
import com.rbbnbb.TediTry1.domain.User;
import com.rbbnbb.TediTry1.repository.RentalRepository;
import com.rbbnbb.TediTry1.repository.ReviewRepository;
import com.rbbnbb.TediTry1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
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

    private static final Double alpha = 0.0002;
    private static final Double beta = 0.02;
    private static final Double epsilon = 0.001;
    private static final Integer steps = 5000;

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
        Array[][] ratings;

        List<Review> allReviews = reviewRepository.findAll(); // ==R
        List<Review> userReviews = new ArrayList<>(reviewRepository.findByReviewer(tenant));
        List<User> allReviewers = new ArrayList<>(userRepository.findAll());
        List<Rental> allRentals = new ArrayList<>(rentalRepository.findAll());

        //From all users, retain only all those who have reviewed a rental
        allReviewers.retainAll(allReviews.
                                stream().
                                map(Review::getReviewer).
                                collect(Collectors.toList()));


        final int K = 10;
//        Array[][] P, Q;
//        Array[][] R = allReviews.stream()
//                .map(l -> l.getStars().stream().mapToInt(Integer::intValue).toArray())
//                .toArray(int[][]::new)b






        RentalInfo[][] R = new RentalInfo[allReviewers.size()][allRentals.size()];
        P = new RentalInfo[R.length][K];
        Q = new RentalInfo[K][R[0].length];

        int userIndex = -1;
        List<Rental> alreadyReviewed = new ArrayList<>();
        for (Review r: userReviews) {
            alreadyReviewed.add(r.getRental());
        }

        Random rClass = new Random();

        for (int i = 0; i < allReviewers.size(); i++) {
           if (allReviewers.get(i).equals(tenant)) userIndex = i;
           for (int j=0; j < allRentals.size(); j++){
               Rental rental = allRentals.get(j);

               for (int k=0; k<K; k++) {
                   P[i][k] = new RentalInfo(rental, rClass.nextDouble());
                   Q[k][j] = new RentalInfo(rental, rClass.nextDouble());
               }

               //Get all reviews made by this user on this rental
               List<Review> reviewList = reviewRepository.findByReviewerAndRental(allReviewers.get(i),rental);
               if (reviewList.isEmpty()){
                   R[i][j] = new RentalInfo(rental,0d); //Average of 1,2,3,4,5]
                   continue;
               }
               //reviewList is not empty. Sort it by time of review descending, and get the (chronologically) last review.
               reviewList.sort(new Comparator<Review>() {
                   @Override
                   public int compare(Review r1, Review r2) {
                       //r2.compare(r1) for descending order
                       return r2.getIssuedAt().compareTo(r1.getIssuedAt());
                   }
               });
               Review r = reviewList.get(0);
               R[i][j] = new RentalInfo(rental,(double)r.getStars());
           }
        }


        matrixFactorization(R,P,Q,K);

        RentalInfo[][] dR = dotProduct(this.P,this.Q);

        List<RentalInfo> rentalsOfInterest = new ArrayList<>();
        for (int j=0; j<dR[userIndex].length; j++) {
            if (!alreadyReviewed.contains(dR[userIndex][j].getRental())){
                rentalsOfInterest.add(dR[userIndex][j]);
            }
        }

        rentalsOfInterest.sort(new Comparator<RentalInfo>() {
            @Override
            public int compare(RentalInfo r1, RentalInfo r2) {
                //r2.compareTo(r1) because descending order is warranted
                return r2.getExpRating().compareTo(r1.getExpRating());
            }
        });

        //Get first 5 or all rentals of interest, whichever is lower
        int last = (rentalsOfInterest.size() > 4) ? 4 : (rentalsOfInterest.size()-1);

        List<Rental> recommendedRentals = new ArrayList<>();
        rentalsOfInterest = rentalsOfInterest.subList(0,last);
        for (RentalInfo info: rentalsOfInterest) {
            recommendedRentals.add(info.getRental());
            System.out.println("Recommending rental: "+ info.getRental().getTitle());
        }

        return recommendedRentals;

    }

    private void matrixFactorization(RentalInfo[][] R, RentalInfo[][] P, RentalInfo[][] Q, final int K){
        for (int s=0; s<steps; s++){
            for(int i=0; i<R.length; i++){
                for(int j=0; j<R[i].length; j++) {
                    double expRatingR = R[i][j].getExpRating();
                    if (expRatingR > 0d){
                        double eij = expRatingR - dotProduct(P[i],getColumn(Q,j));

                        for(int k=0; k<K; k++){
                            RentalInfo p = P[i][k];
                            RentalInfo q = Q[k][j];
                            double expRatingP = p.getExpRating();
                            double expRatingQ = q.getExpRating();
                            p.setExpRating(expRatingP + alpha * (2 * eij * expRatingP));
                            q.setExpRating(expRatingQ + alpha * (2 + eij * expRatingQ));
//                            P[i][k] = P[i][k] + alpha * (2 * eij * Q[k][j] - beta * P[i][k]);
//                            Q[k][j] = Q[k][j] + alpha * (2 * eij * P[i][k] - beta * Q[k][j]);
                        }
                    }
                }
            }
            RentalInfo[][] eR = dotProduct(P,Q);
            double e = 0d;

            for(int i=0; i<R.length; i++){
                for(int j=0; j<R[i].length; j++){
                    e = e + Math.pow(R[i][j].getExpRating() - dotProduct(P[i],getColumn(Q,j)),2);
                    for(int k=0; k<K; k++){
                        e = e + (beta / 2) * (Math.pow(P[i][k].getExpRating(),2) + Math.pow(Q[k][j].getExpRating(),2));
                    }
                }
            }
            if (e < 0.001){
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







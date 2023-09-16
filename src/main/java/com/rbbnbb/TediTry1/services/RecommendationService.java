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

    private Double[][]P;
    private Double[][]Q;

    public RecommendationService(){}

    private class RentalInfo{
        private Rental rental;
        private Double expRating;

        public RentalInfo(Rental rental, Double expRating) {
            this.rental = rental;
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



        Integer[][] R = new Integer[allReviewers.size()][allRentals.size()];

        int userIndex = -1;
        List<Integer> alreadyReviewed = new ArrayList<>();

        for (int i = 0; i < allReviewers.size(); i++) {
           if (allReviewers.get(i).equals(tenant)) userIndex = i;
           for (int j=0; j < allRentals.size(); j++){

               //Get all reviews made by this user on this rental
               List<Review> reviewList = reviewRepository.findByReviewerAndRental(allReviewers.get(i),allRentals.get(j));
               if (reviewList.isEmpty()){
                   R[i][j] = 0; //Average of 1,2,3,4,5]
                   continue;
               }
               if (userIndex > -1) alreadyReviewed.add(j);
               //reviewList is not empty. Sort it by time of review descending, and get the (chronologically) last review.
               reviewList.sort(new Comparator<Review>() {
                   @Override
                   public int compare(Review r1, Review r2) {
                       //r2.compare(r1) for descending order
                       return r2.getIssuedAt().compareTo(r1.getIssuedAt());
                   }
               });
               Review r = reviewList.get(0);
               R[i][j] = r.getStars();
               rentalIndexList.add(new RentalIndex(r.getRental(),j));
           }
        }

        this.P = fill(R.length,K);
        this.Q = fill(K,R[0].length);


        matrixFactorization(R,this.P,this.Q,K);

        Double[][] dR = dotProduct(this.P,this.Q);

        List<RentalIndex> expectedValues = new ArrayList<>();

        for (int j=0; j<dR[userIndex].length; j++){
            if (!alreadyReviewed.contains(j)){
                expectedValues.add(dR[userIndex][j]);
            }
        }

        expectedValues.sort(Comparator.reverseOrder());

        int first = 0;
        int last = (expectedValues.size() > 4) ? 4 : expectedValues.size()-1;




//        for (Double[] doubles : dR) {
//            System.out.print("[");
//            for (Double aDouble : doubles) {
//                System.out.print(aDouble + ", ");
//            }
//            System.out.println("]");
//        }
//
//        for (Integer[] ints : R) {
//            System.out.print("[");
//            for (Integer integer : ints) {
//                System.out.print(integer + ", ");
//            }
//            System.out.println("]");
//        }
    }

    private void matrixFactorization(Integer[][] R, Double[][] P, Double[][] Q, int K){
        for (int s=0; s<steps; s++){
            for(int i=0; i<R.length; i++){
                for(int j=0; j<R[i].length; j++) {
                    if (R[i][j] > 0){
                        double eij = R[i][j] - dotProduct(P[i],getColumn(Q,j));

                        for(int k=0; k<K; k++){
                            P[i][k] = P[i][k] + alpha * (2 * eij * Q[k][j] - beta * P[i][k]);
                            Q[k][j] = Q[k][j] + alpha * (2 * eij * P[i][k] - beta * Q[k][j]);
                        }
                    }
                }
            }
            Double[][] eR = dotProduct(P,Q);
            double e = 0d;

            for(int i=0; i<R.length; i++){
                for(int j=0; j<R[i].length; j++){
                    e = e + Math.pow(R[i][j] - dotProduct(P[i],getColumn(Q,j)),2);
                    for(int k=0; k<K; k++){
                        e = e + (beta / 2) * (Math.pow(P[i][k],2) + Math.pow(Q[k][j],2));
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


    private static Double[][] transpose(Double M[][]){
        int rows = M.length;
        int columns = M[0].length;

        Double[][] T = new Double[columns][rows];

        for (int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                T[j][i] = M[i][j];
            }
        }
        return T;
    }
    private static Double dotProduct(Double[] A, Double[] B)
    {
        double product = 0d;
        final int n = A.length;


        // Loop for calculate dot product
        for (int i = 0; i < n; i++)
            product += A[i] * B[i];
        return product;
    }

    private static Double[][] dotProduct(Double[][] A, Double[][] B){
        Double[][] D = new Double[A.length][B[0].length];
        for (Double[] doubles : D)
            Arrays.fill(doubles, 0d);

        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < B[0].length; j++) {
                for (int k = 0; k < A[0].length; k++) {
                    D[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return D;
    }

    private static Double[] getColumn(Double[][] A, int index){
        Double[] column = new Double[A.length];
        for(int i=0; i<column.length; i++){
            column[i] = A[i][index];
        }
        return column;
    }

    private Double[][] fill(int rows, int columns) {
        Random rClass = new Random();

        Double[][] array = new Double[rows][columns];

        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++) {
                array[i][j] = rClass.nextDouble();
            }
        }
        return array;
    }

    public Double[][] getP() {
        return P;
    }

    public void setP(Double[][] p) {
        P = p;
    }

    public Double[][] getQ() {
        return Q;
    }

    public void setQ(Double[][] q) {
        Q = q;
    }
}







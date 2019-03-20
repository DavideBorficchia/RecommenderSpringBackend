package com.app.recommender.foodrecommender;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface IFoodRecommenderRepository {
    FoodRdf createRdfFood(FoodRdf foodRDF, String userId) throws FileNotFoundException;

    List<FoodRdf> getRdfFoodForRecommendation(String foodId, String userId) throws FileNotFoundException;

    FoodRdf getFoodById(String foodId, String userId) throws IOException;

    FoodRdf[] getAllFoodFromOntology(String userId) throws FileNotFoundException;

    FoodRdf update(FoodRdf foodRDF, String foodId, String userId) throws FileNotFoundException;


//    FoodRdf createRdfFood(FoodRdf foodRDF) throws FileNotFoundException;
//
//
//    List<FoodRdf> getRdfFoodForRecommendation(String name) throws FileNotFoundException;
//
//    FoodRdf getFoodById(String foodName) throws IOException;
//
//    FoodRdf[] getAllFoodFromOntology() throws FileNotFoundException;
//
//    FoodRdf update(FoodRdf foodRDF, String previousName) throws FoodRdfNotFoundException, FileNotFoundException;
}

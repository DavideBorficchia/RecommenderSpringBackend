package com.app.recommender.diet;

import com.app.recommender.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@CrossOrigin
@RestController
public class DietController {

    @Autowired
    private DietService dietService;

    @GetMapping(value = "/random")
    public int getRandomInt() {
        return new Random().nextInt();
    }

//    @GetMapping(value = "/food/{name}")
//    public ResponseEntity getFood(@PathVariable String name) {
//
////        RestTemplate restTemplate = new RestTemplate();
//
//        String foodToLookFor = name.trim();
//        System.out.println(foodToLookFor);
//
//        return ResponseEntity.status(200).body(foodToLookFor);
//    }
//
//    @GetMapping(value = "/food")
//    public ResponseEntity getAllFood() {
//
//
//        return ResponseEntity.status(200).body(Arrays.asList("pasta", "pizza", "insalata"));
//    }


    @PutMapping(value = "/{dietName}/days/{day}/meals/{mealType}")
    public ResponseEntity updateDietMeals(@PathVariable String dietName,
                                          @PathVariable String mealType,
                                          @PathVariable String day,
                                          @RequestBody Food food,
                                          @RequestParam(value = "userId") String userId) {
        Diet diet;
        try {
            diet = this.dietService.getDietByDietName(dietName, userId);
            Map<String, List<Meal>> foodEntries = diet.getDailyFood();

            List<Meal> meals = foodEntries.get(day);
            Optional<Meal> m = meals.stream().filter(mealToCheck -> mealToCheck.getMealType().equals(mealType)).findAny();
            if (m.isPresent()) {
                Meal mealToUpdate = m.get();

                meals.remove(mealToUpdate);

                mealToUpdate.getAllFoodEntries().add(food);

                meals.add(mealToUpdate);

                foodEntries.put(day, meals);

                diet.setDailyFood(foodEntries);

                diet.updateCalories(day);

                dietService.updateDiet(diet);
                return ResponseEntity.status(201).body(mealToUpdate)
                        ;

            }
            return ResponseEntity.status(403).body(diet);
        } catch (DietNotFoundException | NoDietHistoryException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }

    }

    @DeleteMapping(value = "/{dietName}/days/{day}/meals/{mealType}")
    public ResponseEntity deleteFoodFromMeals(@PathVariable String dietName,
                                              @PathVariable String mealType,
                                              @PathVariable String day,
                                              @RequestParam(value = "userId") String userId,
                                              @RequestParam(value = "foodName") String foodName) {
        Diet diet;
        try {
            diet = this.dietService.getDietByDietName(dietName, userId);
            Map<String, List<Meal>> foodEntries = diet.getDailyFood();

            List<Meal> meals = foodEntries.get(day);
            Optional<Meal> m = meals.stream().filter(mealToCheck -> mealToCheck.getMealType().equals(mealType)).findAny();
            if (m.isPresent()) {
                Meal mealToUpdate = m.get();

                meals.remove(mealToUpdate);

                Optional<Food> foodToRemove = mealToUpdate.getAllFoodEntries().stream().filter(f -> f.getName().equals(foodName)).findAny();
                if (foodToRemove.isPresent()) {

                    mealToUpdate.getAllFoodEntries().remove(foodToRemove.get());

                    meals.add(mealToUpdate);

                    foodEntries.put(day, meals);

                    diet.setDailyFood(foodEntries);

                    diet.updateCalories(day);

                    dietService.updateDiet(diet);
                    return ResponseEntity.status(201).body(foodToRemove.get());

                }
            }
            return ResponseEntity.status(403).body(diet);
        } catch (DietNotFoundException | NoDietHistoryException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }

    }

    @PutMapping(value = "/{dietName}/days/{day}/meals/{mealType}/{foodName}")
    public ResponseEntity updateFoodQuantityAndCalories(@PathVariable String dietName,
                                                        @PathVariable String day,
                                                        @PathVariable String mealType,
                                                        @PathVariable String foodName,
                                                        @RequestBody Food food,
                                                        @RequestParam(value = "userId") String userId) {
        Diet diet;
//        String foodName = "mrfdda";
        try {
            diet = this.dietService.getDietByDietName(dietName, userId);
            Map<String, List<Meal>> foodEntries = diet.getDailyFood();

            List<Meal> meals = foodEntries.get(day);
            Optional<Meal> m = meals.stream().filter(mealToCheck -> mealToCheck.getMealType().equals(mealType)).findAny();
            if (m.isPresent()) {
                Meal mealToUpdate = m.get();
                meals.remove(mealToUpdate);
                Optional<Food> optionalFood = mealToUpdate.getAllFoodEntries().stream().filter(f -> f.getName().equals(foodName)).findAny();
                if (optionalFood.isPresent()) {
                    Food foodToUpdate = optionalFood.get();
                    mealToUpdate.getAllFoodEntries().remove(foodToUpdate);
                    foodToUpdate = food;
                    mealToUpdate.getAllFoodEntries().add(foodToUpdate);
                } else {
                    ResponseEntity.status(400).body("Bad request: no food with name " + foodName + " was found in day " + day + " for diet " + dietName);
                }
                meals.add(mealToUpdate);
                foodEntries.put(day, meals);
                diet.setDailyFood(foodEntries);
                diet.updateCalories(day);
                Diet d = this.dietService.updateDiet(diet);
                mealToUpdate.getAllFoodEntries().forEach(f -> System.out.println(f.getCalories()));

            }
        } catch (NoDietHistoryException | DietNotFoundException e) {
            ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.status(201).body(food);
    }

    @PostMapping(value = "/create")
    public ResponseEntity createDiet(@RequestBody Diet diet) {
        try {

            this.dietService.createNewDiet(diet);
        } catch (DietAlreadyExistException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
        return ResponseEntity.status(201).body(diet);
    }

    @GetMapping(value = "/current")
    public ResponseEntity getDiet(@RequestParam String userId) {
        Diet diet;
        try {
            diet = this.dietService.getCurrentDietByUserId(userId);
        } catch (NoDietHistoryException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }

        return ResponseEntity.status(200).body(diet);
    }

    @GetMapping(value = "/{dietName}")
    public ResponseEntity getDietByName(@PathVariable String dietName,
                                        @RequestParam String userId) {
        Diet diet;
        try {
            diet = this.dietService.getDietByDietName(dietName, userId);

        } catch (NoDietHistoryException | DietNotFoundException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.status(200).body(diet);
    }

    @GetMapping(value = "/years/{year}/months/{monthName}")
    public ResponseEntity getAllDietByName(@RequestParam String userId, @PathVariable String year, @PathVariable String monthName) {
        try {
            return ResponseEntity.status(200).body(this.dietService.getRecentDiets(monthName, userId, year));
        } catch (DietNotFoundException | NoDietHistoryException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping(value = "/years/{year}")
    public ResponseEntity getAllDietByYear(@RequestParam String userId, @PathVariable String year) {

        List<DietHistory> dietHistories;
        try {
            dietHistories = this.dietService.getDietsByYear(userId, year);
        } catch (DietNotFoundException | NoDietHistoryException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }


        return ResponseEntity.status(200).body(dietHistories);

    }
}

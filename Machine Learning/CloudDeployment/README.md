# Flask API for Machine Learning Model

This Flask API provides endpoints for a personalized food recommendation system and food recognition. It uses TensorFlow for image prediction and integrates with Firebase Firestore for data storage.

## Endpoints

- `/` (GET): Home endpoint to check if the API is running.
- `/food` (POST, GET): Endpoint for food prediction. Accepts image files and returns a prediction.
- `/recommendation` (GET): Endpoint for getting personalized food recommendations based on user profile and date.
- `/regisuid` (GET): Endpoint for registering a user ID.
- `/getquestionanswer` (GET): Endpoint for retrieving user question asnwered or not.
- `/updatequestion` (GET): Endpoint for updating user question and answer data.
- `/gettingnutrition` (GET): Endpoint for retrieving user nutrition data.
- `/updatefoodday` (GET): Endpoint for updating user food consumption data.
- `/getuserdata` (GET): Endpoint for retrieving user profile data.
- `/updateeaten` (GET): Endpoint for updating user food consumption data for a specific meal from food recommendation.
- `/catering` (GET): Endpoint for getting catering information for a user.
- `/nutritioncalculation` (GET): Endpoint for calculating nutrition data for a specific day.

## Usage

- Make a POST request to `/food` with an image file to predict the food category in the image.
- Make a GET request to `/recommendation` with parameters `id`, `day`, and `month` to get personalized food recommendations.
- Make GET or POST requests to other endpoints as per the API documentation.
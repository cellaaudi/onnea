# Food Recommendation System

This repository contains scripts for building a recommendation system that provides personalized food recommendations to users based on their preferences. The system takes into account both user characteristics (such as age, gender, and weight) and food attributes (such as calories, protein, and fat).

## Description

The recommendation model is trained using a neural network approach. The model architecture includes separate sub-networks for users and items (foods) which transform the raw feature inputs into a shared latent space. The similarity between a user and an item is then computed as the dot product between their latent vectors.

The model training and evaluation process is encapsulated within a Jupyter notebook, where the key steps are:

1. Load and preprocess user and food data.
2. Duplicate rows in user data based on number of non-zero values in the rating columns.
3. Split user and food data into training and test sets.
4. Normalize user and food data using the StandardScaler and MinMaxScaler from the Scikit-learn library.
5. Define the architecture of the neural network model.
6. Train the model and evaluate its performance.
7. Save the trained model and scaler objects for later use in prediction.
8. Use the model to predict the ratings for new user-food pairs.

Another script is provided to upload user and food data to Firestore, a flexible, scalable NoSQL cloud database.

## Prerequisites

Before you begin, ensure you have met the following requirements:

1. You have a `<Windows/Linux/Mac>` machine with Python 3.7 or later installed.
2. You have installed the necessary Python packages listed in `requirements.txt`.

## Installation and Usage

* Clone the repository.
* Install the necessary Python packages with `pip install -r requirements.txt`.
* Run the Jupyter notebook to train and evaluate the model.
* Run the Firestore script to upload data to Firestore database.
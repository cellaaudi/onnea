# Onnea Bangkit Capstone C23-PS390

## Introduction
Onnea is a user-centric application designed to support a healthier lifestyle. It offers three primary features and one secondary feature:

- **Nutrition Tracking**: Tracks daily caloric, protein, fat, and carbohydrate intake while calculating individual nutritional needs.
- **Food Recommendation**: Provides personalized food suggestions based on users' dietary history and preferences using machine learning.
- **Catering Services**: Functions as a marketplace for various catering services, facilitating easy discovery and selection.
- **Food Recognition**: Allows users to photograph their meals, which the app then identifies, providing the dish's name and associated nutritional information using machine learning.

## Team
- (ML) M351DKX3937 – Celvin – Universitas Surabaya
- (ML) M351DKY4382 – Yolenta Ladeta Noventa – Universitas Surabaya
- (CC) C125DKX4753 – Riyo Agung Wicaksono – Universitas Islam Negeri Syarif Hidayatullah Jakarta
- (CC) C288DSX0695 – Astam Mulyana – Universitas Negeri Yogyakarta
- (MD) A197DKX4765 – Cahyo Ramadhan Ridhollah Santosa – Universitas Islam Negeri Sunan Ampel Surabaya
- (MD) A351DKY4270 – Marcella Audi Susanto – Universitas Surabaya

## User Guide
1. Download and install the Onnea app on your Android device.
2. Open the app and register a new account.
3. Log into your account.
4. Answer the preliminary questionnaire to calculate your daily nutritional needs.
5. Begin tracking your caloric, protein, fat, and carbohydrate intake.
6. Explore personalized food recommendations.
7. Browse various catering services and make your selection.
8. Utilize the Food Recognition feature to identify your meal and receive nutritional information.
9. Enjoy a healthier lifestyle!

# Step-by-Step Replication Guide
## 1. Problem Identification
Our mission is to address the widespread lack of awareness regarding healthy lifestyles. Our solution is designed to equip individuals with a convenient tool for tracking nutritional intake, receiving personalized food recommendations, and discovering diverse catering options.

## 2. Research
Thorough market research and user surveys were conducted to understand the gaps in the current health and lifestyle app market, leading to the conceptualization of Onnea.

## 3. Proposed Solution
Our mobile app, Onnea, offers several unique features aimed at promoting healthier lifestyles. These features include nutrition tracking, personalized food recommendations using machine learning, a catering services marketplace, and a food recognition tool that uses machine learning to identify dishes and provide nutritional information.

## 4. Data Collection
We used the Food-101 dataset for training our machine learning model. Additionally, we collected survey data from 77 respondents to gain insights into individuals' eating habits and preferences.

## 5. Data Preprocessing
Data preprocessing was performed using Scikit-learn, Pandas, and Numpy. This step involved cleaning the data and converting it into a format suitable for machine learning models.

## 6. Machine Learning Model
We employed TensorFlow and Keras to build our machine learning model. The model was trained using transfer learning to leverage pre-existing knowledge for better accuracy and performance, and the recommendation model was trained using a content-based filtering approach.

## 7. Deployment
Our machine learning models were deployed to Google Cloud Run, Cloud Build, and Firestore for the database, making them accessible for real-time predictions.

## 8. API
We developed an API using Flask to facilitate communication between our machine learning model and our Android application.

## 9. Mobile App
The mobile app was developed using Kotlin, designed to be user-friendly and intuitive, offering a seamless user experience.

# Resources
## Dataset
- [Food-101](http://data.vision.ee.ethz.ch/cvl/food-101.tar.gz)
- [Survey Data]

## Machine Learning
- TensorFlow
- Keras
- Scikit-learn
- Pandas
- Numpy

## Cloud Computing
- Google Cloud Run
- Google Cloud Build
- Google Cloud Firestore
- Flask
- Firebase

## Mobile Development
- Kotlin
- Retrofit
- Figma
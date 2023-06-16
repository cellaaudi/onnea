import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'

import io
import tensorflow as tf
from tensorflow import keras
import numpy as np
from PIL import Image
from flask import Flask, request, jsonify
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import requests
import random
import pickle
import json

cred = credentials.Certificate('onnea-apps-c8771929abfc.json')  
firebase_admin.initialize_app(cred)

db = firestore.client()

def dayFood(id, day):
    
    doc_ref = db.collection('users').document(str(id))
    doc = doc_ref.get()
    API_KEY = '1983de8b13b4480a9ff3a126460c1bef'

    if doc.exists:
        data = doc.to_dict()
        gender = data.get('Gender')
        age = data.get('Age')
        height = data.get('Height')
        weight = data.get('Weight')
        activity_level = data.get('Activity_Level')
        goals = data.get('Goals')
        fruit_level = data.get('Fruit_Level')
        healthy_food_level = data.get('Healthy_Food_Level')
        sweetness = data.get('Sweetness')
        saltiness = data.get('Saltiness')
        sourness = data.get('Sourness')
        bitterness = data.get('Bitterness')
        savoriness = data.get('Savoriness')
        fattiness = data.get('Fattiness')
        spiciness = data.get('Spiciness')
        calories = data.get('Calories')
        protein = data.get('Protein')
        fat = data.get('Fat')
        carbohydrates = data.get('Carbohydrates')

        gender_male = 1 if gender == 'Male' else 0
        gender_female = 1 if gender == 'Female' else 0

        not_active = 1 if activity_level == 'Not active' else 0
        light_active = 1 if activity_level == 'Light activity' else 0
        active = 1 if activity_level == 'Active' else 0
        moderate_active = 1 if activity_level == 'Moderate active' else 0
        very_active = 1 if activity_level == 'Very active' else 0

        min_protein_breakfast = float(protein)*0.2 - float(protein)*0.2*0.5
        max_protein_breakfast = float(protein)*0.2 + float(protein)*0.2*0.5
        min_carbs_breakfast = float(carbohydrates)*0.2 - float(carbohydrates)*0.2*0.5
        max_carbs_breakfast = float(carbohydrates)*0.2 + float(carbohydrates)*0.2*0.5
        min_fat_breakfast = float(fat)*0.2 - float(fat)*0.2*0.5
        max_fat_breakfast = float(fat)*0.2 + float(fat)*0.2*0.5

        min_protein_maincourse = float(protein)*0.4 - float(protein)*0.4*0.5
        max_protein_maincourse = float(protein)*0.4 + float(protein)*0.4*0.5
        min_carbs_maincourse = float(carbohydrates)*0.4 - float(carbohydrates)*0.4*0.5
        max_carbs_maincourse = float(carbohydrates)*0.4 + float(carbohydrates)*0.4*0.5
        min_fat_maincourse = float(fat)*0.4 - float(fat)*0.4*0.5
        max_fat_maincourse = float(fat)*0.4 + float(fat)*0.4*0.5

        breakfast_id = []
        maincourse_id = []

        type = "breakfast"
        proMin = int(min_protein_breakfast)
        proMax = int(max_protein_breakfast)
        carMin = int(min_carbs_breakfast)
        carMax = int(max_carbs_breakfast)
        fatMin = int(min_fat_breakfast)
        fatMax = int(max_fat_breakfast)
        offset = int(day)*5 - 5
        url = f'https://api.spoonacular.com/recipes/complexSearch?apiKey={API_KEY}&type={type}&minProtein={proMin}&maxProtein={proMax}&minCarbs={carMin}&minFat={fatMin}&maxFat={fatMax}&maxCarbs={carMax}&number=5&offset={offset}'
        response = requests.get(url)

        if response.status_code == 200:
            data = response.json()
            recipes = data["results"]
            
            for recipe in recipes:
                recipe_id = recipe["id"]
                breakfast_id.append(recipe_id)
        else:
            breakfasts_ref = db.collection('food_Breakfast')

            breakfasts_docs = breakfasts_ref.get()

            docss_ids = [doc.id for doc in breakfasts_docs]

            for i in range(5):
                random_docss_id = random.choice(docss_ids)

                breakfast_id.append(random_docss_id)
            print("Permintaan gagal. Kode status:", response.status_code)

        print(breakfast_id)
        
        type = "main course"
        proMin = int(min_protein_maincourse)
        proMax = int(max_protein_maincourse)
        carMin = int(min_carbs_maincourse)
        carMax = int(max_carbs_maincourse)
        fatMin = int(min_fat_maincourse)
        fatMax = int(max_fat_maincourse)
        offset = int(day)*10 - 10
        url = f'https://api.spoonacular.com/recipes/complexSearch?apiKey={API_KEY}&type={type}&minProtein={proMin}&maxProtein={proMax}&minCarbs={carMin}&minFat={fatMin}&maxFat={fatMax}&maxCarbs={carMax}&number=10&offset={offset}'

        response = requests.get(url)
        if response.status_code == 200:
            data = response.json()
            recipes = data["results"]
            
            for recipe in recipes:
                recipe_id = recipe["id"]
                maincourse_id.append(recipe_id)
        else:
            maincourses_ref = db.collection('food_Main_course')

            maincourses_docs = maincourses_ref.get()

            docs_ids = [doc.id for doc in maincourses_docs]

            for i in range(10):
                random_docs_id = random.choice(docs_ids)

                maincourse_id.append(random_docs_id)
            print("Permintaan gagal. Kode status:", response.status_code)

        while(len(breakfast_id) < 5):
            breakfastss_ref = db.collection('food_Breakfast')

            breakfastss_docs = breakfastss_ref.get()

            docsss_ids = [doc.id for doc in breakfastss_docs]

            
            random_docsss_id = random.choice(docsss_ids)

            breakfast_id.append(random_docsss_id)

        while(len(maincourse_id) < 10):
            maincoursesss_ref = db.collection('food_Main_course')

            maincoursesss_docs = maincoursesss_ref.get()

            docssss_ids = [doc.id for doc in maincoursesss_docs]

            
            random_docssss_id = random.choice(docssss_ids)

            maincourse_id.append(random_docssss_id)

        print(maincourse_id)

        id_new_breakfast = []
        id_new_maincourse = []

        for ids in breakfast_id:
            doc_ref = db.collection('food_Breakfast').document(str(ids))
            doc = doc_ref.get()

            if not doc.exists:
                id_new_breakfast.append(ids)

        for ids in maincourse_id:
            doc_ref = db.collection('food_Main_course').document(str(ids))
            doc = doc_ref.get()

            if not doc.exists:
                id_new_maincourse.append(ids)

        print("ID yang belum ada di dokumen:")
        print(id_new_maincourse, id_new_breakfast)


        def get_taste_data(api_key, recipe_id):
            url = f"https://api.spoonacular.com/recipes/{recipe_id}/tasteWidget.json?apiKey={api_key}&normalize=True"
            response = requests.get(url)
            if response.status_code == 200:
                data = response.json()
                return data
            else:
                return None

        def get_nutrition_data(api_key, recipe_id):
            url = f"https://api.spoonacular.com/recipes/{recipe_id}/nutritionWidget.json?apiKey={api_key}"
            response = requests.get(url)
            if response.status_code == 200:
                data = response.json()
                return data.get("nutrients", [])
            else:
                return None
            
        def get_name(api_key, recipe_id):
            url = f"https://api.spoonacular.com/recipes/{recipe_id}/information?apiKey={api_key}"
            response = requests.get(url)
            if response.status_code == 200:
                data = response.json()
                return data
            else:
                return None

        for id_new in id_new_breakfast:
            taste = get_taste_data(API_KEY, id_new)
            if taste is not None:
                doc_ref = db.collection('food_Breakfast').document(str(id_new))
                doc_ref.set({"ID":str(id_new), "Type":"Breakfast"})
                doc_ref.update({"Sweetness":str(taste["sweetness"]), "Saltiness":str(taste["saltiness"]), "Sourness":str(taste["sourness"]), "Bitterness":str(taste["bitterness"]), "Savoriness":str(taste["savoriness"]), "Fattiness":str(taste["fattiness"]), "Spiciness":str(taste["spiciness"])})
            name = get_name(API_KEY, id_new)
            if name is not None:
                doc_ref = db.collection('food_Breakfast').document(str(id_new))
                doc_ref.update({"Name":str(name["title"]), "Link":str(name["image"]), "Image_Type": str(name["imageType"])})
            
            nutrition = get_nutrition_data(API_KEY, id_new)
            if nutrition is not None:
                doc_ref = db.collection('food_Breakfast').document(str(id_new))
                doc_ref.update({"Calories":str(nutrition[0]["amount"]), "Fat":str(nutrition[1]["amount"]), "Saturated_Fat":str(nutrition[2]["amount"]), "Carbohydrates":str(nutrition[3]["amount"]), "Net_Carbohydrates":str(nutrition[4]["amount"]), "Sugar":str(nutrition[5]["amount"]), "Cholesterol":str(nutrition[6]["amount"]), "Sodium":str(nutrition[7]["amount"]), "Protein":str(nutrition[8]["amount"])})
            if nutrition is None and taste is not None:
                doc_ref = db.collection('food_Breakfast').document(str(id_new))
                doc_ref.delete()

        for id_new in id_new_maincourse:
            taste = get_taste_data(API_KEY, id_new)
            if taste is not None:
                doc_ref = db.collection('food_Main_course').document(str(id_new))
                doc_ref.set({"ID":str(id_new), "Type":"Main course"})
                doc_ref.update({"Sweetness":str(taste["sweetness"]), "Saltiness":str(taste["saltiness"]), "Sourness":str(taste["sourness"]), "Bitterness":str(taste["bitterness"]), "Savoriness":str(taste["savoriness"]), "Fattiness":str(taste["fattiness"]), "Spiciness":str(taste["spiciness"])})
            name = get_name(API_KEY, id_new)
            if name is not None:
                doc_ref = db.collection('food_Main_course').document(str(id_new))
                doc_ref.update({"Name":str(name["title"]), "Link":str(name["image"]), "Image_Type": str(name["imageType"])})
            nutrition = get_nutrition_data(API_KEY, id_new)
            if nutrition is not None:
                doc_ref = db.collection('food_Main_course').document(str(id_new))
                doc_ref.update({"Calories":str(nutrition[0]["amount"]), "Fat":str(nutrition[1]["amount"]), "Saturated_Fat":str(nutrition[2]["amount"]), "Carbohydrates":str(nutrition[3]["amount"]), "Net_Carbohydrates":str(nutrition[4]["amount"]), "Sugar":str(nutrition[5]["amount"]), "Cholesterol":str(nutrition[6]["amount"]), "Sodium":str(nutrition[7]["amount"]), "Protein":str(nutrition[8]["amount"])})
            if nutrition is None and taste is not None:
                doc_ref = db.collection('food_Main_course').document(str(id_new))
                doc_ref.delete()

        id_breakfast_predict = []
        id_maincourse_predict = []
        
        for ids in breakfast_id:
            doc_ref = db.collection('food_Breakfast').document(str(ids))
            doc = doc_ref.get()

            if not doc.exists:
                breakfast_ref = db.collection('food_Breakfast')

                breakfast_docs = breakfast_ref.get()

                doc_ids = [doc.id for doc in breakfast_docs]

                random_doc_id = random.choice(doc_ids)

                id_breakfast_predict.append(random_doc_id)
            else:
                id_breakfast_predict.append(ids)

        for ids in maincourse_id:
            doc_ref = db.collection('food_Main_course').document(str(ids))
            doc = doc_ref.get()

            if not doc.exists:
                maincourse_ref = db.collection('food_Main_course')

                maincourse_docs = maincourse_ref.get()

                doc_ids = [doc.id for doc in maincourse_docs]

                random_doc_id = random.choice(doc_ids)

                id_maincourse_predict.append(random_doc_id)
            else:
                id_maincourse_predict.append(ids)
            
        
        #####################

        pred_break = []
        pred_main = []

        for ids in id_breakfast_predict:
            doc_ref = db.collection('food_Breakfast').document(str(ids))
            doc = doc_ref.get()
            datas = doc.to_dict()
            sweetness_food = datas.get("Sweetness")
            saltiness_food = datas.get("Saltiness")
            sourness_food = datas.get("Sourness")
            bitterness_food = datas.get("Bitterness")
            savoriness_food = datas.get("Savoriness")
            fattiness_food = datas.get("Fattiness")
            spiciness_food = datas.get("Spiciness")
            calories_food = datas.get("Calories")
            fat_food = datas.get("Fat")
            saturated_fat_food = datas.get("Saturated_Fat")
            carbohydrates_food = datas.get("Carbohydrates")
            net_carbohydrates_food = datas.get("Net_Carbohydrates")
            sugar_food = datas.get("Sugar")
            cholesterol_food = datas.get("Cholesterol")
            sodium_food = datas.get("Sodium")
            protein_food = datas.get("Protein")
            
            user_pred = np.array([int(age), int(height), int(weight), int(fruit_level), int(healthy_food_level), float(sweetness), float(saltiness), float(sourness), float(bitterness), float(savoriness), float(fattiness), float(spiciness), int(gender_female), int(gender_male), int(active), int(light_active), int(moderate_active), int(not_active), int(very_active)]).reshape(1, 19)

            item_pred = np.array([float(sweetness_food), float(saltiness_food), float(sourness_food), float(bitterness_food), float(savoriness_food), float(fattiness_food), float(spiciness_food), float(calories_food), float(fat_food), float(saturated_fat_food), float(carbohydrates_food), float(net_carbohydrates_food), float(sugar_food), float(cholesterol_food), float(sodium_food), float(protein_food)]).reshape(1, 16)
        
            model = tf.keras.models.load_model('recommendation_model.h5')

            with open('scalerItem.pkl', 'rb') as f:
                scaleritem = pickle.load(f)
            
            with open('scalerUser.pkl', 'rb') as f:
                scaleruser = pickle.load(f)
            
            user_pred = scaleruser.transform(user_pred)
            item_pred = scaleritem.transform(item_pred)

            pred_break.append(model.predict([user_pred, item_pred]))

        for ids in id_maincourse_predict:
            doc_ref = db.collection('food_Main_course').document(str(ids))
            doc = doc_ref.get()
            datas = doc.to_dict()
            sweetness_food = datas.get("Sweetness")
            saltiness_food = datas.get("Saltiness")
            sourness_food = datas.get("Sourness")
            bitterness_food = datas.get("Bitterness")
            savoriness_food = datas.get("Savoriness")
            fattiness_food = datas.get("Fattiness")
            spiciness_food = datas.get("Spiciness")
            calories_food = datas.get("Calories")
            fat_food = datas.get("Fat")
            saturated_fat_food = datas.get("Saturated_Fat")
            carbohydrates_food = datas.get("Carbohydrates")
            net_carbohydrates_food = datas.get("Net_Carbohydrates")
            sugar_food = datas.get("Sugar")
            cholesterol_food = datas.get("Cholesterol")
            sodium_food = datas.get("Sodium")
            protein_food = datas.get("Protein")
            user_pred = np.array([int(age), int(height), int(weight), int(fruit_level), int(healthy_food_level), float(sweetness), float(saltiness), float(sourness), float(bitterness), float(savoriness), float(fattiness), float(spiciness), int(gender_female), int(gender_male), int(active), int(light_active), int(moderate_active), int(not_active), int(very_active)]).reshape(1, 19)

            item_pred = np.array([float(sweetness_food), float(saltiness_food), float(sourness_food), float(bitterness_food), float(savoriness_food), float(fattiness_food), float(spiciness_food), float(calories_food), float(fat_food), float(saturated_fat_food), float(carbohydrates_food), float(net_carbohydrates_food), float(sugar_food), float(cholesterol_food), float(sodium_food), float(protein_food)]).reshape(1, 16)
        
            model = tf.keras.models.load_model('recommendation_model.h5')

            with open('scalerItem.pkl', 'rb') as f:
                scaleritem = pickle.load(f)
            
            with open('scalerUser.pkl', 'rb') as f:
                scaleruser = pickle.load(f)
            
            user_pred = scaleruser.transform(user_pred)
            item_pred = scaleritem.transform(item_pred)

            pred_main.append(model.predict([user_pred, item_pred]))

        index_pred_break = list(enumerate(pred_break))
        index_pred_main = list(enumerate(pred_main))
        sorted_pred_break = sorted(index_pred_break, key=lambda i: i[1], reverse=True)
        sorted_pred_main = sorted(index_pred_main, key=lambda i: i[1], reverse=True)
        sorted_break = [index for index, value in sorted_pred_break]
        sorted_main = [index for index, value in sorted_pred_main]
        
        sorted_lunch = sorted_main[0]
        sorted_dinner = sorted_main[1]
        sorted_break = sorted_break[0]

        ordered_breakfast = id_breakfast_predict[sorted_break]
        ordered_lunch = id_maincourse_predict[sorted_lunch]
        ordered_dinner = id_maincourse_predict[sorted_dinner]

        break_image = db.collection('food_Breakfast').document(str(ordered_breakfast)).get().to_dict().get('Link')
        break_image_type = db.collection('food_Breakfast').document(str(ordered_breakfast)).get().to_dict().get('Image_Type')
        break_name = db.collection('food_Breakfast').document(str(ordered_breakfast)).get().to_dict().get('Name')
        break_calories = db.collection('food_Breakfast').document(str(ordered_breakfast)).get().to_dict().get('Calories')
        break_protein = db.collection('food_Breakfast').document(str(ordered_breakfast)).get().to_dict().get('Protein')
        break_fat = db.collection('food_Breakfast').document(str(ordered_breakfast)).get().to_dict().get('Fat')
        break_carbohydrates = db.collection('food_Breakfast').document(str(ordered_breakfast)).get().to_dict().get('Carbohydrates')
        break_eat = False

        lunch_image = db.collection('food_Main_course').document(str(ordered_lunch)).get().to_dict().get('Link')
        lunch_image_type = db.collection('food_Main_course').document(str(ordered_lunch)).get().to_dict().get('Image_Type')
        lunch_name = db.collection('food_Main_course').document(str(ordered_lunch)).get().to_dict().get('Name')
        lunch_calories = db.collection('food_Main_course').document(str(ordered_lunch)).get().to_dict().get('Calories')
        lunch_protein = db.collection('food_Main_course').document(str(ordered_lunch)).get().to_dict().get('Protein')
        lunch_fat = db.collection('food_Main_course').document(str(ordered_lunch)).get().to_dict().get('Fat')
        lunch_carbohydrates = db.collection('food_Main_course').document(str(ordered_lunch)).get().to_dict().get('Carbohydrates')
        lunch_eat = False

        dinner_image = db.collection('food_Main_course').document(str(ordered_dinner)).get().to_dict().get('Link')
        dinner_image_type = db.collection('food_Main_course').document(str(ordered_dinner)).get().to_dict().get('Image_Type')
        dinner_name = db.collection('food_Main_course').document(str(ordered_dinner)).get().to_dict().get('Name')
        dinner_calories = db.collection('food_Main_course').document(str(ordered_dinner)).get().to_dict().get('Calories')
        dinner_protein = db.collection('food_Main_course').document(str(ordered_dinner)).get().to_dict().get('Protein')
        dinner_fat = db.collection('food_Main_course').document(str(ordered_dinner)).get().to_dict().get('Fat')
        dinner_carbohydrates = db.collection('food_Main_course').document(str(ordered_dinner)).get().to_dict().get('Carbohydrates')
        dinner_eat = False



        output = {
            "Breakfast": [{"ID": ordered_breakfast}, {"Name": break_name}, {"Link": break_image}, {"Image_Type": break_image_type}, {"Calories": break_calories}, {"Protein": break_protein}, {"Fat": break_fat}, {"Carbohydrates": break_carbohydrates}, {"Eat": break_eat}],
            "Lunch": [{"ID": ordered_lunch}, {"Name": lunch_name}, {"Link": lunch_image}, {"Image_Type": lunch_image_type}, {"Calories": lunch_calories}, {"Protein": lunch_protein}, {"Fat": lunch_fat}, {"Carbohydrates": lunch_carbohydrates}, {"Eat": lunch_eat}],
            "Dinner": [{"ID": ordered_dinner}, {"Name": dinner_name}, {"Link": dinner_image}, {"Image_Type": dinner_image_type}, {"Calories": dinner_calories}, {"Protein": dinner_protein}, {"Fat": dinner_fat}, {"Carbohydrates": dinner_carbohydrates}, {"Eat": dinner_eat}]
        }

        print(json.dumps(output,indent=4))
        
        return output
    else:
        return {"message": "Data not found"}
    
def setuid(id):
    

    doc_ref = db.collection('users').document(str(id))
    doc_ref.set({"ID":str(id)})
    
    doc_ref = db.collection('users_Recommendation').document(str(id))
    doc_ref.set({"ID":str(id)})


    return {"message": True}

def getrecommendation(id, day, month):
    if getquestion(id) == False:
        return "Data belum lengkap"
    doc_Ref = db.collection('users_Recommendation').document(str(id)).collection('day').document(str(day))
    doc = doc_Ref.get()
    if not doc.exists:
        doc_Ref.set({"Day":str(day)})
    doc = doc_Ref.get()
    data = doc.to_dict()
    months = data.get('Month')
    if months is None or months != str(month):
        data_food = dayFood(id, day)
        doc_Ref.update({"Month":str(month)})
        doc_Ref.update(data_food)

    data_day = doc_Ref.get().to_dict()

    return data_day

def getquestion(id):

    doc_ref = db.collection('users').document(str(id))
    if doc_ref.get().exists:

        doc = doc_ref.get().to_dict().get('Name')
        if(doc == None):
            return {"message": False}
        else:
            return {"message": True}
    else:

        return {"message": False}

    
def postquestion(id, activity, age, fruit, healthy, height, weight, name, gender, goals):

    if gender == 'Male':
        bmr = 66.5 + (13.75 * int(weight)) + (5.003 * int(height)) - (6.75 * int(age))
    elif gender == 'Female':
        bmr = 655.1 + (9.563 * int(weight)) + (1.85 * int(height)) - (4.676 * int(age))

    if goals == 'Losing Weight':
        calorie_intake = bmr - (0.15 * bmr)
    elif goals == 'Maintaining Weight':
        calorie_intake = bmr
    elif goals == 'Gaining Weight':
        calorie_intake = bmr + (0.15 * bmr)
    else:
        calorie_intake = bmr

    if activity == 'Not active':
        calorie_intake = calorie_intake * 1.2
    elif activity == 'Light activity':
        calorie_intake = calorie_intake * 1.375
    elif activity == 'Moderate active':
        calorie_intake = calorie_intake * 1.55
    elif activity == 'Active':
        calorie_intake = calorie_intake * 1.725
    elif activity == 'Very active':
        calorie_intake = calorie_intake * 1.9

    protein_percentage = 0.2
    fat_percentage = 0.3
    carb_percentage = 0.5
    protein_intake = protein_percentage * calorie_intake / 4
    fat_intake = fat_percentage * calorie_intake / 9
    carb_intake = carb_percentage * calorie_intake / 4


    doc_ref = db.collection('users').document(str(id))
    doc_ref.update({"Activity_Level":str(activity),
                   "Age":str(age),
                   "Fruit_Level":str(fruit),
                   "Healthy_Food_Level":str(healthy),
                   "Height":str(height),
                   "Weight":str(weight),
                   "Name":str(name),
                   "Gender": str(gender),
                   "Goals": str(goals)})
    doc_ref.update({"Calories":str(calorie_intake),
                   "Protein":str(protein_intake),
                   "Fat":str(fat_intake),
                   "Carbohydrates":str(carb_intake)})
    doc_ref.update({"Sweetness":str(1.5),
                   "Saltiness":str(3),
                   "Sourness":str(1),
                   "Bitterness":str(1),
                   "Savoriness":str(2),
                   "Fattiness":str(3),
                   "Spiciness":str(0)})
    

    return {"message": True}

def getnutrition(id):

    doc_ref = db.collection('users').document(str(id))
    calories = doc_ref.get().to_dict().get('Calories')
    protein = doc_ref.get().to_dict().get('Protein')
    fat = doc_ref.get().to_dict().get('Fat')
    carbohydrates = doc_ref.get().to_dict().get('Carbohydrates')


    output = {"Calories": calories, "Protein": protein, "Fat": fat, "Carbohydrates": carbohydrates}

    return output

def updatefood(uid, id, day, month, name, link, type, calories, imagetype, protein, fat, carbohydrates):
    

    doc_ref = db.collection('users_Recommendation').document(str(uid)).collection('day').document(str(day))
    doc_ref.update({"Month":str(month)})
    if type == 'Breakfast':
        doc_ref.update({"Breakfast": [{"ID": str(id)}, {"Name": str(name)}, {"Link": str(link)}, {"Image_Type": str(imagetype)}, {"Calories": str(calories)}, {"Protein": str(protein)}, {"Fat": str(fat)}, {"Carbohydrates": str(carbohydrates)}, {"Eat": True}]})
    elif type == 'Lunch':
        doc_ref.update({"Lunch": [{"ID": str(id)}, {"Name": str(name)}, {"Link": str(link)}, {"Image_Type": str(imagetype)}, {"Calories": str(calories)}, {"Protein": str(protein)}, {"Fat": str(fat)}, {"Carbohydrates": str(carbohydrates)}, {"Eat": True}]})
    elif type == 'Dinner':
        doc_ref.update({"Dinner": [{"ID": str(id)}, {"Name": str(name)}, {"Link": str(link)}, {"Image_Type": str(imagetype)}, {"Calories": str(calories)}, {"Protein": str(protein)}, {"Fat": str(fat)}, {"Carbohydrates": str(carbohydrates)}, {"Eat": True}]})
    
    rating(uid, id)
    recalculate(uid)
    return {"message": True}

def getdata(id):

    doc_ref = db.collection('users').document(str(id))
    doc = doc_ref.get()
    if doc.exists:

        return doc.to_dict()
    else:
        return {"message": False}
    
def updateeat(id, day, month, type):

    doc_ref = db.collection('users_Recommendation').document(str(id)).collection('day').document(str(day))
    doc_ref.update({"Month":str(month)})
    doc_ref = db.collection('users_Recommendation').document(str(id)).collection('day').document(str(day))
    if type == 'Breakfast':
        doc = doc_ref.get().to_dict()
        if doc and "Breakfast" in doc:
            rating(id, doc['Breakfast'][0]['ID'])
            recalculate(id)
            for item in doc["Breakfast"]:
                if "Eat" in item:
                    item["Eat"] = True

            doc_ref.set(doc)
    elif type == 'Lunch':
        doc = doc_ref.get().to_dict()

        if doc and "Lunch" in doc:
            rating(id, doc['Lunch'][0]['ID'])
            recalculate(id)
            for item in doc["Lunch"]:
                if "Eat" in item:
                    item["Eat"] = True

            doc_ref.set(doc)
    elif type == 'Dinner':
        doc = doc_ref.get().to_dict()

        if doc and "Dinner" in doc:
            rating(id, doc['Dinner'][0]['ID'])
            recalculate(id)
            for item in doc["Dinner"]:
                if "Eat" in item:
                    item["Eat"] = True

            doc_ref.set(doc)
    

    return {"message": True}

def getcatering(id):

    doc_ref = db.collection('catering').document('Surabaya').collection(str(id)).document('data')
    data = doc_ref.get().to_dict()
    json_data = json.dumps(data)
    return json_data

def nutritioncalc(id, day):

    calories = 0
    protein = 0
    carbs = 0
    fat = 0

    doc_ref = db.collection('users_Recommendation').document(str(id)).collection('day').document(str(day))
    doc = doc_ref.get()

    if doc.exists:
        data = doc.to_dict()

        breakfast = data['Breakfast']
        lunch = data['Lunch']
        dinner = data['Dinner']

        for item in breakfast:
            if 'Eat' in item:
                if item['Eat']:
                    calories += float(breakfast[4]['Calories'])
                    protein += float(breakfast[5]['Protein'])
                    fat += float(breakfast[6]['Fat'])
                    carbs += float(breakfast[7]['Carbohydrates'])
        for item in lunch:
            if 'Eat' in item:
                if item['Eat']:
                    calories += float(lunch[4]['Calories'])
                    protein += float(lunch[5]['Protein'])
                    fat += float(lunch[6]['Fat'])
                    carbs += float(lunch[7]['Carbohydrates'])
        for item in dinner:
            if 'Eat' in item:
                if item['Eat']:
                    calories += float(dinner[4]['Calories'])
                    protein += float(dinner[5]['Protein'])
                    fat += float(dinner[6]['Fat'])
                    carbs += float(dinner[7]['Carbohydrates'])
            
    output = {"Calories" : calories, "Protein": protein, "Fat": fat, "Carbohydrates": carbs}
    return output

def recalculate(id):
    bitterness = 0
    fattiness = 0
    saltiness = 0
    savoriness = 0
    sourness = 0
    sweetness = 0

    i = 0

    doc_ref = db.collection('users').document(str(id)).collection('ratings').document('rate')
    doc = doc_ref.get().to_dict()
    for key, value in doc.items():
        doc_ref = db.collection('food_Main_course').document(str(key))
        if doc_ref.get().exists:
            data = doc_ref.get().to_dict()
            bitterness += float(data['Bitterness']) * int(value)
            fattiness += float(data['Fattiness']) * int(value)
            saltiness += float(data['Saltiness']) * int(value)
            savoriness += float(data['Savoriness']) * int(value)
            sourness += float(data['Sourness']) * int(value)
            sweetness += float(data['Sweetness']) * int(value)
            i += 100
    bitterness /= i
    fattiness /= i
    saltiness /= i
    savoriness /= i
    sourness /= i
    sweetness /= i
    doc_ref = db.collection('users').document(str(id))
    doc_ref.update({'Bitterness': bitterness, 'Fattiness': fattiness, 'Saltiness': saltiness, 'Savoriness': savoriness, 'Sourness': sourness, 'Sweetness': sweetness})




def rating(uid, id):

    doc_ref = db.collection('users').document(str(uid)).collection('ratings').document('rate')
    doc = doc_ref.get()
    if doc.exists:
        doc_ref.update({str(id): '5'})
    else:
        doc_ref.set({str(id): '5'})



def predict(x):
    model = keras.models.load_model("model.h5")
    predictions = model.predict(x)
    label = np.argmax(predictions)
    return label



app = Flask(__name__)

@app.route("/", methods=["GET"])
def index():
    if request.method == "GET":
        return jsonify({"message": "Kamu Berhasil"})
    return "OK"

@app.route("/food", methods=["POST", "GET"])
def food():
    if request.method == "POST":
        file = request.files.get('file')
        if file is None or file.filename == "":
            return jsonify({"error": "no file"})

        try:
            image_bytes = file.read()
            pillow_img = Image.open(io.BytesIO(image_bytes))
            tensor = pillow_img.resize((299, 299))
            tensor = np.expand_dims(tensor, axis=0)
            prediction = predict(tensor)
            data = {"prediction": int(prediction)}
            return jsonify(data)
        except Exception as e:
            return jsonify({"error": str(e)})
    
    if request.method == "GET":
        return jsonify({"message": "Kamu Berhasil"})
    
@app.route("/recommendation", methods=["GET"])
def recommendation():
    if request.method == "GET":
        id = request.args.get('id')
        day = request.args.get('day')
        month = request.args.get('month')
        try:
            output = getrecommendation(id, day, month)
            return jsonify(output)
        except Exception as e:
            return jsonify({"error": str(e)})
    
@app.route("/regisuid", methods=["GET"])
def regisuid():
    if request.method == "GET":
        id = request.args.get('id')
        try:
            output = setuid(id)
            return jsonify(output)
        except Exception as e:
            return jsonify({"error": str(e)})
    
@app.route("/getquestionanswer", methods=["GET"])
def getquestionanswer():
    if request.method == "GET":
        id = request.args.get('id')
        try:
            output = getquestion(id)
            return jsonify(output)
        except Exception as e:
            return jsonify({"error": str(e)})
    
@app.route("/updatequestion", methods=["GET"])
def updatequestion():
    if request.method == "GET":
        id = request.args.get('id')
        activity = request.args.get('activity')
        age = request.args.get('age')
        fruit = request.args.get('fruit')
        healthy = request.args.get('healthy')
        height = request.args.get('height')
        weight = request.args.get('weight')
        name = request.args.get('name')
        gender = request.args.get('gender')
        goals = request.args.get('goals')
        try:
            output = postquestion(id, activity, age, fruit, healthy, height, weight, name, gender, goals)
            return jsonify(output)
        except Exception as e:
            return jsonify({"error": str(e)})
    
@app.route("/gettingnutrition", methods=["GET"])
def gettingnutrition():
    if request.method == "GET":
        id = request.args.get('id')
        try:
            output = getnutrition(id)
            return jsonify(output)
        except Exception as e:
            return jsonify({"error": str(e)})
        
        
@app.route("/updatefoodday", methods=["GET"])
def updatefoodday():
    if request.method == "GET":
        uid = request.args.get('uid')
        id = request.args.get('id')
        day = request.args.get('day')
        month = request.args.get('month')
        name = request.args.get('name')
        link = request.args.get('link')
        type = request.args.get('type')
        calories = request.args.get('calories')
        imagetype = request.args.get('imagetype')
        protein = request.args.get('protein')
        fat = request.args.get('fat')
        carbohydrates = request.args.get('carbohydrates')
        try:
            output = updatefood(uid, id, day, month, name, link, type, calories, imagetype, protein, fat, carbohydrates)
            return jsonify(output)
        except Exception as e:
            return jsonify({"error": str(e)})
        
@app.route("/getuserdata", methods=["GET"])
def getuserdata():
    if request.method == "GET":
        id = request.args.get('id')
        try:
            output = getdata(id)
            return jsonify(output)
        except Exception as e:
            return jsonify({"error": str(e)})
        
@app.route("/updateeaten", methods=["GET"])
def updateeaten():
    if request.method == "GET":
        id = request.args.get('id')
        day = request.args.get('day')
        month = request.args.get('month')
        type = request.args.get('type')
        try:
            output = updateeat(id, day, month, type)
            return jsonify(output)
        except Exception as e:
            return jsonify({"error": str(e)})
        
@app.route("/catering", methods=["GET"])
def catering():
    if request.method == "GET":
        id = request.args.get('id')
        try:
            output = getcatering(id)
            return output
        except Exception as e:
            return jsonify({"error": str(e)})
        
@app.route("/nutritioncalculation", methods=["GET"])
def nutritioncalculation():
    if request.method == "GET":
        id = request.args.get('id')
        day = request.args.get('day')
        try:
            output = nutritioncalc(id, day)
            return output
        except Exception as e:
            return jsonify({"error": str(e)})


if __name__ == "__main__":
    app.run(debug=True)
import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'

import io
import tensorflow as tf
from tensorflow import keras
import numpy as np
from PIL import Image
from flask import Flask, request, jsonify

model = keras.models.load_model("model.h5")

def predict(x):
    predictions = model.predict(x)
    label = np.argmax(predictions)
    return label

app = Flask(__name__)

@app.route("/", methods=["GET", "POST"])
def index():
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

    return "OK"


if __name__ == "__main__":
    app.run(debug=True)
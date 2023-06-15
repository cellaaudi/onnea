# Food Recognition

This is a simple yet powerful model for food recognition. It utilizes a transfer learning approach based on EfficientNetV2-B2, a state-of-the-art convolutional neural network architecture for image classification.

The steps taken in this model are as follows:

## 1. Import Dependencies
First, we import necessary libraries, such as TensorFlow, Scikit-learn, OS, and Glob, which will be used for data processing and model building.

## 2. Setup Environment
We then check whether GPU is available for the training process. If GPU is not detected, the script will raise a RuntimeError.

## 3. Set Image Size and Batch Size
The image size and batch size are defined to be used in the model.

## 4. Load and Preprocess Data
The image paths are loaded and labels are extracted. The labels are also transformed to numeric representation for the model. Images are resized and preprocessed to be used for training and validation.

## 5. Build the Model
We employ the EfficientNetV2-B2 model with weights pre-trained on ImageNet. We then add a Global Average Pooling layer and a Dense layer with a softmax activation function for our multi-class classification problem. We compile the model with the Adam optimizer and sparse categorical cross-entropy loss function.

## 6. Train the Model
We train the model using the train data and validate it using the test data. The model training output shows the loss and accuracy for both training and validation data per epoch.

## 7. Fine-tuning the Model
After the initial round of training, we fine-tune the model. This involves unfreezing the top layers of the base model and continuing the training. A custom callback is defined to stop the training once the validation accuracy reaches 80%.

## 8. Save the Model
Finally, after training and fine-tuning the model, we save it as a `.h5` file for later use.

To run this project, ensure that you have all the necessary libraries installed. You can install any missing libraries using pip and the provided requirements.txt file. 

**Note**: This model requires a significant amount of computational resources due to the complexity of the EfficientNet architecture and the size of the dataset. It is recommended to run this script on a machine with a powerful GPU.
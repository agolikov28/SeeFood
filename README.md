# SeeFood
CMSC436 Final Project Spring 2022

## Concept
#### Idea: 
With SeeFood, users will take pictures of food labels and the app will extract nutritional information from the food labels. This capability will be used in some nutritional cases, such as diabetes or high blood pressure sufferers, lifestyle cases, such as people trying to lose weight or supporting vegetarian lifestyles, and so on. Rather than expecting the user to record every single food item they eat, the purpose of the app would instead be to give a visual display to see how a serving size of the food plays into a healthy, daily diet.

#### Sub-Idea:
What if we made a general app catered towards people who wants to understand their nutritional intake. The can select a diet based on their
height, weight, age, and other factors we can take from a website to calculate a healthy diet. We can use a 
different calculator depending on whether they want a normal diet to maintain weight, a diet to bulk, or one 
to cut weight. We would need to be able to track certain intakes, overall calories, protein, carbohydrates, sugar, sodium, etc.
This app would allow this by scanning nutritional labels (either text from images or directly from the phones camera).
Users would then be able to add and delete this information as they choose. 

## App Design
#### Preliminary Mockup:
<img
  src="Initial Wireframe.jpg"
  style="display: inline-block; margin: 0 auto; width: 60%">
#### Debating App Styles:
<img 
  src="New Diet Screen (v1).jpg"
  style="display: inline-block; margin: 0 auto; width: 20%">
<img 
  src="New Diet Screen (v2).jpg"
  style="display: inline-block; margin: 0 auto; width: 20%">
 <img 
  src="My Diet Screen (v1).jpg"
  style="display: inline-block; margin: 0 auto; width: 20%">
<img 
  src="My Diet Screen (v2).jpg"
  style="display: inline-block; margin: 0 auto; width: 20%">

#### Final Demo Video:


https://user-images.githubusercontent.com/83187638/232629202-de920db2-b93a-40ef-ba48-746b8bac2d8d.mp4


## Fundamental Parts
#### Image Processing: 
We are going to need image processing to get the information from the labels into the application. Listed below are some sources we canuse for image processing.

Using Google Keep: https://keep.google.com/u/0/ and
https://gadgetstouse.com/blog/2021/05/21/copy-text-from-images-on-android-iphone-pc/#:~:text=Thanks%20to%20Google%20Lens%20technology,web%20and%20the%20mobile%20app.
Stack Overflow Android Scanners: https://stackoverflow.com/questions/37287910/how-to-extract-text-from-image-android-app
Camera Scanner: https://v4all123.blogspot.com/2018/03/simple-example-of-ocrreader-in-android.html
Scan From Images: https://www.zoftino.com/extracting-text-from-images-android
Using Firebase: https://developers.google.com/ml-kit/vision/text-recognition/v2/android

#### Data Management:
We plan on managing our data using shared preferences as they are cheap and can store string information over app usages. This is far easier than using a third party storage method such as SQL or Firebase.
We are only going to store one diet at a time. When a user selects to create a new plan, a message will pop up warning them that their current nutrition plan is about to be deleted.

#### Macros Calculator:
https://healthyeater.com/flexible-dieting-calculator

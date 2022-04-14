# SeeFood
CMSC436 Final Project Spring22 Chinhnam's Edit

### Idea: 
With this app users will take pictures of food labels and the app will extract nutritional information
from the food labels. This capability will be used in some nutritional use case (diabetes or high
blood pressure sufferers, people trying to lose weight, people supporting vegetarian lifestyles, etc.)
Rather than expecting the user to record every single food item they eat the purpose of the app
would instead be to give a visual display to see how a serving size of the food plays into a healthy,
daily diet. For example could show a pie chart with what percentage of daily sodium the serving
would be.

#### Sub-Idea:
What if we made a general app catered towards people who go to the gym. The can select a diet based on their
height, weight, age, and other factors we can take from a website to calculate a healthy diet. We can use a 
different calculator depending on whether they want a normal diet to maintain weight, a diet to bulk, or one 
to cut weight.

The app can scan text from images as well as directly from the phones camera. They can add and delete label
information as they choose. 

We need to be able to track certain intakes, overall calories, protein, carbohydrates, sugar, sodium, etc.

We also calculate how much is needed daily based on the diet they previously selected. This way we can show 
how much more of each group they need to eat to fulfil their daily intake.

#### Preliminary Wireframe: [IMG_4971.heic.pdf](https://github.com/agolikov28/SeeFood/files/8438290/IMG_4971.heic.pdf)

## Fundamental Parts
### Image Processing: 

We are going to need image processing to get the information from the labels into the application. Listed below are some sources we canuse for image processing.

Using Google Keep: https://keep.google.com/u/0/ and
https://gadgetstouse.com/blog/2021/05/21/copy-text-from-images-on-android-iphone-pc/#:~:text=Thanks%20to%20Google%20Lens%20technology,web%20and%20the%20mobile%20app.

Stack Overflow Android Scanners: https://stackoverflow.com/questions/37287910/how-to-extract-text-from-image-android-app

Camera Scanner: https://v4all123.blogspot.com/2018/03/simple-example-of-ocrreader-in-android.html

### Data Management:

We plan on managing our data using shared preferences as they are cheap and can store string information over app usages. This is far easier than using a third party storage method such as SQL or Firebase.

We are only going to store one diet at a time. When a user selects to create a new plan, a message will pop up warning them that their current nutrition plan is about to be deleted.

### Macros Calculator:

https://healthyeater.com/flexible-dieting-calculator

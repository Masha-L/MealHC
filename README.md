# MealHC

<p align= "center">
<img width="189" alt="icon2xhdpi" src="https://user-images.githubusercontent.com/44718709/47953085-ae543f00-df4e-11e8-9d55-6c3074f37672.png">

</p>

**MealHC** is an app that acts as a guide to meals at Mount Holyoke College. It takes in a user's dietary restrictions and goes through the MHC menu to come up with different meal options for every day of the week. 

### Installation: 
An android device is required to download and run this app. App can be downloaded from Google Play.


### Usage: 
Users can check off any dietary restrictions or allergies they may have in the settings tab. This will generate a number of meal options that includes entree, sides, soup and accompaniments.


|Deitary restrictions considered presently: | Allergens considered presently:|
|-------------------------------------------|--------------------------------|
| Vegetarian |  Soybean|
| Vegan |  Gluten |
| Kosher |  Nuts |
| Halal |  Tree nuts |
| Gluten Free |  Coconut |
| Pescatarian |  Fish |
| No Dairy |  Shellfish |
| No Eggs |  Dairy |
| No Pork |  Sesame |


### Features
* The calorie intake will be determined based on the age, sex and activity level of the user
* The composition of the meal options will be based on nutritional value requirements

### App mockup

![allldpi](https://user-images.githubusercontent.com/44718709/47955617-22074380-df71-11e8-84a5-d439c88b5aa0.png)

### Screenshots

![screen shot 2018-11-03 at 2 19 51 pm](https://user-images.githubusercontent.com/44718709/47955800-92af5f80-df73-11e8-93dc-f4397cea680c.png)


### How it works:
Data scrape: the data from the Mount Holyoke menu website is extracted into a database. This data includes nutritional values of each food item and the place to find it. The scraped data is put in a .json file in the form of a database.

Data extraction: the algorithm parses the database, extracts the items and creates appropriate objects such as FoodItem, MealOption, or Menu.

Diet Analysis: the algorithm analyzes the information about the user's physical characteristics as well as food allergies, dietary restrictions, and preferences. Then, it uses scientific data to create a list of healthy menu options for the given day.


#### Features that can be added:
1. More dietary restrictions 
2. A tracker of meals consumed


### Contributers
This app was built by students at Mount Holyoke College and UMass for HackHolyoke 2018.


### User disclaimer
This app is not meant to be used by anyone under the age of 18.




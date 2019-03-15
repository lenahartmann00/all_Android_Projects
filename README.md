# Practice Apps - Android
Here are all of my android apps collected, that I coded during the Android Courses (Udemy and Team Treehouse). Those were only programmed to apply the theoretical knowledge. That is why I didn't really pay attention on writing clean code but rather on experimenting different solutions and getting the most learning output out of it. Also I didn't rewrite the code again so it is as unclean and probably not good readable as I 
Nevertheless you will find a quick summary what the app does, one or two images what it looks like and the main learnings I got by implementing this.

## Directory
1. [Currency Converter (here named: Button)](#currency-converter)
2. [Simple Phrases](#simple-phrases)
3. [Multiply](#multiply)
4. [Weather App - Vol. 2](#weather-app-vol.-2)
5. [Guess the Celebrity](#guess-the-celebrity)
6. [Timer](#timer)
7. [News](#news)
8. [Notes](#notes)

## Currency Converter 
### Summary
Converts Euro in US-Dollar and shows the result in a Toast
### Screenshot
![](/Screenshots/currencyConverter.jpg = 250x)
### What I learned
- Creating an EditText, which only excepts numbers, and getting the input of it
- Working with buttons
- Implementing Toasts to show the result of the convertion
- Displaying an image using the ImageView

## Simple Phrases
### Summary
Translates some basic phrases into french, by playing an audio
### Screenshot
![](https://github.com/lenahartmann00/all_Android_Projects/tree/master/Screenshots/simplePhrases.png)
### What I learned
- Playing an audio using a MediaPlayer and Uri
- Structring the buttons with a GridLayout

## Multiply
### Summary
Demonstrates the multiplication table (of the integers 1 through 20) with a slider and a listView
### Screenshots
![](https://github.com/lenahartmann00/all_Android_Projects/tree/master/Screenshots/multiply1.png)
![](https://github.com/lenahartmann00/all_Android_Projects/tree/master/Screenshots/multiply2.png)
### What I learned
- Creating a ListView
- Setting up a SeekBar and a SeekbarListener
- Updating a ListView with new data from the SeekBar using the SeekbarListener

## Weather App - Vol. 2
### Summary
### Screenshot
![](https://github.com/lenahartmann00/all_Android_Projects/tree/master/Screenshots/weatherApp.png)
### What I learned
- Getting information from a REST-API [Apixu](https://www.apixu.com/) (using DownloadTask, HttpURLConnection and InputStreamReader)
- Evaluating the Json data
- Handling Exceptions
- Updating the text of a TextView programmatically

## Guess the Celebrity
### Summary
Multiple Choice quiz game that let's the user guess the celebrity shown in the picture
### Screenshot
![](https://github.com/lenahartmann00/all_Android_Projects/tree/master/Screenshots/guessTheCelebrity.png)
### What I learned
- Downloading HTML code of a website
- Working with HTML code as a String(Cutting and splitting it to get only the necessary information)
- Downloading Images from a website
- Java backend for the game (multiple choice, similar to [BrainTrainer](https://github.com/lenahartmann00/BrainTrainer))

## Timer
### Summary
Simple "egg"-timer that plays a horn sound when finished.
### Screenshot
![](https://github.com/lenahartmann00/all_Android_Projects/tree/master/Screenshots/timer.png)
### What I learned
- Interacting with a timer (making it start and stop by pressing a button)
- Playing a sound when the timer gets to 0ms
- Converting ms in minutes and seconds and showing it with a TextView to the user

## News
### Summary
Shows the New, Top and Best Stories of Hacker News. 
### Screenshot
![](https://github.com/lenahartmann00/all_Android_Projects/tree/master/Screenshots/news.png)
### What I learned
- REST-API: [Hacker News](https://github.com/HackerNews/API)
- WebView to show the current news website
- Setting up a SQLite Database and storing last updated data in order to load while network process is still running (prevents frozen app at a slow internet connection)

## Notes
### Summary
Let's the user take notes
### Screenshot
![](https://github.com/lenahartmann00/all_Android_Projects/tree/master/Screenshots/notes.png)
### What I learned
- Saving input from user in SQLite-Database
- Loading data from database into ListView and EditText
- Using Mulitple Activities (one for the ListView, one for the EditText)

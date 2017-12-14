# Scala_Final_Project

[![CircleCI](https://circleci.com/gh/nolanzsg/Scala_Final_Project.svg?style=svg)](https://circleci.com/gh/nolanzsg/Scala_Final_Project)

### Introduction
This program is a Scala mobile phone recommender system, it shows top 10 mobile phones depending on many different variables. 
There is also a concise web application coming along with it, which displays an image of recommendations based on user's input.

### Key Features
1. This mobile phone recommender system will list top 10 unlocked mobile phones in **different brands**, **price intervals**, **average sentiment score** and **rating score** of each product. 
2. This system will also calculate **average price, number**, **average ratings** and **combined score** of products for vairous price intervals, which in a descending order for recommending purpose.

### Code Instruction
1. Install Apache Spark on your device, run spark-shell.<br/> You will see the **output** in command line, which are the top 10 best-selling unlocked mobile phones for each brand. <br/> Also, corresponding csv files will be found in the folder
2. Choose the range of price, top 10 best-selling mobile phones will be shown in the recommend order.
3. Web application: display screenshots of each result

### Methodology
1. Spark for storing data
2. Sentiment analyses on customer reviews
3. Clustering algorithms for brands and reviews

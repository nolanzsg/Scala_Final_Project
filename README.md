# Scala_Final_Project

### Introduction
This program is a Scala mobile phone recommender system, it shows top 10 mobile phones depending on many different variables. 
There is also a concise web application coming along with it, which displays an image of recommendations based on user's input.
Commandline.txt can be run in command line

### Dataset Overview
Amazon Reviews of unlocked mobile phones in 2016

| Product Name |  Brand Name  |   Price  |  Rating |Reviews  | Reveiw Coutns |
| -----: |:-----:| -----:|-----:|-----:|-----:|
| String     | String | Double |  Int| String| String |

Resource: https://www.kaggle.com/PromptCloudHQ/amazon-reviews-unlocked-mobile-phones

### Key Features
1. This mobile phone recommender system will list top 10 unlocked mobile phones in **different brands**, **price intervals**, **average sentiment score** and **rating score** of each product. 
2. This system will also calculate **average price, number**, **average ratings** and **combined score** of products for vairous price intervals, which in a descending order for recommending purpose.

### Code Instruction
###### REMEMBER TO CHANGE FILE PATH PLEASE
1. Install Apache Spark on your device, run spark-shell.<br/> You will see the **output** in command line, which are the top 10 best-selling unlocked mobile phones for each brand. <br/> Also, corresponding csv files will be found in the folder
2. Choose the range of price, top 10 best-selling mobile phones will be shown in the recommend order.
3. Web application: display screenshots of each result

### Methodology
1. Spark for storing data
2. Sentiment analyses on customer reviews
3. Clustering algorithm and machine learning for analyzing brands and reviews

#### Referenece
1. https://www.kaggle.com/PromptCloudHQ/amazon-reviews-unlocked-mobile-phones
2. https://databricks-prod-cloudfront.cloud.databricks.com/public/4027ec902e239c93eaaa8714f173bcfc/3741049972324885/3783546674231782/4413065072037724/latest.html
3. https://acadgild.com/blog/twitter-sentiment-analysis-using-spark/

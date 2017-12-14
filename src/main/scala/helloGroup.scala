class helloGroup {
  import org.apache.spark.SparkContext
  import org.apache.spark.SparkContext._
  import org.apache.spark.SparkConf

  val conf = new SparkConf().setAppName("Mobile phone Application")
  val sc = new SparkContext(conf)

  //read csv file

  val sqlContext = new org.apache.spark.sql.SQLContext(sc)
  import sqlContext.implicits._
  import org.apache.spark.sql.types.{StructType, StructField, StringType, IntegerType, DoubleType}


  val customSchema = StructType(Array(
    StructField("productName", StringType, true),
    StructField("brandName", StringType, true),
    StructField("price", DoubleType, true),
    StructField("rating", IntegerType, true),
    StructField("reviews", StringType, true)))

  val amazon_Review = sqlContext.read.format("csv")
    .option("header", "true")
    .schema(customSchema)
    .load("/Users/zhusuoge/Desktop/Amazon_Unlocked_Mobile.csv")

  //filter empty lines

  //val amazon_review_filter = amazon_Review.filter(length(amazon_Review("productName"))>0&&length(amazon_Review("brandName"))>0&&length(amazon_Review("price"))>0&&length(amazon_Review("rating"))>0&&length(amazon_Review("reviews"))>0)
  val amazon_review_filter = amazon_Review.filter( $"productname".isNotNull && $"brandName".isNotNull && $"price".isNotNull && $"rating".isNotNull && $"reviews".isNotNull)

  //count product and brand number
  amazon_review_filter.select("productName").distinct.count

  amazon_review_filter.select("brandName").distinct.count

  amazon_review_filter.registerTempTable("amazon_review_filter")

  //top ten sum(rating) brand

  sqlContext.sql("select brandname, sum(rating) as rat from amazon_review_filter group by brandname order by rat DESC limit 10").show

  //Top ten brands average rating and count rating

  sqlContext.sql("select brandname, sum(rating) as rat ,count(rating), sum(rating)/count(rating) from amazon_review_filter group by brandname order by rat DESC limit 10").show

  //Top products for each top ten brands(Samsung, Apple, BLU)

  sqlContext.sql("select productname, sum(rating) as rat ,count(rating), sum(rating)/count(rating) from amazon_review_filter where brandname = 'Samsung' group by productname order by rat DESC limit 10").show

  sqlContext.sql("select productname, sum(rating) as rat ,count(rating), sum(rating)/count(rating) from amazon_review_filter where brandname = 'Apple'  group by productname order by rat DESC limit 10").show

  sqlContext.sql("select productname, sum(rating) as rat ,count(rating), sum(rating)/count(rating) from amazon_review_filter where brandname = 'BLU'  group by productname order by rat DESC limit 10").show

  //Price and rating relation

  sqlContext.sql("select rating, avg(price) from amazon_review_filter group by rating order by rating DESC").show

  //top ten brands average price

  sqlContext.sql("select brandname, sum(rating) as rat ,count(rating),avg(rating), avg(price) from amazon_review_filter group by brandname order by rat DESC limit 10").show






  //Sentiment Score

  val AFINN = sc.textFile("/Users/zhusuoge/Desktop/AFINN.txt").map(x=> x.split("\t")).map(x=>(x(0).toString,x(1).toInt))

  val dictionary = AFINN.collectAsMap



  val calculateScore:(String => Double) = (s:String) => {
    val ReviewWordsSentiment = s.toString.split(" ").map(word => {

      var senti: Double = 0;

      if (dictionary.contains(word.toLowerCase())) {

        senti = dictionary(word.toLowerCase())

      };

      senti;

    });

    val reviewSentiment = (ReviewWordsSentiment.sum/(s.toString.split(" ").length) +4)/1.6
    reviewSentiment
  }
  import org.apache.spark.sql.functions.udf
  val sentiScore = udf(calculateScore)

  val amazon_review_senti = amazon_review_filter.withColumn("sentiScore",sentiScore(col("reviews")))


  amazon_review_senti.registerTempTable("amazon_review_senti")

  val amazon_review_combinedScore = amazon_review_senti.withColumn("combinedScore", ($"rating"+$"sentiScore")/2)
  amazon_review_combinedScore.registerTempTable("amazon_review_combinedScore")



  val amazon_review_avgScore = sqlContext.sql("select productName, brandName, price, avg(rating) as avgRating ,avg(sentiScore) as avgsentimentscore, avg(combinedScore) as avgCombinedScore from amazon_review_combinedScore group by productName,price,brandName")

  amazon_review_avgScore.registerTempTable("amazon_review_avgScore")

  sqlContext.sql("select count(*) from amazon_review_avgScore where avgsentimentscore>avgrating").show

  sqlContext.sql("select count(*) from amazon_review_avgScore where avgsentimentscore<avgrating").show



  //Top ten brands average combinedScore and count combinedScore


  //sqlContext.sql("select brandname, avg(price),sum(combinedScore) as rat ,count(combinedScore), sum(combinedScore)/count(combinedScore) from amazon_review_combinedScore group by brandname order by rat DESC limit 10").show

  //sqlContext.sql("select brandname, avg(price),sum(combinedScore) as rat ,count(combinedScore), sum(combinedScore)/count(combinedScore),avg(rating) from amazon_review_combinedScore group by brandname order by rat DESC limit 10").show


  sqlContext.sql("select brandname, avg(price),sum(combinedScore) as rat ,avg(rating), avg(sentiScore), avg(combinedScore)from amazon_review_combinedScore group by brandname order by rat DESC limit 10").coalesce(1).write.option("header", "true").format("com.databricks.spark.csv").save("/Users/zhusuoge/Desktop/scala_project_output/TopTenBrand.csv")


  sqlContext.sql("select productname, avg(price),sum(combinedScore) as rat ,count(combinedScore), sum(combinedScore)/count(combinedScore) from amazon_review_combinedScore where brandname = 'Samsung' group by productname order by rat DESC limit 10").show



  sqlContext.sql("select rating, avg(price) from amazon_review_filter group by rating order by rating DESC").coalesce(1).write.option("header", "true").format("com.databricks.spark.csv").save("/Users/zhusuoge/Desktop/scala_project_output/rating_price.csv")



  //Top ten brands clustering important words
  val Samsung_reviews = sqlContext.sql("select brandname, reviews from amazon_review_filter where brandname = 'Samsung' ")

  Samsung_reviews.coalesce(1).write.option("header", "true").format("com.databricks.spark.csv").save("/Users/zhusuoge/Desktop/scala_project_output/Samsung_reviews.csv")


  import org.apache.spark.ml.linalg.Vector

  val rawdata = sqlContext.read.format("csv").option("header", "true").option("inferSchema", "true").load("/Users/zhusuoge/Desktop/scala_project_output/Samsung_reviews.csv")

  import org.apache.spark.ml.feature.RegexTokenizer


  // Set params for RegexTokenizer
  val tokenizer = new RegexTokenizer()
    .setPattern("[\\W_]+")
    .setMinTokenLength(3) // Filter away tokens with length < 4
    .setInputCol("reviews")
    .setOutputCol("tokens")

  // Tokenize document
  val tokenized_reviews = tokenizer.transform(rawdata)

  //removeStopWords
  import org.apache.spark.ml.feature.StopWordsRemover

  val remover = new StopWordsRemover()
    .setInputCol("tokens")
    .setOutputCol("filtered")


  val filtered_reviews = remover.transform(tokenized_reviews)


  //Vector of Token Counts

  import org.apache.spark.ml.feature.CountVectorizer

  val vectorizer = new CountVectorizer()
    .setInputCol("filtered")
    .setOutputCol("features")
    .setVocabSize(10000)
    .setMinDF(5)
    .fit(filtered_reviews)


  val countVectors = vectorizer.transform(filtered_reviews).select("features")

  //convert the DataFrame back into an RDD

  import org.apache.spark.sql.Row

  val reviewVectors = countVectors.withColumn("id",monotonicallyIncreasingId)


  val reviewVectors_add = reviewVectors.select("id", "features")


  //val lda_countVector = reviewVectors_add.map { case Row(id: Long, countVector: Vector) => (id,countVector) }



  import org.apache.spark.ml.clustering.LDA

  val lda = new LDA().setK(2).setMaxIter(10)


  val model = lda.fit(reviewVectors_add)


  // Describe topics.
  //val topics = model.describeTopics(5)
  //println("The topics described by their top-weighted terms:")
  //topics.show(false)

  // Shows the result.
  //val transformed = model.transform(reviewVectors_add)
  //transformed.show(false)


  val topicIndices = model.describeTopics(10)
  val vocabList = vectorizer.vocabulary




  val topics = topicIndices.map { r =>r.get(1).asInstanceOf[Seq[Int]].toArray.map(vocabList(_))}


  topics.collect.foreach(_.foreach(println(_)))


  val result = topics.collect.map(x => x.map(x => if(x.isEmpty) 0 else x).mkString(","))

  sc.parallelize(result).coalesce(1).saveAsTextFile("/Users/zhusuoge/Desktop/scala_project_output/Samsung_important_words.txt")



}

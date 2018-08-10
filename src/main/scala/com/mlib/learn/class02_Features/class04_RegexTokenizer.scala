package com.mlib.learn.class02_Features

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.feature.{RegexTokenizer, Tokenizer}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
/**
  * Feature Transformers（特征变换）
  * Tokenizer（分词器） : 将文本 （如一个句子）拆分成单词
  */
object class04_RegexTokenizer {
	def main(args: Array[String]): Unit = {
		Logger.getLogger("org.apache.kafka").setLevel(Level.ERROR)
		Logger.getLogger("org.apache.zookeeper").setLevel(Level.ERROR)
		Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
		Logger.getLogger("org.spark_project").setLevel(Level.ERROR)
		Logger.getLogger("org.elasticsearch").setLevel(Level.ERROR)
		
		val spark: SparkSession = SparkSession
				.builder
				.appName("class02_DataETL")
				.master("local[*]")
				.getOrCreate()
		
		val sentenceDataFrame = spark.createDataFrame(Seq(
			(0, "Hi I heard about Spark"),
			(1, "I wish Java could use case classes"),
			(2, "Logistic,regression,models,are,neat")
		)).toDF("id", "sentence")
		
		val tokenizer = new Tokenizer().setInputCol("sentence").setOutputCol("words")
		val regexTokenizer = new RegexTokenizer()
				.setInputCol("sentence")
				.setOutputCol("words")
				.setPattern("\\W") // alternatively .setPattern("\\w+").setGaps(false)
		
		val countTokens = udf { (words: Seq[String]) => words.length }
		
		val tokenized = tokenizer.transform(sentenceDataFrame)
		tokenized.select("sentence", "words")
				.withColumn("tokens", countTokens(col("words"))).show(false)
		
		val regexTokenized = regexTokenizer.transform(sentenceDataFrame)
		regexTokenized.select("sentence", "words")
				.withColumn("tokens", countTokens(col("words"))).show(false)
		
	
		
	}
}

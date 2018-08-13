package com.mlib.learn.class02_Features

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.feature.NGram
import org.apache.spark.sql.SparkSession

/**
  * n-gram（N元模型）
  * 每n个词一截取
  */
object class06_Ngram {
	def main(args: Array[String]): Unit = {
		Logger.getLogger("org.apache.kafka").setLevel(Level.ERROR)
		Logger.getLogger("org.apache.zookeeper").setLevel(Level.ERROR)
		Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
		Logger.getLogger("org.spark_project").setLevel(Level.ERROR)
		Logger.getLogger("org.elasticsearch").setLevel(Level.ERROR)
		
		val spark: SparkSession = SparkSession
				.builder
				.appName(this.getClass.getName.replace("$", ""))
				.master("local[*]")
				.getOrCreate()
		
		
		val wordDataFrame = spark.createDataFrame(Seq(
			(0, Array("Hi", "I", "heard", "about", "Spark")),
			(1, Array("I", "wish", "Java", "could", "use", "case", "classes")),
			(2, Array("Logistic", "regression", "models", "are", "neat"))
		)).toDF("id", "words")
		
		val ngram = new NGram().setN(2).setInputCol("words").setOutputCol("ngrams")
		
		val ngramDataFrame = ngram.transform(wordDataFrame)
		ngramDataFrame.select("ngrams").show(false)
		
	}
}

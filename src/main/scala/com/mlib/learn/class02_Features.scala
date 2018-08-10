package com.mlib.learn

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.feature.{HashingTF, IDF, IDFModel, Tokenizer}
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * 特征的提取，转化 和 选择
  */
object class02_Features {
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
		
		val sentenceData = spark.createDataFrame(Seq(
			(0.0, "Hi I heard about Spark "),
			(0.0, "I wish Java could use case classes"),
			(1.0, "Logistic regression models are neat")
		)).toDF("label", "sentence")
		sentenceData.show()
		
		val tokenizer: Tokenizer = new Tokenizer().setInputCol("sentence").setOutputCol("words")
		val wordsData: DataFrame = tokenizer.transform(sentenceData)
		wordsData.show()
		
		val hashingTF = new HashingTF().setInputCol("words").setOutputCol("rawFeatures").setNumFeatures(20)
		val featurizedData = hashingTF.transform(wordsData)
		
		val idf: IDF = new IDF().setInputCol("rawFeatures").setOutputCol("features")
		val idfModel: IDFModel = idf.fit(featurizedData)
		
		val rescaledData: DataFrame = idfModel.transform(featurizedData)
		rescaledData.show()
		rescaledData.select("label", "features").show()
	}
	
	
}

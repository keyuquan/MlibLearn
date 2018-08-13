package com.mlib.learn.class02_Features

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.feature.{CountVectorizer, CountVectorizerModel}
import org.apache.spark.sql.SparkSession

/**
  * CountVectorizer
  *
  * 把词语转化为词向量
  */
object class03_CountVectorizerModel {
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
		val df = spark.createDataFrame(Seq(
			(0, Array("ae", "b", "c")),
			(1, Array("a", "b", "b", "c", "a"))
		)).toDF("id", "words")
		
		// 适合语料库中的CountVectorizerModel
		val countVectorizer: CountVectorizer = new CountVectorizer()
				.setInputCol("words")
				.setOutputCol("features")
				.setVocabSize(3)
				.setMinDF(2)
		val cvModel: CountVectorizerModel = countVectorizer.fit(df)
		cvModel.transform(df).show()
		
		// 或者，使用先验词汇表定义CountVectorizerModel
		val cvm = new CountVectorizerModel(Array("a", "b", "c"))
				.setInputCol("words")
				.setOutputCol("features")
		cvm.transform(df).show()
		
		
	}
}

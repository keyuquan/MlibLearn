package com.mlib.learn

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.ml.feature.Word2Vec
import org.apache.spark.ml.linalg.Vector

/**
  * Word2Vec
  * 把一句话 转化为一个词向量
  */
object class02_Features02 {
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
		
		// 输入数据：每行是句子或文档中的一个单词。
		val documentDF: DataFrame = spark.createDataFrame(Seq(
			"Hi I heard about Spark".split(" "),
			"I wish Java could use case classes".split(" "),
			"Logistic regression models are neat".split(" ")
		).map(Tuple1.apply)).toDF("text")
		documentDF.show()
		
		// 学习从单词到矢量的映射。
		val word2Vec = new Word2Vec()
				.setInputCol("text")
				.setOutputCol("result")
				.setVectorSize(3)
				.setMinCount(0)
		val model = word2Vec.fit(documentDF)
		
		val result = model.transform(documentDF)
		result.show(100)
		
		result.collect().foreach { case Row(text: Seq[_], features: Vector) =>
			println(s"Text: [${text.mkString(", ")}] => \nVector: $features\n")
		}
		
	}
}

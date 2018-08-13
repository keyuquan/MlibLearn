package com.mlib.learn.class02_Features

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.feature.StopWordsRemover
import org.apache.spark.sql.SparkSession


/**
  * StopWordsRemover（去停用词）
  * Stop words （停用字）是（在文档中）频繁出现，但未携带太多意义的词语，它们不应该参与算法运算。
  *
  */
object class05_StopWordsRemover {
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
		val remover = new StopWordsRemover()
				.setInputCol("raw")
				.setOutputCol("filtered")
		
		val dataSet = spark.createDataFrame(Seq(
			(0, Seq("I", "saw", "the", "red", "baloon")),
			(1, Seq("Mary", "had", "a", "little", "lamb"))
		)).toDF("id", "raw")
		
		remover.transform(dataSet).show()
		
	}
}

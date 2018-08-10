package com.mlib.learn.class02_Features

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

/**
  * Binarization （二值化）是将数值特征阈值化为二进制（0/1）特征的过程。
  */
object class02_Features07 {
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
		
		import org.apache.spark.ml.feature.Binarizer
		
		val data = Array((0, 0.1), (1, 0.8), (2, 0.2))
		val dataFrame = spark.createDataFrame(data).toDF("id", "feature")
		dataFrame.show()
		
		val binarizer: Binarizer = new Binarizer()
				.setInputCol("feature")
				.setOutputCol("binarized_feature")
				.setThreshold(0.5)
		
		val binarizedDataFrame = binarizer.transform(dataFrame)
		
		println(s"Binarizer output with Threshold = ${binarizer.getThreshold}")
		binarizedDataFrame.show()
		
	}
}

package com.mlib.learn.class02_Features

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

/**
  * CountVectorizer
  */
object class02_Features08 {
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
		import org.apache.spark.ml.feature.PCA
		import org.apache.spark.ml.linalg.Vectors
		
		val data = Array(
			Vectors.sparse(5, Seq((1, 1.0), (3, 7.0))),
			Vectors.dense(2.0, 0.0, 3.0, 4.0, 5.0),
			Vectors.dense(4.0, 0.0, 0.0, 6.0, 7.0)
		)
		val df = spark.createDataFrame(data.map(Tuple1.apply)).toDF("features")
		
		val pca = new PCA()
				.setInputCol("features")
				.setOutputCol("pcaFeatures")
				.setK(3)
				.fit(df)
		
		val result = pca.transform(df).select("pcaFeatures")
		result.show(false)
		
	}
}

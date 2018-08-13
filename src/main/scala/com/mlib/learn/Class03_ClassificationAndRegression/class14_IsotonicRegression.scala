package com.mlib.learn.Class03_ClassificationAndRegression

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.regression.IsotonicRegression

/**
  * 保序回归
  */
object class14_IsotonicRegression {
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
		
		// Loads data.
		val dataset = spark.read.format("libsvm")
				.load("data/mllib/sample_isotonic_regression_libsvm_data.txt")
		
		// Trains an isotonic regression model.
		val ir = new IsotonicRegression()
		val model = ir.fit(dataset)
		
		println(s"Boundaries in increasing order: ${model.boundaries}\n")
		println(s"Predictions associated with the boundaries: ${model.predictions}\n")
		
		// Makes predictions.
		model.transform(dataset).show()
	}
}

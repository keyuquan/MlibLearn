package com.mlib.learn.Class03_ClassificationAndRegression

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.regression.LinearRegression

/**
  * 线性回归
  */
object class08_LinearRegression {
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
	
		// Load training data
		val training = spark.read.format("libsvm")
				.load("data/mllib/sample_linear_regression_data.txt")
		
		val lr = new LinearRegression()
				.setMaxIter(10)
				.setRegParam(0.3)
				.setElasticNetParam(0.8)
		
		// Fit the model
		val lrModel = lr.fit(training)
		
		// Print the coefficients and intercept for linear regression
		println(s"Coefficients: ${lrModel.coefficients} Intercept: ${lrModel.intercept}")
		
		// Summarize the model over the training set and print out some metrics
		val trainingSummary = lrModel.summary
		println(s"numIterations: ${trainingSummary.totalIterations}")
		println(s"objectiveHistory: [${trainingSummary.objectiveHistory.mkString(",")}]")
		trainingSummary.residuals.show()
		println(s"RMSE: ${trainingSummary.rootMeanSquaredError}")
		println(s"r2: ${trainingSummary.r2}")
	}
}

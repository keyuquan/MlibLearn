package com.mlib.learn.Class03_ClassificationAndRegression

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.regression.GeneralizedLinearRegression

/**
  * 广义线性回归
  */
object class09_GeneralizedLinearRegression {
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
		val dataset = spark.read.format("libsvm")
				.load("data/mllib/sample_linear_regression_data.txt")
		
		val glr = new GeneralizedLinearRegression()
				.setFamily("gaussian")
				.setLink("identity")
				.setMaxIter(10)
				.setRegParam(0.3)
		
		// Fit the model
		val model = glr.fit(dataset)
		
		// Print the coefficients and intercept for generalized linear regression model
		println(s"Coefficients: ${model.coefficients}")
		println(s"Intercept: ${model.intercept}")
		
		// Summarize the model over the training set and print out some metrics
		val summary = model.summary
		println(s"Coefficient Standard Errors: ${summary.coefficientStandardErrors.mkString(",")}")
		println(s"T Values: ${summary.tValues.mkString(",")}")
		println(s"P Values: ${summary.pValues.mkString(",")}")
		println(s"Dispersion: ${summary.dispersion}")
		println(s"Null Deviance: ${summary.nullDeviance}")
		println(s"Residual Degree Of Freedom Null: ${summary.residualDegreeOfFreedomNull}")
		println(s"Deviance: ${summary.deviance}")
		println(s"Residual Degree Of Freedom: ${summary.residualDegreeOfFreedom}")
		println(s"AIC: ${summary.aic}")
		println("Deviance Residuals: ")
		summary.residuals().show()

	}
}

package com.mlib.learn.Class03_Regression

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

/**
  * 分类算法
  * 二项式 逻辑 回归
  */
object class01_BinomialLogisticRegression {
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
		
		import org.apache.spark.ml.classification.LogisticRegression
		
		// Load training data
		val training = spark
				.read
				.format("libsvm")
				.load("E:\\workspace\\MlibLearn\\src\\main\\scala\\com\\mlib\\learn\\data\\mllib\\sample_multiclass_classification_data.txt")
		training.show(100)
		val lr = new LogisticRegression()
				.setMaxIter(10)
				.setRegParam(0.3)
				.setElasticNetParam(0.8)
		
		// Fit the model
		val lrModel = lr.fit(training)
		
		// Print the coefficients and intercept for multinomial logistic regression
		println(s"Coefficients: \n${lrModel.coefficientMatrix}")
		println(s"Intercepts: ${lrModel.interceptVector}")
		
	}
}

package com.mlib.learn.Class03_ClassificationAndRegression

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.classification.NaiveBayes
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.sql.SparkSession

/**
  * 朴素贝叶斯
  */
object class07_NaiveBayes {
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
		
		// Load the data stored in LIBSVM format as a DataFrame.
		val data = spark.read.format("libsvm").load("data/mllib/sample_libsvm_data.txt")
		
		// Split the data into training and test sets (30% held out for testing)
		val Array(trainingData, testData) = data.randomSplit(Array(0.7, 0.3), seed = 1234L)
		
		// Train a NaiveBayes model.
		val model = new NaiveBayes()
				.fit(trainingData)
		
		// Select example rows to display.
		val predictions = model.transform(testData)
		predictions.show()
		
		// Select (prediction, true label) and compute test error
		val evaluator = new MulticlassClassificationEvaluator()
				.setLabelCol("label")
				.setPredictionCol("prediction")
				.setMetricName("accuracy")
		val accuracy = evaluator.evaluate(predictions)
		println("Test set accuracy = " + accuracy)
	}
	
}

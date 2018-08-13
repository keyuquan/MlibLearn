package com.mlib.learn.Class03_ClassificationAndRegression

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.classification.{LogisticRegression, OneVsRest}
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.sql.SparkSession

/**
  * 一对一休息分类器（a.k.a.一对全）
  */
object class06_OneVSRest {
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
		
	
		// load data file.
		val inputData = spark.read.format("libsvm")
				.load("data/mllib/sample_multiclass_classification_data.txt")
		
		// generate the train/test split.
		val Array(train, test) = inputData.randomSplit(Array(0.8, 0.2))
		
		// instantiate the base classifier
		val classifier = new LogisticRegression()
				.setMaxIter(10)
				.setTol(1E-6)
				.setFitIntercept(true)
		
		// instantiate the One Vs Rest Classifier.
		val ovr = new OneVsRest().setClassifier(classifier)
		
		// train the multiclass model.
		val ovrModel = ovr.fit(train)
		
		// score the model on test data.
		val predictions = ovrModel.transform(test)
		
		// obtain evaluator.
		val evaluator = new MulticlassClassificationEvaluator()
				.setMetricName("accuracy")
		
		// compute the classification error on test data.
		val accuracy = evaluator.evaluate(predictions)
		println(s"Test Error = ${1 - accuracy}")
	}
}

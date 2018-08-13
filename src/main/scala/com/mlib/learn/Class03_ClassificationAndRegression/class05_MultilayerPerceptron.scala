package com.mlib.learn.Class03_ClassificationAndRegression

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.classification.MultilayerPerceptronClassifier
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator

/**
  * 多层感知器分类器
  */
object class05_MultilayerPerceptron {
	
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
		val data = spark.read.format("libsvm")
				.load("data/mllib/sample_multiclass_classification_data.txt")
		
		// Split the data into train and test
		val splits = data.randomSplit(Array(0.6, 0.4), seed = 1234L)
		val train = splits(0)
		val test = splits(1)
		
		// specify layers for the neural network:
		// input layer of size 4 (features), two intermediate of size 5 and 4
		// and output of size 3 (classes)
		val layers = Array[Int](4, 5, 4, 3)
		
		// create the trainer and set its parameters
		val trainer = new MultilayerPerceptronClassifier()
				.setLayers(layers)
				.setBlockSize(128)
				.setSeed(1234L)
				.setMaxIter(100)
		
		// train the model
		val model = trainer.fit(train)
		
		// compute accuracy on the test set
		val result = model.transform(test)
		val predictionAndLabels = result.select("prediction", "label")
		val evaluator = new MulticlassClassificationEvaluator()
				.setMetricName("accuracy")
		
		println("Test set accuracy = " + evaluator.evaluate(predictionAndLabels))
	}
	
}

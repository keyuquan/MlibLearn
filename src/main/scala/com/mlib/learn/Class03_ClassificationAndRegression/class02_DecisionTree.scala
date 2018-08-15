package com.mlib.learn.Class03_Regression

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.DecisionTreeClassificationModel
import org.apache.spark.ml.classification.DecisionTreeClassifier
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature._

/**
  * 决策树 分类
  *
  */
object class02_DecisionTree {
	
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
		
		val data: DataFrame = spark.read.format("libsvm").load("E:\\workspace\\MlibLearn\\src\\main\\scala\\com\\mlib\\learn\\data\\mllib\\sample_libsvm_data.txt")
		data.show()
		
		// Index labels, adding metadata to the label column.
		// Fit on whole dataset to include all labels in index.
		val labelIndexer: StringIndexerModel = new StringIndexer()
				.setInputCol("label")
				.setOutputCol("indexedLabel")
				.fit(data)
		
		// Automatically identify categorical features, and index them. 自动识别分类功能并对其进行索引
		val featureIndexer: VectorIndexerModel = new VectorIndexer()
				.setInputCol("features")
				.setOutputCol("indexedFeatures")
				.setMaxCategories(4) // features with > 4 distinct values are treated as continuous.
				.fit(data)
		
		// Split the data into training and test sets (30% held out for testing).
		val Array(trainingData, testData) = data.randomSplit(Array(0.7, 0.3))
		
		// Train a DecisionTree model.
		val dt = new DecisionTreeClassifier()
				.setLabelCol("indexedLabel")
				.setFeaturesCol("indexedFeatures")
		
		// Convert indexed labels back to original labels.
		val labelConverter = new IndexToString()
				.setInputCol("prediction")
				.setOutputCol("predictedLabel")
				.setLabels(labelIndexer.labels)
		
		// Chain indexers and tree in a Pipeline.
		val pipeline = new Pipeline()
				.setStages(Array(labelIndexer, featureIndexer, dt, labelConverter))
		
		// Train model. This also runs the indexers.
		val model = pipeline.fit(trainingData)
		
		// Make predictions.
		val predictions: DataFrame = model.transform(testData)
		
		// Select example rows to display.
		predictions.select("predictedLabel", "label", "features").show()
		
		// Select (prediction, true label) and compute test error.
		val evaluator = new MulticlassClassificationEvaluator()
				.setLabelCol("indexedLabel")
				.setPredictionCol("prediction")
				.setMetricName("accuracy")
		val accuracy = evaluator.evaluate(predictions)
		println("Test Error = " + (1.0 - accuracy))
		
		val treeModel = model.stages(2).asInstanceOf[DecisionTreeClassificationModel]
		println("Learned classification tree model:\n" + treeModel.toDebugString)
		
	}
}

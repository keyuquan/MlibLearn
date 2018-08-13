package com.mlib.learn.class01_MlPipelines

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.classification.{LogisticRegression, LogisticRegressionModel}
import org.apache.spark.ml.linalg.{Vector, Vectors}
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.sql.{Row, SparkSession}

/**
  * ML Pipelines（ML管道）组件：
  *
  * Transformers（转换器）:model
  * Estimators（模型学习器）:
  * Pipeline组件的参数:
  * Transformer.transform() 和 Estimator.fit() 都是无状态。以后,可以通过替换概念来支持有状态算法.
  */
object class01_MLPipelines {
	
	def main(args: Array[String]) {
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
		
		// 训练数据集
		val training = spark.createDataFrame(Seq(
			(1.0, Vectors.dense(0.0, 1.1, 0.1)),
			(0.0, Vectors.dense(2.0, 1.0, -1.0)),
			(0.0, Vectors.dense(2.0, 1.3, 1.0)),
			(1.0, Vectors.dense(0.0, 1.2, -0.5))
		)).toDF("label", "features")
		
		// Estimators（模型学习器）： LogisticRegression(逻辑回归)
		val lr = new LogisticRegression()
		//  打印模型选择器的参数
		//		println("LogisticRegression parameters:\n" + lr.explainParams() + "\n")
		
		//设置参数的方法一：设置最大迭代次数 ； 设置正则化参数
		lr.setMaxIter(10).setRegParam(0.01)
		
		// 学习模型Estimators（LogisticRegression）
		val model1: LogisticRegressionModel = lr.fit(training)
		// 根据 Transformers (LogisticRegressionModel) 查找   Estimators（LogisticRegression） 的 参数
		//		println("Model 1 was fit using parameters: " + model1.parent.extractParamMap)
		
		// 设置参数的方法二
		val paramMap = ParamMap(lr.maxIter -> 20)
				.put(lr.maxIter, 30) // Specify 1 Param. This overwrites the original maxIter.
				.put(lr.regParam -> 0.1, lr.threshold -> 0.55) // Specify multiple Params.
		
		// One can also combine ParamMaps.
		val paramMap2 = ParamMap(lr.probabilityCol -> "myProbability") // Change output column name.
		val paramMapCombined = paramMap ++ paramMap2
		
		val model2 = lr.fit(training, paramMapCombined)
		//		println("Model 2 was fit using parameters: " + model2.parent.extractParamMap)
		
		// 测试数据集
		val test = spark.createDataFrame(Seq(
			(1.0, Vectors.dense(-1.0, 1.5, 1.3)),
			(0.0, Vectors.dense(3.0, 2.0, -0.1)),
			(1.0, Vectors.dense(0.0, 2.2, -1.5))
		)).toDF("label", "features")
		
		// 预测数据
		model2.transform(test)
				.select("features", "label", "myProbability", "prediction")
				.collect()
				.foreach { case Row(features: Vector, label: Double, prob: Vector, prediction: Double) =>
					println(s"($features, $label) -> prob=$prob, prediction=$prediction")
				}
		
	}
}

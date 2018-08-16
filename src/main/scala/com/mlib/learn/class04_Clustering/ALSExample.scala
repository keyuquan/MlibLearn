package com.mlib.learn.class04_Clustering

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.recommendation.ALS
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  *
  * 协同过滤：
  * userCF: 基于用户相似度的推荐： 和用户有共同喜好的用户的喜好
  * itemCF: 基于物品相似度的推荐： 和用户购买的商品相似的商品
  * modelCF: 基于模型相似度的计算：根据用户喜好信息，训练处一个模型，根据模型给用户进行推荐
  *
  */
object ALSExample {
	
	case class Rating(userId: Int, movieId: Int, rating: Float, timestamp: Long)
	
	def parseRating(str: String): Rating = {
		val fields = str.split("::")
		assert(fields.size == 4)
		Rating(fields(0).toInt, fields(1).toInt, fields(2).toFloat, fields(3).toLong)
	}
	
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
		import spark.implicits._
		
		// 一.读取数据 ，并把数据划分为 训练集 和 测试集
		val ratings = spark.read.textFile("E:\\workspace\\MlibLearn\\src\\main\\scala\\com\\mlib\\learn\\data\\mllib\\als\\sample_movielens_ratings.txt")
				.map(parseRating)
				.toDF()
		val Array(training, test) = ratings.randomSplit(Array(0.8, 0.2))
		
		// 二.训练数据，构建模型
		val als: ALS = new ALS()
				.setMaxIter(5)
				.setRegParam(0.01)
				.setUserCol("userId")
				.setItemCol("movieId")
				.setRatingCol("rating")
		val model = als.fit(training)
		
		//	model.save("E:\\workspace\\MlibLearn\\src\\main\\scala\\com\\mlib\\learn\\mode")
		// 	val model2: ALS = ALS.load("E:\\workspace\\MlibLearn\\src\\main\\scala\\com\\mlib\\learn\\mode")
		
		// 冷启动问题，丢弃没有标签的数据
		model.setColdStartStrategy("drop")
		
		//三.推荐
		// 1.为每个用户推荐10个电影
		val userRecs: DataFrame = model.recommendForAllUsers(10)
		
		// 为每个电影，推荐10个用户
		val movieRecs = model.recommendForAllItems(10)
		userRecs.show()
		movieRecs.show()
		
		// 四.通过计算测试数据上的RMSE来评估模型
		// 1.真实数据
		val predictions = model.transform(test)
		// 2.预测数据
		val evaluator = new RegressionEvaluator()
				.setMetricName("rmse")
				.setLabelCol("rating")
				.setPredictionCol("prediction")
		// 3、计算预测数据和真实数据的 均方根误差
		val rmse = evaluator.evaluate(predictions)
		println(s"Root-mean-square error = $rmse")
		
		
		spark.stop()
	}
	
}





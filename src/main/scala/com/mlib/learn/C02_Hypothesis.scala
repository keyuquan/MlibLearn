package com.mlib.learn

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.ml.linalg.{Vector, Vectors}
import org.apache.spark.ml.stat.ChiSquareTest
import org.apache.spark.sql.SparkSession
/**
  * 相关度的计算
  */
object C02_Hypothesis  {
	
	def main(args: Array[String]): Unit = {
		
		Logger.getLogger("org.apache.kafka").setLevel(Level.ERROR)
		Logger.getLogger("org.apache.zookeeper").setLevel(Level.ERROR)
		Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
		Logger.getLogger("org.spark_project").setLevel(Level.ERROR)
		Logger.getLogger("org.elasticsearch").setLevel(Level.ERROR)
		
		val sparkSession: SparkSession = SparkSession
				.builder()
				.appName("Spark SQL basic example")
				.config("spark.some.config.option", "some-value")
				.master("local[2]")
				.getOrCreate()
		
		import sparkSession.implicits._
		val sc: SparkContext = sparkSession.sparkContext
		
		val data = Seq(
			(0.0, Vectors.dense(0.5, 10.0)),
			(0.0, Vectors.dense(1.5, 20.0)),
			(1.0, Vectors.dense(1.5, 30.0)),
			(0.0, Vectors.dense(3.5, 30.0)),
			(0.0, Vectors.dense(3.5, 40.0)),
			(1.0, Vectors.dense(3.5, 40.0))
		)
		
		val df = data.toDF("label", "features")
		
		val chi = ChiSquareTest.test(df, "features", "label").head
		println("pValues = " + chi.getAs[Vector](0))
		println("degreesOfFreedom = " + chi.getSeq[Int](1).mkString("[", ",", "]"))
		println("statistics = " + chi.getAs[Vector](2))
		
	}
	
	
}

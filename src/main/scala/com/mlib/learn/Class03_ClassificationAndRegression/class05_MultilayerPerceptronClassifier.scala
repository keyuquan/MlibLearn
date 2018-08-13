package com.mlib.learn.Class03_ClassificationAndRegression

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

/**
  * 多层感知器分类器
  */
object class05_MultilayerPerceptronClassifier {
	
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
	
	}
	
}

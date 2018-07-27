package com.mlib.learn

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.ml.linalg.{Matrix, Vectors}
import org.apache.spark.ml.stat.Correlation
import org.apache.spark.sql.{Row, SparkSession}

/**
  * 相关度的计算
  */
object C01_Correlation {
	
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
			Vectors.sparse(4, Seq((0, 1.0), (3, -2.0))),
			Vectors.dense(4.0, 5.0, 0.0, 3.0),
			Vectors.dense(6.0, 7.0, 0.0, 8.0),
			Vectors.sparse(4, Seq((0, 9.0), (3, 1.0)))
		)
		
		val df = data.map(Tuple1.apply).toDF("features")
		df.show()
		//斯皮尔曼相关矩阵
		val Row(coeff1: Matrix) = Correlation.corr(df, "features").head
		println("Pearson correlation matrix:\n" + coeff1.toString)
		//皮尔森相关矩阵
		val Row(coeff2: Matrix) = Correlation.corr(df, "features", "spearman").head
		println("Spearman correlation matrix:\n" + coeff2.toString)
		
	}
	
	
}

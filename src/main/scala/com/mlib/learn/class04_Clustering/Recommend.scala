package com.mlib.learn.class04_Clustering

import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.storage.StorageLevel

import scala.collection.mutable.ArrayBuffer

/**
  *
  * 基于用户相似度的推荐小案例
  *
  */
object Recommend {
	
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
		
		
		val sc = spark.sparkContext
		//数据清洗 (102,3.0)
		val dataClean: RDD[Array[(Long, Float)]] = sc.textFile("E:\\workspace\\MlibLearn\\src\\main\\scala\\com\\mlib\\learn\\data\\recommend.txt").map { line =>
			val tokens = line.split(",")
			(tokens(0).toLong, (tokens(1).toLong, if (tokens.length > 2) tokens(2).toFloat else 0f))
		}.aggregateByKey(Array[(Long, Float)]())(_ :+ _, _ ++ _).filter(_._2.size > 2).values.persist(StorageLevel.MEMORY_ONLY_SER)
		
		
		//全局计算模 (101,76.25)
		val norms: RDD[(Long, Float)] = dataClean.flatMap(_.map(y => (y._1, y._2 * y._2))).reduceByKey(_ + _)
		norms.foreach(println)
		
		//广播数据 (101,76.25)
		val normsMap = sc.broadcast(norms.collectAsMap())
		
		//共生矩阵 ((106,105),14.0)
		val matrix: RDD[((Long, Long), Float)] = dataClean.map(list => list.sortWith(_._1 > _._1)).flatMap(occMatrix).reduceByKey(_ + _)
		matrix.foreach(println)
		
		//计算相似度 (103,(102,0.1975496259559987)) ： 计算相似度  1/1+ (P（A）+ p(B) - P(A->B) )
		val similarity = matrix.map(a => (a._1._1, (a._1._2, 1 / (1 + Math.sqrt(normsMap.value.get(a._1._1).get + normsMap.value.get(a._1._2).get - 2 * a._2)))))
		
		similarity.collect().foreach(println)
		sc.stop
	}
	
	
	def occMatrix(a: Array[(Long, Float)]): ArrayBuffer[((Long, Long), Float)] = {
		val array = ArrayBuffer[((Long, Long), Float)]()
		//笛卡尔共生
		for (i <- 0 to (a.size - 1); j <- (i + 1) to (a.size - 1)) {
			array += (((a(i)._1, a(j)._1), a(i)._2 * a(j)._2))
		}
		array
	}
}

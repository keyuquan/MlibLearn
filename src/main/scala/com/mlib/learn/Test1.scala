package com.mlib.learn

import org.apache.spark.ml.linalg
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext
import org.apache.spark.ml.linalg.{Matrix, Vectors}
import org.apache.spark.ml.stat.Correlation
import org.apache.spark.sql.Row


class Test1 {
	
	def main(args: Array[String]): Unit = {
		val sc: SparkContext = init()
		val sqlContext: SQLContext = SQLContext.getOrCreate(sc);
		
		val data = Seq(
			Vectors.sparse(4, Seq((0, 1.0), (3, -2.0))),
			Vectors.dense(4.0, 5.0, 0.0, 3.0),
			Vectors.dense(6.0, 7.0, 0.0, 8.0),
			Vectors.sparse(4, Seq((0, 9.0), (3, 1.0)))
		)
		
		val df = data.map(Tuple1.apply).toDF("features")
		val Row(coeff1: Matrix) = Correlation.corr(df, "features").head
		println("Pearson correlation matrix:\n" + coeff1.toString)
		
		val Row(coeff2: Matrix) = Correlation.corr(df, "features", "spearman").head
		println("Spearman correlation matrix:\n" + coeff2.toString)
	}
	
	def init(): SparkContext = {
		
		val conf = new SparkConf().setAppName("LMRetainMain")
				.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
				.set("spark.shuffle.consolidateFiles", "true")
				.set("spark.sql.shuffle.partitions", "60")
				.setMaster("local[*]")
		conf.set("es.nodes", "192.168.121.41")
		conf.set("es.port", "9200")
		conf.set("es.index.auto.create", "true")
		conf.set("es.index.read.missing.as.empty", "true")
		conf.set("es.batch.size.entries", "1000")
		conf.set("es.batch.flush.timeout", "60")
		conf.set("es.mapping.date.rich", "false")
		val sc = new SparkContext(conf)
		sc
	}
	
}

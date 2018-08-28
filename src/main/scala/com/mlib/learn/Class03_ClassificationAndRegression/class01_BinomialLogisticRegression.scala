package com.mlib.learn.Class03_Regression

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

/**
  * 分类算法
  * 二项式 逻辑 回归
  */
object class01_BinomialLogisticRegression {
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
		
		import org.apache.spark.ml.classification.LogisticRegression
		
		// Load training data
		val training = spark
				.read
				.format("libsvm")
				.load("E:\\workspace\\MlibLearn\\src\\main\\scala\\com\\mlib\\learn\\data\\mllib\\sample_multiclass_classification_data.txt")
		training.show(100)
		
		/**
		  * setRegParam : 设置正则化参数，防止过拟合问题，不要设置太大，否则就欠拟合
		  * setElasticNetParam： 正则化范式比(默认0.0)，正则化一般有两种范式：L1(Lasso)和L2(Ridge)。
		  * L1一般用于特征的稀疏化（过滤掉不需要的特征），L2一般用于防止过拟合。这里的参数即设置L1范式的占比，默认0.0即只使用L2范式
		  *
		  * 密集向量和稀疏向量的区别：
		  * 密集向量的值就是一个普通的Double数组 而稀疏向量由两个并列的 数组indices和values组成
		  * 例如：向量(1.0,0.0,1.0,3.0)
		  * 用密集格式表示为[1.0,0.0,1.0,3.0]，
		  * 用稀疏格式表示为(4,[0,2,3],[1.0,1.0,3.0]) 第一个4表示向量的长度(元素个数)，[0,2,3]就是indices数组，[1.0,1.0,3.0]是values数组 表示向量0的位置的值是1.0，2的位置的值是1.0,而3的位置的值是3.0,其他的位置都是0
		  *
		  */
		val lr = new LogisticRegression()
				.setMaxIter(10)
				.setRegParam(0.3)
				.setElasticNetParam(0.8)
		
		// Fit the model
		val lrModel = lr.fit(training)
		
		val prediction = lrModel.transform(training)
		prediction.show(100)
		
		
		// 打印多项逻辑回归的系数和截距
		println(s"Coefficients: \n${lrModel.coefficientMatrix}")
		println(s"Intercepts: ${lrModel.interceptVector}")
		
	}
}

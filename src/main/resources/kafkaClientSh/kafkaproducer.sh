#!/bin/bash
/usr/local/kafka_2.11-0.10.1.0/bin/kafka-console-producer.sh --broker-list isec-hdp01:9092,isec-hdp02:9092,isec-hdp03:9092 --topic  spark_test_01

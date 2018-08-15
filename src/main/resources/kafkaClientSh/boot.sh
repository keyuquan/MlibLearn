#!/bin/bash
export KAFKA_OPTS="-Djava.security.krb5.conf=/etc/krb5.conf -Djava.security.auth.login.config=/usr/hdp/2.6.4.0-91/kafka/conf/kafka_jaas.conf"
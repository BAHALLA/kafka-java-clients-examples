# This repo contains examples of kafka clients with java

To be able to run the examples in this repository you should have a working kafka cluster
you can set up one easily with docker or strimzi, [refer to this repo for more details](https://github.com/BAHALLA/k8s-helm-terraform).

## Producer example 
Check [this blog post](https://strimzi.io/blog/2023/10/03/kafka-producer-client-essentials/), in strimzi website for more details.

To run to producer example:
```shell
 mvn compile
 mvn exec:java -Dexec.mainClass=org.example.Producer
```


## Consumer example
Check [this blog post](https://strimzi.io/blog/2023/11/09/kafka-consumer-client-essentials/), in strimzi website for more details.

To run to consumer example:
```shell
 mvn compile
 mvn exec:java -Dexec.mainClass=org.example.Consumer
```
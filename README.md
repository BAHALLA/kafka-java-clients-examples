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

## Configure kafka client ssl

To configure kafka client to communicate throught ssl with kafka cluster (strimzi)
1. Get the cluster ca cert:
```shell
kubectl -n strimzi get secret my-cluster-cluster-ca-cert -o jsonpath='{.data.ca\.crt}' | base64 -d > ca.crt
```
2. Import the certificate to a truststore and configure the client to use it: 
```shell
keytool -import -file ca.crt -keystore client.truststore.p12 -alias ca -storepass 123456 -noprompt
```
3. configure client ssl configs with the created truststore
4. To test with kafka cli: 
```shell
./kafka-console-producer.sh --broker-list 192.168.49.2:30628 --producer-property security.protocol=SSL --producer-property ssl.truststore.password=123456 --producer-property ssl.truststore.location=/tmp/client.truststore.p12 --topic my-topic
```
# Dogecoin meets Apache Spark !
CPU Mining cryptocurrencies using Apache Spark without any external dependencies.

![](http://dogecoin.com/imgs/dogecoin-300.png "Dogecoin") ![](https://spark.apache.org/images/spark-logo-trademark.png "Apache Spark")

# Overview
Using [CPU Miner](https://github.com/pooler/cpuminer) from https://github.com/pooler/cpuminer in an Apache Spark job that starts tasks on a Yarn/Mesos cluster. Each cluster executor starts an instance of CPU Miner using the provided mining options.

# Usage
## Mining pools
Get yourself an account in any mining pool that supports CPU mining : 

| Currency  | Mining pools   |
|---|---|
| Bitcoin  |  https://en.bitcoin.it/wiki/Comparison_of_mining_pools |
| Litecoin  | https://www.litecoinpool.org/  |
| Dogecoin  | https://aikapool.com/doge/index.php?page=login  |

Keep an eye on https://coinmarketcap.com/ and look for new coins that do not require special mining equipments.

## Package 
Package the project into a single jar (requires [SBT](https://www.scala-sbt.org/)), in the project root run :
```
sbt package
```
You'll get a `spark-yarn-miner_2.11-*.jar` jar.
## Run
Next, use `spark-submit` to submit your job to the cluster :
```
spark-submit --master yarn --class fr.marouni.spark.coins.SparkYarnMiner spark-yarn-miner_2.11-0.1.0-SNAPSHOT.jar --url XXX --username YYY --worker-id WWW --password UUUU
```
Where :
* `--url` : Your mining pool URL (check mining pool docs). Example : stratum+tcp://stratum.aikapool.com:7915
* `--username` : Mining pool account username
* `--worker-id` : Mining pool worker id (check mining pool docs)
* `--password` : Mining pool account password

## Tuning
Tune your mining with the following `spark-submit` options :
* `spark-submit --conf spark.executor.instances=10` on a 10 nodes cluster
* `spark-submit --conf spark.executor.cores=6` on a cluster with 6 cores per node

Recommended tuning : 1 executor per cluster node with maximum allowed number of cores per executor.

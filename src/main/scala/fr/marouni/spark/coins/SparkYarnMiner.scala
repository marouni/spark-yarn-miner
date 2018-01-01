package fr.marouni.spark.coins

import org.apache.log4j.Logger
import org.apache.spark.sql.SparkSession
import org.apache.spark.{ SparkConf, SparkFiles }

object SparkYarnMiner extends App {
  override def main(args: Array[String]) {

    val logger = Logger.getLogger(getClass)

    // Max number of parallel miners launched
    // It doesn't matter as the mappers will run indefinitely (until the job is killed) on executors
    // The mining parameters should be tuned using spark-submit options
    val PARALLEL_MINERS = 1024
    val RUNNER_SCRIPT = "runner.sh"
    val MINER = "minerd"

    val conf = new SparkConf()
    val sparkSession = SparkSession.builder()
      .appName("Spark Yarn Miner")
      .master("local[*]")
      .config(conf)
      .getOrCreate
    val sparkContext = sparkSession.sparkContext

    val tempFolderPath = Utilities.createTempFolder
    val localScriptPath = Utilities.copyResourceToTempFolder(RUNNER_SCRIPT, tempFolderPath)
    val localMinerdPath = Utilities.copyResourceToTempFolder(MINER, tempFolderPath)

    sparkContext.addFile(localScriptPath)
    sparkContext.addFile(localMinerdPath)
    val remoteScriptPath = SparkFiles.get(RUNNER_SCRIPT)
    val remoteMinerdPath = SparkFiles.get(MINER)

    val scriptEnvMap: Map[String, String] = Utilities.parseArguments(args) + ("MINER_EXEC" -> remoteMinerdPath)

    logger.info(s"Running miners with $scriptEnvMap")

    // Launch ...
    sparkContext.emptyRDD[Int]
      .repartition(PARALLEL_MINERS)
      .pipe(remoteScriptPath, scriptEnvMap)
      .count()
  }
}

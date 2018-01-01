package fr.marouni.spark.coins

import java.nio.file.{ Files, Paths }
import java.util.UUID

import org.apache.commons.cli.{ GnuParser, HelpFormatter, Options, ParseException }
import org.apache.log4j.Logger

object Utilities {

  val logger = Logger.getLogger(getClass)
  val DEFAULT_URL = "stratum+tcp://stratum.aikapool.com:7915"
  val DEFAULT_USERNAME = "59427323640714255344"
  val DEFAULT_WORKER_ID = "worker0001"
  val DEFAULT_PASSWROD = "password"

  /**
   * Parse command line arguments into the runner's script env map
   * @param args arguments list
   * @return Map of argument keys and their values
   */
  def parseArguments(args: Array[String]): Map[String, String] = {
    val options = new Options()
    type ArgumentOption = org.apache.commons.cli.Option

    val url = new ArgumentOption("l", "url", true, "Mining URL (stratum+tcp://eu.stratum.slushpool.com:3333)")
    options.addOption(url)

    val username = new ArgumentOption("u", "username", true, "Mining pool username")
    options.addOption(username)

    val workerId = new ArgumentOption("w", "worker-id", true, "Mining pool worker-id")
    options.addOption(workerId)

    val password = new ArgumentOption("p", "password", true, "Mining pool password")
    options.addOption(password)

    val parser = new GnuParser
    val formatter = new HelpFormatter

    try {
      val cmd = parser.parse(options, args)
      Map(
        ("URL", if (cmd.getOptionValue("url") == null) DEFAULT_URL else cmd.getOptionValue("url")),
        ("USERNAME", if (cmd.getOptionValue("username") == null) DEFAULT_USERNAME else cmd.getOptionValue("username")),
        ("WORKER_ID", if (cmd.getOptionValue("worker-id") == null) DEFAULT_WORKER_ID else cmd.getOptionValue("worker-id")),
        ("PASSWORD", if (cmd.getOptionValue("password") == null) DEFAULT_PASSWROD else cmd.getOptionValue("password"))
      )
    } catch {
      case pe: ParseException =>
        formatter.printHelp("SparkYarnMiner", options)
        logger.warn("Error parsing arguments, going with defaults")
        Map(
          ("url", DEFAULT_URL),
          ("username", DEFAULT_USERNAME),
          ("worker-id", DEFAULT_WORKER_ID),
          ("password", DEFAULT_PASSWROD)
        )
    }
  }

  /**
   * Copy resource to temp folder and return local resource path
   * @param resourceName resource name
   * @param tempFolder temp folder path
   * @return local path of copied resource
   */
  def copyResourceToTempFolder(resourceName: String, tempFolder: String): String = {
    val resourceInputStream = getClass.getClassLoader.getResourceAsStream(resourceName)
    val localFilePath = Paths.get(tempFolder, resourceName)
    Files.copy(resourceInputStream, localFilePath)
    resourceInputStream.close()
    localFilePath.toString
  }

  /**
   * Create a temp folder
   * @return temp folder path
   */
  def createTempFolder: String = {
    val tempFolderPath = Files.createTempDirectory(UUID.randomUUID().toString)
    tempFolderPath.toFile.deleteOnExit()
    tempFolderPath.toAbsolutePath.toString
  }

  def cleanUp(tempFolder: String): Unit = {
    Paths.get(tempFolder).toFile.delete()
  }

}

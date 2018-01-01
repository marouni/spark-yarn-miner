// description
organization := "fr.marouni"
name := "spark-yarn-miner"
version := "0.1.0-SNAPSHOT"

// scala
scalaVersion := "2.11.11"
val scalaStringVersion = scalaVersion.toString

// dependencies
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.2.1"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.2.1"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.2.1"
libraryDependencies += "org.apache.spark" %% "spark-mllib" % "2.2.1"
libraryDependencies += "org.apache.spark" %% "spark-hive" % "2.2.1"

// test dependencies
libraryDependencies += "org.specs2" %% "specs2-core" % "4.0.0" % "test"
scalacOptions in Test ++= Seq("-Yrangepos")

// Scalariform settings
import scalariform.formatter.preferences._
scalariformPreferences := scalariformPreferences.value
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(DoubleIndentConstructorArguments, true)
    .setPreference(DanglingCloseParenthesis, Preserve)
scalariformAutoformat := true
scalariformWithBaseDirectory := true

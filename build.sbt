name := "akka-scala-tdd-katas"
version := "1.0"
scalaVersion := "2.12.4"

lazy val common = Seq(
  version := "1.0",
  scalaVersion := "2.12.4",
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.5.9",

    "org.scalatest" %% "scalatest" % "3.0.4" % Test,
    "com.typesafe.akka" %% "akka-testkit" % "2.5.9" % Test
  )
)

scalacOptions ++= Seq("-deprecation", "-feature")

lazy val root = Project("akka-scala-tdd-katas", file("."))
  .aggregate(
    bowlingGameFirstDay, bowlingGameSecondDay, bowlingGameThirdDay, bowlingGameFourthDay, bowlingGameFifthDay,
    bowlingGameSixthDay, bowlingGameSeventhDay
  )

lazy val bowlingGameFirstDay = project.in(file("bowling-game-kata/bowling-game-day-1"))
  .settings(common: _*)
  .settings(name := "bowling-game-day-1")

lazy val bowlingGameSecondDay = project.in(file("bowling-game-kata/bowling-game-day-2"))
  .settings(common: _*)
  .settings(name := "bowling-game-day-2")

lazy val bowlingGameThirdDay = project.in(file("bowling-game-kata/bowling-game-day-3"))
  .settings(common: _*)
  .settings(name := "bowling-game-day-3")

lazy val bowlingGameFourthDay = project.in(file("bowling-game-kata/bowling-game-day-4"))
  .settings(common: _*)
  .settings(name := "bowling-game-day-4")

lazy val bowlingGameFifthDay = project.in(file("bowling-game-kata/bowling-game-day-5"))
  .settings(common: _*)
  .settings(name := "bowling-game-day-5")

lazy val bowlingGameSixthDay = project.in(file("bowling-game-kata/bowling-game-day-6"))
  .settings(common: _*)
  .settings(name := "bowling-game-day-6")

lazy val bowlingGameSeventhDay = project.in(file("bowling-game-kata/bowling-game-day-7"))
  .settings(common: _*)
  .settings(name := "bowling-game-day-7")

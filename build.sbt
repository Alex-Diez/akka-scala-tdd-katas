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
    bowling_game_day_1, bowling_game_day_2, bowling_game_day_3, bowling_game_day_4, bowling_game_day_5,
    bowling_game_day_6, bowling_game_day_7, bowling_game_day_8, bowling_game_day_9, bowling_game_day_10
  )

lazy val bowling_game_day_1 = project.in(file("bowling_game_kata/bowling_game_day_1"))
  .settings(common: _*)
  .settings(name := "bowling_game_day_1")

lazy val bowling_game_day_2 = project.in(file("bowling_game_kata/bowling_game_day_2"))
  .settings(common: _*)
  .settings(name := "bowling_game_day_2")

lazy val bowling_game_day_3 = project.in(file("bowling_game_kata/bowling_game_day_3"))
  .settings(common: _*)
  .settings(name := "bowling_game_day_3")

lazy val bowling_game_day_4 = project.in(file("bowling_game_kata/bowling_game_day_4"))
  .settings(common: _*)
  .settings(name := "bowling_game_day_4")

lazy val bowling_game_day_5 = project.in(file("bowling_game_kata/bowling_game_day_5"))
  .settings(common: _*)
  .settings(name := "bowling_game_day_5")

lazy val bowling_game_day_6 = project.in(file("bowling_game_kata/bowling_game_day_6"))
  .settings(common: _*)
  .settings(name := "bowling_game_day_6")

lazy val bowling_game_day_7 = project.in(file("bowling_game_kata/bowling_game_day_7"))
  .settings(common: _*)
  .settings(name := "bowling_game_day_7")

lazy val bowling_game_day_8 = project.in(file("bowling_game_kata/bowling_game_day_8"))
  .settings(common: _*)
  .settings(name := "bowling_game_day_8")

lazy val bowling_game_day_9 = project.in(file("bowling_game_kata/bowling_game_day_9"))
  .settings(common: _*)
  .settings(name := "bowling_game_day_9")

lazy val bowling_game_day_10 = project.in(file("bowling_game_kata/bowling_game_day_10"))
  .settings(common: _*)
  .settings(name := "bowling_game_day_10")

import java.util.Date

name := "sbt-example"

description := "SBT examples, sample tasks"

enablePlugins(JavaAppPackaging)

val akkaVersion = "2.3.2"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "org.scalatest" % "scalatest_2.10" % "2.2.1" % "test",
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test"
)
//Setting key is evaluated one upone project load time
val simpleSettingKey = settingKey[Long]("Simple setting key")

simpleSettingKey := new Date().getTime //no matter howmany times called its the same. Evaluated only once.

val rand = taskKey[Unit]("Print somethign random")

val ten = taskKey[Int]("Print somethign random")

ten := 10

rand := {
  //re-evaluated every time you run, result in different number of printlns
  val i = new java.util.Random().nextInt(10)
  for (j <- 0 to i) println(j)
}


//Tasks with scope
val testTask = taskKey[Unit]("This should run in test scope")

testTask in Test := println(new Date().toString) //this can be executed using test scope. test:testTaskjjj

//Modifying a defined setting ************************************************
val baseSetting = settingKey[Seq[String]]("")

baseSetting := Seq("1", "2")

//modifying a setting
baseSetting += "3" //baseSetting will now print  List(1, 2, 3)
//************************************************

//Modifying a defined task ************************************************

ten <<= ten.map((i: Int) => {
  i * 2
})

val simpleTask = taskKey[Unit]("")

simpleTask := println("test")
//simple task take input from and prints it.
simpleTask <<= ten.map((i: Int) => println(i)) //prints the updated ten task

//Adding a new file in home folder to universal packager.
mappings in Universal := {
  val awsomeFile = baseDirectory.value.listFiles().filter(_.getName.contains("MyAwsomeFile.txt")).head
  //this add a Awsome file to new folder called lib1, and rename the file..
  (mappings in Universal).value :+(awsomeFile, "lib1/AWSOMENESS")
}

simpleTask := println(UniversalSrc.toString())

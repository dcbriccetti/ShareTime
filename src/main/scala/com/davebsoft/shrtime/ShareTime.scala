package com.davebsoft.shrtime

import org.querki.jquery._
import org.scalajs.dom

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("com.davebsoft.shrtime.ShareTime")
object ShareTime {
  private var students = Seq[Student]()
  private var periodMins = 60

  def main(args: Array[String]): Unit = $(() => setUpUi())

  private def setUpUi() = {
    val periodMinsElem = $("#period-mins")
    periodMinsElem value periodMins.toString
    periodMinsElem.change(() => {
      periodMins = periodMinsElem.valueString.toInt
      setBarMaxes()
    })

    dom.window.setInterval(() => update(), 1000)
  }

  @JSExport
  def addStudents(): Unit = {
    val sn = $("#stu-names")
    val names = sn.valueString.trim().split(',').map(_.trim)
    names foreach addStudent
    setBarMaxes()
    sn.value("")
    $("#stu-display").removeClass("invisible")
  }

  private def setBarMaxes(): Unit = {
    val max = periodMins / students.size
    students.foreach(_.setBarMax(max))
  }

  private def addStudent(stuName: String): Unit = students :+= Student(stuName)

  private def update(): Unit = {
    students.foreach { student =>
      if (student.active)
        student addPortionOfSharedSecond students.count(_.active)
      val mins = "%.2f".format(student.minutesUsed)
      student.progressBar value mins
      student.progressText text mins
    }
  }
}

case class Student(name: String) {
  var minutesUsed = 0D
  var active = false
  private val progressTd = createElement().children(".stu-progress-td")
  val progressBar: JQuery = progressTd.children("progress")
  val progressText: JQuery = progressTd.children(".stu-progress-num")

  private def createElement() = {
    val element = $("#stu-tr-template").clone().removeAttr("id").removeAttr("hidden")
    element.children("td.checkbox-label").text(name)
    element.children(".toggle-td").children(".stu-toggle").click(() => active = !active)
    element appendTo "#stu-table"
    element
  }

  def addPortionOfSharedSecond(numActive: Int): Unit = minutesUsed += 1D / 60 / numActive

  def setBarMax(max: Int): Unit = progressBar.attr("max", max)
}

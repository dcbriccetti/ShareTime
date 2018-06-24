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
      adjustForStudentCount()
    })

    dom.window.setInterval(() => update(), 1000)
  }

  @JSExport
  def addStudents(): Unit = {
    val sn = $("#stu-names")
    val names = sn.valueString.split("[,\\n]").map(_.trim).filter(_.nonEmpty)
    names foreach addStudent
    adjustForStudentCount()
    sn.value("")
  }

  private def adjustForStudentCount(): Unit = {
    if (students.nonEmpty) {
      val minutesPerStudent = math.round(periodMins.toFloat / students.size)
      $(".minutes-per").text(minutesPerStudent.toString)
      students.foreach(_.setBarMax(minutesPerStudent))
      $("#stu-display").removeClass("invisible")
    } else {
      $("#stu-display").addClass("invisible")
    }
  }

  private def addStudent(stuName: String): Unit = students :+= Student(stuName)

  private def update(): Unit = {
    students.foreach { student =>
      if (student.active)
        student addPortionOfSharedSecond students.count(_.active)
      val formattedMinutes = "%.2f".format(student.minutesUsed)
      student.progressBar value formattedMinutes
      student.progressText text formattedMinutes
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
    val e = $("#stu-tr-template").clone().removeAttr("id").removeAttr("hidden")
    e.children("td.checkbox-label").text(name)
    e.children(".toggle-td").children(".stu-toggle").click(() => active = !active)
    e appendTo "#stu-table"
    e
  }

  def addPortionOfSharedSecond(numActive: Int): Unit = minutesUsed += 1D / 60 / numActive

  def setBarMax(max: Int): Unit = progressBar.attr("max", max)
}

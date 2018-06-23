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
    periodMinsElem.value(periodMins.toString)
    periodMinsElem.change(() => {
      periodMins = periodMinsElem.valueString.toInt
      students.foreach(_.progressBar.attr("max", periodMins))
    })

    dom.window.setInterval(() => update(), 1000)
  }

  @JSExport
  def addStudents(): JQuery = {
    val sn = $("#stu-names")
    val names = sn.valueString.trim().split(',').map(_.trim)
    names.foreach(addStudent)
    sn.value("")
  }

  private def addStudent(stuName: String): Unit = {
    val newElem = $("#stu-tr-template").clone().removeAttr("id").removeAttr("hidden")
    newElem.children("td.checkbox-label").text(stuName)
    val newStu = Student(stuName, newElem)
    students :+= newStu
    newElem.children(".toggle-td").children(".stu-toggle").click(() => newStu.active = !newStu.active)
    newElem.appendTo("#stu-table")
  }

  private def update(): Unit = {
    val numActive = students.count(_.active)
    if (numActive > 0)
      students.foreach { student =>
        if (student.active) {
          student.addPortionOfSharedSecond(numActive)
          val minsUsed = "%.2f".format(student.minutesUsed)
          student.progressBar.value(minsUsed)
          student.element.children(".stu-progress-num").text(minsUsed)
        }
      }
  }
}

case class Student(name: String, element: JQuery) {
  var minutesUsed = 0D
  var active = false

  def progressBar: JQuery = element.children(".stu-progress-bar-tr").children("progress")

  def addPortionOfSharedSecond(numActive: Int): Unit = minutesUsed += 1D / 60 / numActive
}

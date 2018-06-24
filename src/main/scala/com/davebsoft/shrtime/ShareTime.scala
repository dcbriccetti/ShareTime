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
  }

  private def setBarMaxes(): Unit = {
    students.foreach(_.setBarMax(periodMins / students.size))
  }

  private def addStudent(stuName: String): Unit = {
    val newStu = Student(stuName)
    students :+= newStu
  }

  private def update(): Unit = {
    val numActive = students.count(_.active)
    students.foreach { student =>
      if (student.active) student addPortionOfSharedSecond numActive
      val mins = "%.2f".format(student.minutesUsed)
      student.progressBar value mins
      student.progressText text mins
    }
  }
}

case class Student(name: String) {
  var minutesUsed = 0D
  var active = false
  private val element = createElement()

  private def createElement() = {
    val element = $("#stu-tr-template").clone().removeAttr("id").removeAttr("hidden")
    element.children("td.checkbox-label").text(name)
    element.children(".toggle-td").children(".stu-toggle").click(() => active = !active)
    element appendTo "#stu-table"
    element
  }

  def progressBar: JQuery = progressTd.children("progress")

  def progressText: JQuery = progressTd.children(".stu-progress-num")

  def addPortionOfSharedSecond(numActive: Int): Unit = minutesUsed += 1D / 60 / numActive

  def setBarMax(max: Int): Unit = progressBar.attr("max", max)

  private def progressTd = element.children(".stu-progress-td")
}

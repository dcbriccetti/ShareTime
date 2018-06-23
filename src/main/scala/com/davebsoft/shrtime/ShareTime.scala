package com.davebsoft.shrtime

import org.querki.jquery._
import org.scalajs.dom

import scala.math.BigDecimal.RoundingMode.HALF_UP

object ShareTime {
  private var students = Seq[Student]()
  private var periodMins = 60

  def main(args: Array[String]): Unit = $(() => setUpUi())

  private def setUpUi() = {
    $("#add-students").click(() => {
      val sn = $("#stu-name")
      sn.valueString.split(',').map(_.trim).foreach(addStudent)
      sn.value("")
    })

    val periodMinsElem = $("#period-mins")
    periodMinsElem.value(periodMins.toString)
    periodMinsElem.change(() => {
      periodMins = periodMinsElem.valueString.toInt
      students.foreach(_.progressBar.attr("max", periodMins))
    })

    dom.window.setInterval(() => update(), 1000)
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
          student.addSharedPortionOfThisSecond(numActive)
          val mu = BigDecimal(student.minutesUsed).setScale(1, HALF_UP).toString
          student.progressBar.value(mu)
          student.element.children(".stu-progress-num").text(mu)
        }
      }
  }
}

case class Student(name: String, element: JQuery) {
  var minutesUsed = 0D
  var active = false

  def progressBar: JQuery = element.children(".stu-progress-bar-tr").children("progress")

  def addSharedPortionOfThisSecond(numActive: Int): Unit = minutesUsed += 1D / 60 / numActive
}

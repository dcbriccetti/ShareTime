package com.davebsoft.shrtime

import org.querki.jquery._
import org.scalajs.dom

import scala.math.BigDecimal.RoundingMode.HALF_UP

object ShareTime {
  private var students = Seq[Student]()

  def main(args: Array[String]): Unit = $(() => setUpUi())

  private def setUpUi() = {
    $("#add").click(() => addStudent())
    dom.window.setInterval(() => update(), 1000)
  }

  private def addStudent(): Unit = {
    val newElement = $(".stu-div").clone().removeClass("stu-div").removeAttr("hidden")
    val stuName = $("#stu-name").valueString
    newElement.children(".checkbox-label").text(stuName)
    val newStu = Student(stuName, newElement)
    students :+= newStu
    newElement.children(".stu-toggle").click(() => toggleStudent(newStu))
    newElement.appendTo("#students")
  }

  private def toggleStudent(student: Student): Unit = {
    student.active = !student.active
  }

  private def update(): Unit = {
    val numActive = students.count(_.active)
    if (numActive > 0)
      students.foreach { student =>
        if (student.active) {
          student.minutesUsed += 1D / 60 / numActive
          val mu = BigDecimal(student.minutesUsed).setScale(1, HALF_UP).toString
          student.element.children(".stu-progress-bar").value(mu)
          student.element.children(".stu-progress-num").text(mu)
        }
      }
  }
}

case class Student(name: String, element: JQuery) {
  var minutesUsed = 0D
  var active = false
}

package com.davebsoft.shrtime

import org.querki.jquery._
import org.scalajs.dom

object ShareTime {
  val student = Student()

  def main(args: Array[String]): Unit = {
    $(() =>
    setUpUi())
  }

  private def setUpUi() = {
    $("#stu1-toggle").click(() => toggleStudent())
    dom.window.setInterval(() => update(), 1000)
  }

  private def toggleStudent(): Unit = {
    dom.console.log($("toggleStudent"))
    student.active = !student.active
  }

  def update(): Unit = {
    if (student.active) {
      student.secondsUsed += 1
      $("body").append(s"<p>${student.secondsUsed}</p>")
    }
  }
}

case class Student() {
  var secondsUsed = 0D
  var active = false
}

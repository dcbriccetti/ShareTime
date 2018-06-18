package com.davebsoft.shrtime

import org.querki.jquery._

object ShareTime {
  def main(args: Array[String]): Unit = {
    $(() => setupUI())
  }

  def setupUI(): Unit = {
    $("body").append("<p>Hello World</p>")
    $("#click-me-button").click(() => addClickedMessage())
  }

  def addClickedMessage(): Unit = {
    $("body").append("<input type='checkbox'/>Student 1")
  }
}

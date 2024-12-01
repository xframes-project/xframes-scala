package dev.xframes

import play.api.libs.json.{JsArray, JsObject, Json}

object MyCallbackHandler extends AllCallbacks {
  // Singleton setup for xframes
  var xframes: XFramesWrapper = _

  // Initialization block for singleton setup
  def initialize(xframesWrapper: XFramesWrapper): Unit = {
    xframes = xframesWrapper
  }

  override def onInit(): Unit = {
    println("Initialization callback called!")

    val rootNode: JsObject = Json.obj(
      "id" -> 0,
      "type" -> "node",
      "root" -> true
    )

    val textNode: JsObject = Json.obj(
      "id" -> 1,
      "type" -> "unformatted-text",
      "text" -> "Hello, world!"
    )

    val children: JsArray = JsArray(Seq(Json.toJson(1)))

    // Set elements
    xframes.setElement(rootNode.toString())
    xframes.setElement(textNode.toString())
    xframes.setChildren(0, children.toString())
  }

  override def onTextChanged(id: Int, text: String): Unit = {
    println(s"Text changed (ID: $id, Text: $text)")
  }

  override def onComboChanged(id: Int, value: Int): Unit = {
    println(s"Combo changed (ID: $id, Value: $value)")
  }

  override def onNumericValueChanged(id: Int, value: Float): Unit = {
    println(s"Numeric value changed (ID: $id, Value: $value)")
  }

  override def onBooleanValueChanged(id: Int, value: Boolean): Unit = {
    println(s"Boolean value changed (ID: $id, Value: $value)")
  }

  override def onMultipleNumericValuesChanged(id: Int, values: Array[Float]): Unit = {
    print(s"Multiple numeric values changed (ID: $id, Values: ")
    values.foreach(value => print(s"$value "))
    println(")")
  }

  override def onClick(id: Int): Unit = {
    println(s"Click callback (ID: $id)")
  }
}

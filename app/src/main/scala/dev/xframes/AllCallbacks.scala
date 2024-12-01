package dev.xframes

trait AllCallbacks {
  def onInit(): Unit
  def onTextChanged(id: Int, text: String): Unit
  def onComboChanged(id: Int, value: Int): Unit
  def onNumericValueChanged(id: Int, value: Float): Unit
  def onBooleanValueChanged(id: Int, value: Boolean): Unit
  def onMultipleNumericValuesChanged(id: Int, values: Array[Float]): Unit
  def onClick(id: Int): Unit
}

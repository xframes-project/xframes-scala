package dev.xframes

import play.api.libs.json._
import java.util.concurrent.{Executors, TimeUnit}

// Case class to represent font definition
case class FontDef(name: String, sizes: List[Int])

object FontDef {
  // Implicit JSON format for FontDef to convert between case class and JSON
  implicit val fontDefFormat: Format[FontDef] = Json.format[FontDef]
}

class XFramesWrapper {
  // Native method declarations
  @native def setElement(elementJson: String): Unit
  @native def setChildren(parentId: Int, childrenJson: String): Unit
  @native def init(
                    assetsBasePath: String,
                    rawFontDefinitions: String,
                    rawStyleOverrideDefinitions: String,
                    allCallbacks: AllCallbacks
                  ): Unit

  // Load the native library
  System.loadLibrary("xframesjni")
}

object XFramesWrapperMain {
  def main(args: Array[String]): Unit = {
    println("Start!")

    val xframes = new XFramesWrapper()

    // Initialize callbacks
    MyCallbackHandler.initialize(xframes)

    // Initialize with paths and callbacks
    xframes.init(
      "C:\\dev\\xframes-scala\\assets",
      getFontDefinitions(),
      getStyleOverrides(),
      MyCallbackHandler
    )

    // Start periodic task
    keepProcessRunning()
  }

  // Keeps the process running periodically
  def keepProcessRunning(): Unit = {
    val scheduler = Executors.newScheduledThreadPool(1)

    val task = new Runnable {
      def run(): Unit = {
//        println("Running...");
      }
    }

    // Schedule the task every second
    scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS)
  }

  // Returns font definitions as a JSON string
  def getFontDefinitions(): String = {
    // Define the font definitions
    val fontDefs = List(
      FontDef("roboto-regular", List(16, 18, 20, 24, 28, 32, 36, 48))
    )

    // Flatten the font definitions
    val flattenedDefs = fontDefs.flatMap { fontEntry =>
      fontEntry.sizes.map { size =>
        Json.obj(
          "name" -> fontEntry.name,
          "size" -> size
        )
      }
    }

    // Create the final JSON structure with a "defs" key holding the flattened list
    val json = Json.obj(
      "defs" -> JsArray(flattenedDefs)
    )

    // Return the JSON string
    json.toString()
  }

  // Returns style overrides as a JSON string
  def getStyleOverrides(): String = {
    val theme2Colors = Map(
      "white" -> "#fff",
      "lighterGrey" -> "#7A818C",
      "black" -> "#0A0B0D",
      "lightGrey" -> "#5a5a5a",
      "darkestGrey" -> "#141f2c",
      "darkerGrey" -> "#2a2e39",
      "darkGrey" -> "#363b4a",
      "evenLighterGrey" -> "#8491a3",
      "green" -> "#75f986",
      "red" -> "#ff0062"
    )

    // Create a map to hold color overrides with their IDs.
    val colorMap: Map[Int, JsArray] = Map(
      ImGuiCol.Text.id -> JsArray(Seq(JsString(theme2Colors("white")), JsNumber(1))),
      ImGuiCol.TextDisabled.id -> JsArray(Seq(JsString(theme2Colors("lighterGrey")), JsNumber(1))),
      ImGuiCol.WindowBg.id -> JsArray(Seq(JsString(theme2Colors("black")), JsNumber(1))),
      ImGuiCol.ChildBg.id -> JsArray(Seq(JsString(theme2Colors("black")), JsNumber(1))),
      ImGuiCol.PopupBg.id -> JsArray(Seq(JsString(theme2Colors("white")), JsNumber(1))),
      ImGuiCol.Border.id -> JsArray(Seq(JsString(theme2Colors("lightGrey")), JsNumber(1))),
      ImGuiCol.BorderShadow.id -> JsArray(Seq(JsString(theme2Colors("darkestGrey")), JsNumber(1))),
      ImGuiCol.FrameBg.id -> JsArray(Seq(JsString(theme2Colors("black")), JsNumber(1))),
      ImGuiCol.FrameBgHovered.id -> JsArray(Seq(JsString(theme2Colors("darkerGrey")), JsNumber(1))),
      ImGuiCol.FrameBgActive.id -> JsArray(Seq(JsString(theme2Colors("lightGrey")), JsNumber(1))),
      ImGuiCol.TitleBg.id -> JsArray(Seq(JsString(theme2Colors("lightGrey")), JsNumber(1))),
      ImGuiCol.TitleBgActive.id -> JsArray(Seq(JsString(theme2Colors("darkerGrey")), JsNumber(1))),
      ImGuiCol.TitleBgCollapsed.id -> JsArray(Seq(JsString(theme2Colors("lightGrey")), JsNumber(1))),
      ImGuiCol.MenuBarBg.id -> JsArray(Seq(JsString(theme2Colors("lightGrey")), JsNumber(1))),
      ImGuiCol.ScrollbarBg.id -> JsArray(Seq(JsString(theme2Colors("darkerGrey")), JsNumber(1))),
      ImGuiCol.ScrollbarGrab.id -> JsArray(Seq(JsString(theme2Colors("darkerGrey")), JsNumber(1))),
      ImGuiCol.ScrollbarGrabHovered.id -> JsArray(Seq(JsString(theme2Colors("lightGrey")), JsNumber(1))),
      ImGuiCol.ScrollbarGrabActive.id -> JsArray(Seq(JsString(theme2Colors("darkestGrey")), JsNumber(1))),
      ImGuiCol.CheckMark.id -> JsArray(Seq(JsString(theme2Colors("darkestGrey")), JsNumber(1))),
      ImGuiCol.SliderGrab.id -> JsArray(Seq(JsString(theme2Colors("darkerGrey")), JsNumber(1))),
      ImGuiCol.SliderGrabActive.id -> JsArray(Seq(JsString(theme2Colors("lightGrey")), JsNumber(1))),
      ImGuiCol.Button.id -> JsArray(Seq(JsString(theme2Colors("black")), JsNumber(1))),
      ImGuiCol.ButtonHovered.id -> JsArray(Seq(JsString(theme2Colors("darkerGrey")), JsNumber(1))),
      ImGuiCol.ButtonActive.id -> JsArray(Seq(JsString(theme2Colors("black")), JsNumber(1))),
      ImGuiCol.Header.id -> JsArray(Seq(JsString(theme2Colors("black")), JsNumber(1))),
      ImGuiCol.HeaderHovered.id -> JsArray(Seq(JsString(theme2Colors("black")), JsNumber(1))),
      ImGuiCol.HeaderActive.id -> JsArray(Seq(JsString(theme2Colors("lightGrey")), JsNumber(1))),
      ImGuiCol.Separator.id -> JsArray(Seq(JsString(theme2Colors("darkestGrey")), JsNumber(1))),
      ImGuiCol.SeparatorHovered.id -> JsArray(Seq(JsString(theme2Colors("lightGrey")), JsNumber(1))),
      ImGuiCol.SeparatorActive.id -> JsArray(Seq(JsString(theme2Colors("lightGrey")), JsNumber(1))),
      ImGuiCol.ResizeGrip.id -> JsArray(Seq(JsString(theme2Colors("black")), JsNumber(1))),
      ImGuiCol.ResizeGripHovered.id -> JsArray(Seq(JsString(theme2Colors("lightGrey")), JsNumber(1))),
      ImGuiCol.ResizeGripActive.id -> JsArray(Seq(JsString(theme2Colors("darkerGrey")), JsNumber(1))),
      ImGuiCol.Tab.id -> JsArray(Seq(JsString(theme2Colors("black")), JsNumber(1))),
      ImGuiCol.TabHovered.id -> JsArray(Seq(JsString(theme2Colors("darkerGrey")), JsNumber(1))),
      ImGuiCol.TabActive.id -> JsArray(Seq(JsString(theme2Colors("lightGrey")), JsNumber(1))),
      ImGuiCol.TabUnfocused.id -> JsArray(Seq(JsString(theme2Colors("black")), JsNumber(1))),
      ImGuiCol.TabUnfocusedActive.id -> JsArray(Seq(JsString(theme2Colors("lightGrey")), JsNumber(1))),
      ImGuiCol.PlotLines.id -> JsArray(Seq(JsString(theme2Colors("darkerGrey")), JsNumber(1))),
      ImGuiCol.PlotLinesHovered.id -> JsArray(Seq(JsString(theme2Colors("lightGrey")), JsNumber(1))),
      ImGuiCol.PlotHistogram.id -> JsArray(Seq(JsString(theme2Colors("darkerGrey")), JsNumber(1))),
      ImGuiCol.PlotHistogramHovered.id -> JsArray(Seq(JsString(theme2Colors("lightGrey")), JsNumber(1))),
      ImGuiCol.TableHeaderBg.id -> JsArray(Seq(JsString(theme2Colors("black")), JsNumber(1))),
      ImGuiCol.TableBorderStrong.id -> JsArray(Seq(JsString(theme2Colors("lightGrey")), JsNumber(1))),
      ImGuiCol.TableBorderLight.id -> JsArray(Seq(JsString(theme2Colors("darkerGrey")), JsNumber(1))),
      ImGuiCol.TableRowBg.id -> JsArray(Seq(JsString(theme2Colors("darkGrey")), JsNumber(1))),
      ImGuiCol.TableRowBgAlt.id -> JsArray(Seq(JsString(theme2Colors("darkerGrey")), JsNumber(1))),
      ImGuiCol.TextSelectedBg.id -> JsArray(Seq(JsString(theme2Colors("darkerGrey")), JsNumber(1))),
      ImGuiCol.DragDropTarget.id -> JsArray(Seq(JsString(theme2Colors("darkerGrey")), JsNumber(1))),
      ImGuiCol.NavHighlight.id -> JsArray(Seq(JsString(theme2Colors("darkerGrey")), JsNumber(1))),
      ImGuiCol.NavWindowingHighlight.id -> JsArray(Seq(JsString(theme2Colors("darkerGrey")), JsNumber(1))),
      ImGuiCol.NavWindowingDimBg.id -> JsArray(Seq(JsString(theme2Colors("darkerGrey")), JsNumber(1))),
      ImGuiCol.ModalWindowDimBg.id -> JsArray(Seq(JsString(theme2Colors("darkerGrey")), JsNumber(1)))
    )

    // Create the theme JSON object
    val theme2Json = Json.obj(
      "colors" -> JsObject(colorMap.map {
        case (key, value) => (key.toString, value)  // Convert Int key to String
      })
    )

    // Return the JSON string
    theme2Json.toString()
  }
}


// Enum for ImGui colors
object ImGuiCol extends Enumeration {
  val Text: Value = Value(0)
  val TextDisabled: Value = Value(1)
  val WindowBg: Value = Value(2)
  val ChildBg: Value = Value(3)
  val PopupBg: Value = Value(4)
  val Border: Value = Value(5)
  val BorderShadow: Value = Value(6)
  val FrameBg: Value = Value(7)
  val FrameBgHovered: Value = Value(8)
  val FrameBgActive: Value = Value(9)
  val TitleBg: Value = Value(10)
  val TitleBgActive: Value = Value(11)
  val TitleBgCollapsed: Value = Value(12)
  val MenuBarBg: Value = Value(13)
  val ScrollbarBg: Value = Value(14)
  val ScrollbarGrab: Value = Value(15)
  val ScrollbarGrabHovered: Value = Value(16)
  val ScrollbarGrabActive: Value = Value(17)
  val CheckMark: Value = Value(18)
  val SliderGrab: Value = Value(19)
  val SliderGrabActive: Value = Value(20)
  val Button: Value = Value(21)
  val ButtonHovered: Value = Value(22)
  val ButtonActive: Value = Value(23)
  val Header: Value = Value(24)
  val HeaderHovered: Value = Value(25)
  val HeaderActive: Value = Value(26)
  val Separator: Value = Value(27)
  val SeparatorHovered: Value = Value(28)
  val SeparatorActive: Value = Value(29)
  val ResizeGrip: Value = Value(30)
  val ResizeGripHovered: Value = Value(31)
  val ResizeGripActive: Value = Value(32)
  val Tab: Value = Value(33)
  val TabHovered: Value = Value(34)
  val TabActive: Value = Value(35)
  val TabUnfocused: Value = Value(36)
  val TabUnfocusedActive: Value = Value(37)
  val PlotLines: Value = Value(38)
  val PlotLinesHovered: Value = Value(39)
  val PlotHistogram: Value = Value(40)
  val PlotHistogramHovered: Value = Value(41)
  val TableHeaderBg: Value = Value(42)
  val TableBorderStrong: Value = Value(43)
  val TableBorderLight: Value = Value(44)
  val TableRowBg: Value = Value(45)
  val TableRowBgAlt: Value = Value(46)
  val TextSelectedBg: Value = Value(47)
  val DragDropTarget: Value = Value(48)
  val NavHighlight: Value = Value(49)
  val NavWindowingHighlight: Value = Value(50)
  val NavWindowingDimBg: Value = Value(51)
  val ModalWindowDimBg: Value = Value(52)

  val COUNT = Value(53)
}
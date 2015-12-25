import util.JsonUtil

import scala.util.parsing.json.JSON

/**
 * Created by root on 15-11-19.
 */
object TestJson {
	def main(args: Array[String]) {
		val test = "3   4   5 a b   c   d"
		println(test.span(_ != "\t")._2)
		val json = "{\"userId\":\"8f946d49991f2457e3ec1003fecb5774\",\"data\":{\"fondIds\":[29,30,31,56,55,54,53,24,25,32,23,106,107,108]}}"
		val s = JSON.parseFull(json)
		val t = JsonUtil.jsonToMap(json)
		println(t.get("8f946d49991f2457e3ec1003fecb5774"))
		try {
			val data:String = s match {
				case Some(map: Map[String, Any]) => {
					//get.asInstanceOf[Map[String, String]]
					val fondIds = map.get("data") match {
						case Some(m: Map[String, Any]) => {
							m.get("fondIds") match {
								case Some(m2: List[Int]) => m2(2)
							}
						}
					}
					println(map.get("data"))
					""
				}
			}
		}catch {
			case e => ""
		}
	}
}


import java.io.{BufferedReader, FileReader}

import com.github.sterkh.spatial.{QuadTree, Shape}
import com.opencsv.CSVReader
import org.locationtech.jts.io.WKTReader


case class City(city: String, state_id: String, state_name: String, county: String, lat: Float, lng: Float)

def getCities() = {

  import collection.JavaConverters._

  val bufReader = new BufferedReader(new FileReader("/Users/yuri/Documents/wkt/us_cities.csv"))
  val csvReader = new CSVReader(bufReader, ',', '\"', 1)

  val lines: List[Array[String]] = csvReader.readAll().asScala.toList

  val wkt = new WKTReader()

  val cities = lines.map(line => {
    City(line(0), line(2), line(3), line(5), line(6).toFloat, line(7).toFloat)
  })

  cities
}

val t0 = System.nanoTime()
val qt = new QuadTree[String]()
val qti = qt.createIndex("/Users/yuri/Documents/wkt/us_states.csv",
  3, 0, 10, List.empty[Int], ';')
val t1 = System.nanoTime()
val cities = getCities()

println("Number of cities: " + cities.length)

cities.foreach {
  c => {
    val res = qti.query(c.lng, c.lat)
    //    println((c.city, c.state_name, qti.query(c.lng, c.lat)))

    res.headOption match {
      case Some(x) if x != c.state_name => println((c.city, c.state_name, x))
      case _ =>
    }
  }
}
val t2 = System.nanoTime()

println("Create QuadTree index time: " + ((t1 - t0) / 1000000000.0)  + " s")
println("Query QuadTree index time: " + ((t2 - t1) / 1000000000.0) + " s")

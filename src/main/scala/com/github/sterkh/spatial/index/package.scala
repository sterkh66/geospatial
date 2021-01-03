package com.github.sterkh.spatial

import org.locationtech.jts.geom.Geometry

package object index {

  case class Shape[T](id: T, geometry: Geometry)

}
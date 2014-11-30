package models

import java.util.UUID

case class Product(name: String, price: BigDecimal, quantity: Int, id: String = UUID.randomUUID().toString)
  extends WithId
package models

case class Order(products: List[OrderedProduct])

case class OrderedProduct(productId: String, preOrderedQuantity: Int)
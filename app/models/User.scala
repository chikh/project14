package models

case class User(
                 name: String,
                 password: String
                 )
  extends WithId {
  def id: String = name
}
package models

case class User(
                 email: String,
                 password: String
                 )
  extends WithId {
  def id: String = email
}
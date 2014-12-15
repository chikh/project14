import com.google.inject.{Guice, AbstractModule}
import mappers.{DbEmulatorCRUDMapper, CRUDController}
import play.api.GlobalSettings

object Global extends GlobalSettings {

  val injector = Guice.createInjector(new AbstractModule {
    protected def configure() {
      bind(classOf[CRUDController]).to(classOf[DbEmulatorCRUDMapper])
    }
  })

  override def getControllerInstance[A](controllerClass: Class[A]): A = injector.getInstance(controllerClass)
}

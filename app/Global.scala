import com.google.inject.{Guice, AbstractModule}
import play.api.GlobalSettings

object Global extends GlobalSettings {

  val injector = Guice.createInjector(new AbstractModule {
    protected def configure() {
      // bind(classOf[TextGenerator]).to(classOf[WelcomeTextGenerator])
    }
  })

  override def getControllerInstance[A](controllerClass: Class[A]): A = injector.getInstance(controllerClass)
}

package overclocking.jrobocontainer.exceptions;

public class AmbiguousConstructorException extends JRoboContainerException
{

    public <T> AmbiguousConstructorException(Class<T> clazz)
    {
        super(clazz + " has many constructors and no one marked as @ContainerConstructor");
    }
}

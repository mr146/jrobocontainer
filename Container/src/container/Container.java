package container;

import classloader.IClassLoaderConfiguration;
import classloader.JRoboClassLoader;
import configurations.BindedImplementationConfiguration;
import configurations.BindedInstanceConfiguration;
import classloaderconfigurations.DefaultClassLoaderConfiguration;
import storage.IStorage;
import storage.Storage;

import java.util.HashSet;

public class Container implements IContainer
{

    private JRoboClassLoader classLoader;
    private IStorage storage;

    public Container()
    {
        this(new DefaultClassLoaderConfiguration());
    }

    public Container(IClassLoaderConfiguration classLoaderConfiguration)
    {
        storage = new Storage();
        classLoader = new JRoboClassLoader(storage, classLoaderConfiguration);
        classLoader.loadClasses();
        storage.buildExtendedInheritanceGraph();
    }

    @Override
    public <T> T get(Class<T> requiredAbstraction)
    {
        return storage.getConfiguration(requiredAbstraction).get(new HashSet<Class<?>>());
    }

    @Override
    public <T> T create(Class<T> requiredAbstraction)
    {
        return storage.getConfiguration(requiredAbstraction).create(new HashSet<Class<?>>());
    }

    @Override
    public <T1, T2 extends T1> void bindInstance(Class<T1> abstraction, T2 instance)
    {
        storage.setConfiguration(abstraction, new BindedInstanceConfiguration(storage, abstraction, instance));
    }

    @Override
    public <T1, T2 extends T1> void bindImplementation(Class<T1> abstraction, Class<T2> bindedImplementation)
    {
        storage.setConfiguration(abstraction, new BindedImplementationConfiguration(storage, abstraction, bindedImplementation));
    }

    @Override
    public <T> T[] getAll(Class<T> requiredAbstraction)
    {
        return storage.getConfiguration(requiredAbstraction).getAll(new HashSet<Class<?>>());
    }
}
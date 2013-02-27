package classloader;

import storage.IStorage;

import java.io.File;

public class JRoboClassLoader
{

    String classPath;
    private IClassLoaderConfiguration filter;
    EntitiesWalker directoriesWalker;

    public JRoboClassLoader(IStorage storage, IClassLoaderConfiguration classLoaderConfiguration)
    {
        this.classPath = classLoaderConfiguration.getClassPaths();
        this.filter = classLoaderConfiguration;
        this.directoriesWalker = new EntitiesWalker(storage, classLoaderConfiguration);
    }

    public void loadClasses()
    {
        for (String path : classPath.split(System.getProperty("path.separator")))
        {
            directoriesWalker.addFolder(new File(path));
        }
    }

}
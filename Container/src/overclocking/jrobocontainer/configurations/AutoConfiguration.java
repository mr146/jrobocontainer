package overclocking.jrobocontainer.configurations;

import overclocking.jrobocontainer.container.AbstractionInstancePair;
import overclocking.jrobocontainer.exceptions.JroboContainerException;
import overclocking.jrobocontainer.injectioncontext.IInjectionContext;
import overclocking.jrobocontainer.storages.ClassNode;
import overclocking.jrobocontainer.storages.IClassNodesStorage;
import overclocking.jrobocontainer.storages.IStorage;

public class AutoConfiguration extends AbstractConfiguration
{
    private Object instance;

    public AutoConfiguration(IStorage storage, IClassNodesStorage classNodesStorage, String abstractionId)
    {
        super(storage, classNodesStorage);
        this.abstractionId = abstractionId;
        this.instance = null;
    }

    public <T> T innerGet(IInjectionContext injectionContext, ClassLoader classLoader)
    {

        ClassNode abstraction = classNodesStorage.getClassNodeById(abstractionId);
        try
        {
            if (instance != null)
            {
                injectionContext.reuse(abstraction);
                return (T) instance;
            }
            injectionContext.beginCreate(abstraction);
            if (abstraction.isFactory())
            {
                instance = factoryCreator.createFactory(abstraction.getClazz());
            } else
            {
                String resolvedClassId = resolveClass(abstractionId, storage.getImplementations(abstractionId), classNodesStorage);
                synchronized (storage.getSynchronizeObject(resolvedClassId))
                {
                    if (resolvedClassId.equals(abstractionId))
                    {
                        instance = getInstance(resolvedClassId, injectionContext, classLoader, null);
                    } else
                    {
                        instance = storage.getConfiguration(resolvedClassId).get(injectionContext, classLoader);
                    }
                }
            }
            injectionContext.endCreate(abstraction);
            return (T) instance;
        }
        catch (JroboContainerException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new JroboContainerException("Failed to get " + abstraction.getClassName(), injectionContext, ex);
        }
    }

    public <T> T innerCreate(IInjectionContext injectionContext, ClassLoader classLoader, AbstractionInstancePair[] substitutions)
    {
        ClassNode abstraction = classNodesStorage.getClassNodeById(abstractionId);
        if (abstraction.isFactory())
            return (T) factoryCreator.createFactory(abstraction.getClazz());
        try
        {
            String resolvedClassId = resolveClass(abstractionId, storage.getImplementations(abstractionId), classNodesStorage);
            return (T) getInstance(resolvedClassId, injectionContext, classLoader, substitutions);
        }
        catch (JroboContainerException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            throw new JroboContainerException("Failed to create " + abstraction.getClassName(), ex);
        }
    }
}

package com.synlabs.ipsaa.jpa.interceptor;

import com.synlabs.ipsaa.entity.common.BaseEntity;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.SPACE;

public class EntityInterceptor extends EmptyInterceptor
{
  private static final Set<String> IGNORED_PROPERTIES = new HashSet<String>()
  {
    {
      this.add("createdAt");
      this.add("createdBy");
      this.add("id");
      this.add("modifiedAt");
      this.add("modifiedBy");
      this.add("version");
    }
  };
  private static final String      UNDERSCORE         = "_";

  private Set<Object>     audited = new HashSet<>();
  private Set<BaseEntity> changes = new HashSet<>();

  public void onDelete(final Object entity
      , final Serializable id
      , final Object[] state
      , final String[] propertyNames
      , final Type[] types)
  {
    if (isAuditable(entity))
    {
      final BaseEntity log = ((IPropertyChangeAuditable) entity).auditRemoval();

      if (log != null)
      {
        changes.add(log);
        audited.add(entity);
      }
    }
  }

  public boolean onFlushDirty(final Object entity
      , final Serializable id
      , final Object[] currentState
      , final Object[] previousState
      , final String[] propertyNames
      , final Type[] types)
  {
    if (isAuditable(entity))
    {
      for (int i = 0; i < propertyNames.length; ++i)
      {
        if (!types[i].isCollectionType()
            && !IGNORED_PROPERTIES.contains(propertyNames[i])
            && areNotEqual(currentState[i], previousState[i]))
        {
          final BaseEntity log = ((IPropertyChangeAuditable) entity).auditPropertyChange(propertyNames[i]
              , getObjectValue(previousState[i])
              , getObjectValue(currentState[i]));

          if (log != null)
          {
            changes.add(log);
            audited.add(entity);
          }
        }
      }
    }

    return false;
  }

  public boolean onSave(final Object entity
      , final Serializable id
      , final Object[] state
      , final String[] propertyNames
      , final Type[] types)
  {
    if (isAuditable(entity))
    {
      final BaseEntity log = ((IPropertyChangeAuditable) entity).auditAddition();

      if (log != null)
      {
        changes.add(log);
        audited.add(entity);
      }
    }

    return false;
  }

  private String getObjectValue(final Object object)
  {
    if (object == null)
    {
      return null;
    }

    if (object instanceof IAuditedProperty)
    {
      return ((IAuditedProperty) object).getAuditValue();
    }

    if (object instanceof Date)
    {
      return ((Date) object).toString();
    }

    if (object instanceof Enum<?>)
    {
      return toPrintableName((Enum<?>) object);
    }

    return object.toString();
  }

  public void postFlush(final Iterator entities)
  {
    if (changes == null || changes.isEmpty())
    {
      return;
    }

    // Obtain a JPA EntityManager to record audit logs.
    final EntityManager entityManager = SpringDataContext.getBean(EntityManager.class);

    try
    {
      changes.forEach(entityManager::persist);

      changes.clear();
      audited.clear();

      entityManager.flush();
    }
    catch (final Throwable t)
    {
      // Ignore the exception as this is just an auditing action.
    }
    finally
    {
      changes.clear();
      audited.clear();
    }
  }

  private boolean isAuditable(Object entity)
  {
    return entity instanceof IPropertyChangeAuditable
        && !audited.contains(entity);
  }

  public static boolean areEqual(final Object first, final Object second)
  {
    return (first == second)
        || ((first != null) && (second != null) && first.equals(second));
  }

  public static boolean areNotEqual(final Object first, final Object second)
  {
    return !areEqual(first, second);
  }

  public static <T extends Enum<?>> String toPrintableName(final T value)
  {
    return value != null
           ? StringUtils.capitalize(value.name().replaceAll(UNDERSCORE, SPACE))
           : null;
  }
}

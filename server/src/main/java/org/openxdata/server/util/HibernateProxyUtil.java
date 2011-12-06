package org.openxdata.server.util;

/*
 * Copyright 2008, Maher Kilani
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see .
 */
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.proxy.HibernateProxy;
import org.openxdata.server.admin.model.Editable;

/**
 * This class is implemented to solve the hibernate lazy loading problem with
 * OXD which doesnt support lazy loading in the meantime.
 *
 * Used when returning objects requested by external clients through the 
 * web services.
 * 
 */
public class HibernateProxyUtil {

    private static Logger log = Logger.getLogger(HibernateProxyUtil.class);

    /**
     * This function would take as a prameter any kind of object and recursively
     * access all of its member and clean it from any uninitialized variables.
     * The function will stop the recursion if the member variable is not of type
     * baseBean (defined in the application) and if not of type collection
     *
     * @param listObj
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    @SuppressWarnings("unchecked")
    public static void cleanObject(Object listObj, HashSet visitedBeansSet) throws IllegalArgumentException, IllegalAccessException,
            ClassNotFoundException, InstantiationException, InvocationTargetException {
        if (visitedBeansSet == null)
            visitedBeansSet = new HashSet();
        if (listObj == null)
            return;

        // to handle the case of abnormal return consisting of array Object
        // case if hybrid bean
        if (listObj instanceof Object[]) {
            Object[] objArray = (Object[]) listObj;
            for (int z = 0; z < objArray.length; z++) {
                cleanObject(objArray[z], visitedBeansSet);
            }
        } else {

            Iterator itOn = null;

            if (listObj instanceof List) {
                itOn = ((List) listObj).iterator();
            } else if (listObj instanceof Set) {
                itOn = ((Set) listObj).iterator();
            } else if (listObj instanceof Map) {
                itOn = ((Map) listObj).values().iterator();
            }

            if (itOn != null) {
                while (itOn.hasNext()) {
                    cleanObject(itOn.next(), visitedBeansSet);
                }
            } else {
                if (!visitedBeansSet.contains(listObj)) {
                    visitedBeansSet.add(listObj);
                    processBean(listObj, visitedBeansSet);
                }

            }
        }
    }

    /**
     * Remove the un-initialized proxies from the given object
     *
     * @param objBean
     * @throws Exception
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    @SuppressWarnings("unchecked")
    private static void processBean(Object objBean, HashSet visitedBeans) throws IllegalAccessException, IllegalArgumentException,
            ClassNotFoundException, InstantiationException, InvocationTargetException {
        Class tmpClass = objBean.getClass();
        Field[] classFields = null;
        while (tmpClass != null && tmpClass != Editable.class && tmpClass != Object.class) {
            classFields = tmpClass.getDeclaredFields();
            cleanFields(objBean, classFields, visitedBeans);
            tmpClass = tmpClass.getSuperclass();
        }
    }

    @SuppressWarnings("unchecked")
    private static void cleanFields(Object objBean, Field[] classFields, HashSet visitedBeans) throws ClassNotFoundException,
            IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException {
        boolean accessModifierFlag = false;
        for (int z = 0; z < classFields.length; z++) {
            Field field = classFields[z];
            accessModifierFlag = false;
            if (!field.isAccessible()) {
                field.setAccessible(true);
                accessModifierFlag = true;
            }

            Object fieldValue = field.get(objBean);

            if (fieldValue instanceof HibernateProxy) {
                String className = ((HibernateProxy) fieldValue).getHibernateLazyInitializer().getEntityName();
                Class clazz = Class.forName(className);
                Class[] constArgs = {Integer.class};
                Constructor construct = null;
                Editable baseBean = null;

                try {
                    construct = clazz.getConstructor(constArgs);
                } catch (NoSuchMethodException e) {
                    log.info("No such method for base bean " + className);
                }

                if (construct != null) {
                    baseBean = (Editable) construct.newInstance((Integer) ((HibernateProxy) fieldValue).getHibernateLazyInitializer().getIdentifier());
                }
                field.set(objBean, baseBean);
            } else {
                if (fieldValue instanceof org.hibernate.collection.PersistentCollection) {
           
                    if (!((org.hibernate.collection.PersistentCollection) fieldValue).wasInitialized())
                        field.set(objBean, null);
                    else {
                        cleanObject((fieldValue), visitedBeans);
                    }

                } else {
                    if (fieldValue instanceof Editable || fieldValue instanceof Collection)
                        cleanObject(fieldValue, visitedBeans);
                }
            }
            if (accessModifierFlag)
                field.setAccessible(false);
        }
    }

	public static void cleanObject(Object users) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, InvocationTargetException {
		cleanObject(users, null);
	}
}
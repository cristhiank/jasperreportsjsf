/*
 * JaspertReports JSF Plugin Copyright (C) 2011 A. Alonso Dominguez
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version. This library is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details. You should have
 * received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA A.
 *
 * Alonso Dominguez
 * alonsoft@users.sf.net
 */
package net.sf.jasperreports.jsf.test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jmock.Mockery;
import org.junit.experimental.theories.Theories;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 *
 * @author antonio.alonso
 */
public class JMockTheories extends Theories {

    private static final Logger logger = Logger.getLogger(
            JMockTheories.class.getPackage().getName());

    private final Field mockeryField;

    public JMockTheories(Class<?> testClass) throws InitializationError {
        super(testClass);
        mockeryField = findMockeryField(testClass);
        mockeryField.setAccessible(true);
    }

    @Override
    protected List<MethodRule> rules(Object test) {
        List<MethodRule> list = super.rules(test);
        list.add(new AssertionSatisfiedRule());
        return list;
    }

    protected Mockery mockeryOf(Object test) {
        try {
            Mockery mockery = (Mockery)mockeryField.get(test);
            if (mockery == null) {
                throw new IllegalStateException("Mockery named '"
                    + mockeryField.getName() + "' is null");
            }
            return mockery;
        }
        catch (IllegalAccessException e) {
            throw new IllegalStateException("cannot get value of field "
                + mockeryField.getName(), e);
        }
    }

    private void assertIsSatisfied(Object test) {
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, "Checking mockery expectations.");
        }
        mockeryOf(test).assertIsSatisfied();
    }

    public class AssertionSatisfiedRule implements MethodRule {

        public Statement apply(final Statement base,
                final FrameworkMethod method, final Object target) {
            return new Statement() {
                public void evaluate() throws Throwable {
                    try {
                        base.evaluate();
                        assertIsSatisfied(target);
                    } catch (Throwable e) {
                        assertIsSatisfied(target);
                        throw e;
                    }
                }
            };
        }

    }

    static Field findMockeryField(Class<?> testClass) throws InitializationError {
        for (Class<?> c = testClass; c != Object.class; c = c.getSuperclass()) {
            for (Field field: c.getDeclaredFields()) {
                if (Mockery.class.isAssignableFrom(field.getType())) {
                    return field;
                }
            }
        }

        throw new InitializationError("no Mockery found in test class "
            + testClass);
    }

}

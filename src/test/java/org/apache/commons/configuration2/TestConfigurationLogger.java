/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.configuration2;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.NoOpLog;
import org.easymock.EasyMock;
import org.junit.Test;

/**
 * Test class for {@code ConfigurationLogger}.
 *
 * @version $Id$
 */
public class TestConfigurationLogger
{
    /** Constant for a message to be logged. */
    private static final String MSG = "Interesting log output";

    /**
     * Tries to create an instance without passing in a logger name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInitNoLoggerName()
    {
        new ConfigurationLogger((String) null);
    }

    /**
     * Tries to create an instance without passing a logger class.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInitNoLoggerClass()
    {
        new ConfigurationLogger((Class<?>) null);
    }

    /**
     * Tests whether a correct internal logger is created.
     */
    @Test
    public void testInitWithLoggerSpec()
    {
        ConfigurationLogger logger1 =
                new ConfigurationLogger(getClass().getName());
        ConfigurationLogger logger2 = new ConfigurationLogger(getClass());

        assertNotNull("No internal logger", logger1.getLog());
        assertEquals("Different internal loggers", logger1.getLog(),
                logger2.getLog());
    }

    /**
     * Tests whether the debug status can be queried.
     */
    @Test
    public void testIsDebugEnabled()
    {
        Log log = EasyMock.createMock(Log.class);
        EasyMock.expect(log.isDebugEnabled()).andReturn(Boolean.TRUE);
        EasyMock.replay(log);
        ConfigurationLogger logger = new ConfigurationLogger(log);

        assertTrue("No debug log", logger.isDebugEnabled());
        EasyMock.verify(log);
    }

    /**
     * Tests whether the info status can be queried.
     */
    @Test
    public void testIsInfoEnabled()
    {
        Log log = EasyMock.createMock(Log.class);
        EasyMock.expect(log.isInfoEnabled()).andReturn(Boolean.FALSE);
        EasyMock.replay(log);
        ConfigurationLogger logger = new ConfigurationLogger(log);

        assertFalse("Wrong info log", logger.isInfoEnabled());
        EasyMock.verify(log);
    }

    /**
     * Tests whether debug logging is possible.
     */
    @Test
    public void testDebug()
    {
        Log log = EasyMock.createMock(Log.class);
        log.debug(MSG);
        EasyMock.replay(log);
        ConfigurationLogger logger = new ConfigurationLogger(log);

        logger.debug(MSG);
        EasyMock.verify(log);
    }

    /**
     * Tests whether info logging is possible.
     */
    @Test
    public void testInfo()
    {
        Log log = EasyMock.createMock(Log.class);
        log.info(MSG);
        EasyMock.replay(log);
        ConfigurationLogger logger = new ConfigurationLogger(log);

        logger.info(MSG);
        EasyMock.verify(log);
    }

    /**
     * Tests whether warn logging is possible.
     */
    @Test
    public void testWarn()
    {
        Log log = EasyMock.createMock(Log.class);
        log.warn(MSG);
        EasyMock.replay(log);
        ConfigurationLogger logger = new ConfigurationLogger(log);

        logger.warn(MSG);
        EasyMock.verify(log);
    }

    /**
     * Tests whether an exception can be logged on warn level.
     */
    @Test
    public void testWarnWithException()
    {
        Log log = EasyMock.createMock(Log.class);
        Throwable ex = new Exception("Test exception");
        log.warn(MSG, ex);
        EasyMock.replay(log);
        ConfigurationLogger logger = new ConfigurationLogger(log);

        logger.warn(MSG, ex);
        EasyMock.verify(log);
    }

    /**
     * Tests whether error logging is possible.
     */
    @Test
    public void testError()
    {
        Log log = EasyMock.createMock(Log.class);
        log.error(MSG);
        EasyMock.replay(log);
        ConfigurationLogger logger = new ConfigurationLogger(log);

        logger.error(MSG);
        EasyMock.verify(log);
    }

    /**
     * Tests whether an exception can be logged on error level.
     */
    @Test
    public void testErrorWithException()
    {
        Log log = EasyMock.createMock(Log.class);
        Throwable ex = new Exception("Test exception");
        log.error(MSG, ex);
        EasyMock.replay(log);
        ConfigurationLogger logger = new ConfigurationLogger(log);

        logger.error(MSG, ex);
        EasyMock.verify(log);
    }

    /**
     * Tests whether a dummy logger can be created.
     */
    @Test
    public void testDummyLogger()
    {
        ConfigurationLogger logger = ConfigurationLogger.newDummyLogger();

        assertThat("Wrong internal logger", logger.getLog(),
                instanceOf(NoOpLog.class));
    }

    /**
     * Tests that a derived class can be created for a logger.
     */
    @Test
    public void testSubClass()
    {
        final StringBuilder buf = new StringBuilder();
        ConfigurationLogger logger = new ConfigurationLogger()
        {
            @Override
            public void info(String msg)
            {
                buf.append(msg);
            }
        };

        assertNull("Got an internal logger", logger.getLog());
        logger.info(MSG);
        assertEquals("Message not logged", MSG, buf.toString());
    }
}

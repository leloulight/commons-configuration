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
package org.apache.commons.configuration2.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration2.ConfigurationDecoder;
import org.apache.commons.configuration2.ConfigurationLogger;
import org.apache.commons.configuration2.beanutils.BeanHelper;
import org.apache.commons.configuration2.convert.ConversionHandler;
import org.apache.commons.configuration2.convert.ListDelimiterHandler;
import org.apache.commons.configuration2.interpol.ConfigurationInterpolator;
import org.apache.commons.configuration2.interpol.InterpolatorSpecification;
import org.apache.commons.configuration2.interpol.Lookup;
import org.apache.commons.configuration2.sync.Synchronizer;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@code BasicBuilderParameters}.
 *
 * @version $Id$
 */
public class TestBasicBuilderParameters
{
    /** The instance to be tested. */
    private BasicBuilderParameters params;

    @Before
    public void setUp() throws Exception
    {
        params = new BasicBuilderParameters();
    }

    /**
     * Tests the default parameter values.
     */
    @Test
    public void testDefaults()
    {
        Map<String, Object> paramMap = params.getParameters();
        assertTrue("Got parameters", paramMap.isEmpty());
    }

    /**
     * Tests whether a defensive copy is created when the parameter map is
     * returned.
     */
    @Test
    public void testGetParametersDefensiveCopy()
    {
        Map<String, Object> map1 = params.getParameters();
        Map<String, Object> mapCopy = new HashMap<String, Object>(map1);
        map1.put("otherProperty", "value");
        Map<String, Object> map2 = params.getParameters();
        assertNotSame("Same map returned", map1, map2);
        assertEquals("Different properties", mapCopy, map2);
    }

    /**
     * Tests whether the logger parameter can be set.
     */
    @Test
    public void testSetLogger()
    {
        ConfigurationLogger log = EasyMock.createMock(ConfigurationLogger.class);
        EasyMock.replay(log);
        assertSame("Wrong result", params, params.setLogger(log));
        assertSame("Wrong logger parameter", log,
                params.getParameters().get("logger"));
    }

    /**
     * Tests whether the throw exception on missing property can be set.
     */
    @Test
    public void testSetThrowExceptionOnMissing()
    {
        assertSame("Wrong result", params,
                params.setThrowExceptionOnMissing(true));
        assertEquals("Wrong flag value", Boolean.TRUE, params.getParameters()
                .get("throwExceptionOnMissing"));
    }

    /**
     * Tests whether the list delimiter handler property can be set.
     */
    @Test
    public void testSetListDelimiter()
    {
        ListDelimiterHandler handler =
                EasyMock.createMock(ListDelimiterHandler.class);
        EasyMock.replay(handler);
        assertSame("Wrong result", params,
                params.setListDelimiterHandler(handler));
        assertSame("Wrong delimiter handler", handler, params.getParameters()
                .get("listDelimiterHandler"));
    }

    /**
     * Tests whether a {@code ConfigurationInterpolator} can be set.
     */
    @Test
    public void testSetInterpolator()
    {
        ConfigurationInterpolator ci =
                EasyMock.createMock(ConfigurationInterpolator.class);
        EasyMock.replay(ci);
        assertSame("Wrong result", params, params.setInterpolator(ci));
        assertSame("Wrong interpolator", ci,
                params.getParameters().get("interpolator"));
    }

    /**
     * Tests whether prefix lookups can be set.
     */
    @Test
    public void testSetPrefixLookups()
    {
        Lookup look = EasyMock.createMock(Lookup.class);
        Map<String, Lookup> lookups = Collections.singletonMap("test", look);
        assertSame("Wrong result", params, params.setPrefixLookups(lookups));
        Map<?, ?> map = (Map<?, ?>) params.getParameters().get("prefixLookups");
        assertNotSame("No copy was created", lookups, map);
        assertEquals("Wrong lookup", look, map.get("test"));
        assertEquals("Wrong number of lookups", 1, map.size());
        Map<?, ?> map2 = (Map<?, ?>) params.getParameters().get("prefixLookups");
        assertNotSame("No copy in parameters", map, map2);
    }

    /**
     * Tests whether null values are handled by setPrefixLookups().
     */
    @Test
    public void testSetPrefixLookupsNull()
    {
        params.setPrefixLookups(new HashMap<String, Lookup>());
        params.setPrefixLookups(null);
        assertFalse("Found key",
                params.getParameters().containsKey("prefixLookups"));
    }

    /**
     * Tests whether default lookups can be set.
     */
    @Test
    public void testSetDefaultLookups()
    {
        Lookup look = EasyMock.createMock(Lookup.class);
        Collection<Lookup> looks = Collections.singleton(look);
        assertSame("Wrong result", params, params.setDefaultLookups(looks));
        Collection<?> col =
                (Collection<?>) params.getParameters().get("defaultLookups");
        assertNotSame("No copy was created", col, looks);
        assertEquals("Wrong number of lookups", 1, col.size());
        assertSame("Wrong lookup", look, col.iterator().next());
        Collection<?> col2 =
                (Collection<?>) params.getParameters().get("defaultLookups");
        assertNotSame("No copy in parameters", col, col2);
    }

    /**
     * Tests whether null values are handled by setDefaultLookups().
     */
    @Test
    public void testSetDefaultLookupsNull()
    {
        params.setDefaultLookups(new ArrayList<Lookup>());
        params.setDefaultLookups(null);
        assertFalse("Found key",
                params.getParameters().containsKey("defaultLookups"));
    }

    /**
     * Tests whether a parent {@code ConfigurationInterpolator} can be set.
     */
    @Test
    public void testSetParentInterpolator()
    {
        ConfigurationInterpolator parent =
                EasyMock.createMock(ConfigurationInterpolator.class);
        EasyMock.replay(parent);
        assertSame("Wrong result", params, params.setParentInterpolator(parent));
        assertSame("Wrong parent", parent,
                params.getParameters().get("parentInterpolator"));
    }

    /**
     * Tests whether a custom {@code ConfigurationInterpolator} overrides
     * settings for custom lookups.
     */
    @Test
    public void testSetLookupsAndInterpolator()
    {
        Lookup look1 = EasyMock.createMock(Lookup.class);
        Lookup look2 = EasyMock.createMock(Lookup.class);
        ConfigurationInterpolator parent =
                EasyMock.createMock(ConfigurationInterpolator.class);
        ConfigurationInterpolator ci =
                EasyMock.createMock(ConfigurationInterpolator.class);
        params.setDefaultLookups(Collections.singleton(look1));
        params.setPrefixLookups(Collections.singletonMap("test", look2));
        params.setInterpolator(ci);
        params.setParentInterpolator(parent);
        Map<String, Object> map = params.getParameters();
        assertFalse("Got prefix lookups", map.containsKey("prefixLookups"));
        assertFalse("Got default lookups", map.containsKey("defaultLookups"));
        assertFalse("Got a parent interpolator",
                map.containsKey("parentInterpolator"));
    }

    /**
     * Tries a merge with a null object.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMergeNull()
    {
        params.merge(null);
    }

    /**
     * Tests whether properties of other parameter objects can be merged.
     */
    @Test
    public void testMerge()
    {
        ListDelimiterHandler handler1 = EasyMock.createMock(ListDelimiterHandler.class);
        ListDelimiterHandler handler2 = EasyMock.createMock(ListDelimiterHandler.class);
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("throwExceptionOnMissing", Boolean.TRUE);
        props.put("listDelimiterHandler", handler1);
        props.put("other", "test");
        props.put(BuilderParameters.RESERVED_PARAMETER_PREFIX + "test",
                "reserved");
        BuilderParameters p = EasyMock.createMock(BuilderParameters.class);
        EasyMock.expect(p.getParameters()).andReturn(props);
        EasyMock.replay(p);
        params.setListDelimiterHandler(handler2);
        params.merge(p);
        Map<String, Object> map = params.getParameters();
        assertEquals("Wrong list delimiter handler", handler2,
                map.get("listDelimiterHandler"));
        assertEquals("Wrong exception flag", Boolean.TRUE,
                map.get("throwExceptionOnMissing"));
        assertEquals("Wrong other property", "test", map.get("other"));
        assertFalse(
                "Reserved property was copied",
                map.containsKey(BuilderParameters.RESERVED_PARAMETER_PREFIX
                        + "test"));
    }

    /**
     * Tests whether a specification object for interpolation can be obtained.
     */
    @Test
    public void testFetchInterpolatorSpecification()
    {
        ConfigurationInterpolator parent =
                EasyMock.createMock(ConfigurationInterpolator.class);
        Lookup l1 = EasyMock.createMock(Lookup.class);
        Lookup l2 = EasyMock.createMock(Lookup.class);
        Lookup l3 = EasyMock.createMock(Lookup.class);
        Map<String, Lookup> prefixLookups = new HashMap<String, Lookup>();
        prefixLookups.put("p1", l1);
        prefixLookups.put("p2", l2);
        Collection<Lookup> defLookups = Collections.singleton(l3);
        params.setParentInterpolator(parent);
        params.setPrefixLookups(prefixLookups);
        params.setDefaultLookups(defLookups);
        Map<String, Object> map = params.getParameters();
        InterpolatorSpecification spec =
                BasicBuilderParameters.fetchInterpolatorSpecification(map);
        assertSame("Wrong parent", parent, spec.getParentInterpolator());
        assertEquals("Wrong prefix lookups", prefixLookups,
                spec.getPrefixLookups());
        assertEquals("Wrong number of default lookups", 1, spec
                .getDefaultLookups().size());
        assertTrue("Wrong default lookup", spec.getDefaultLookups()
                .contains(l3));
    }

    /**
     * Tests whether an InterpolatorSpecification can be fetched if a
     * ConfigurationInterpolator is present.
     */
    @Test
    public void testFetchInterpolatorSpecificationWithInterpolator()
    {
        ConfigurationInterpolator ci =
                EasyMock.createMock(ConfigurationInterpolator.class);
        params.setInterpolator(ci);
        InterpolatorSpecification spec =
                BasicBuilderParameters.fetchInterpolatorSpecification(params
                        .getParameters());
        assertSame("Wrong interpolator", ci, spec.getInterpolator());
        assertNull("Got a parent", spec.getParentInterpolator());
    }

    /**
     * Tests fetchInterpolatorSpecification() if the map contains a property of
     * an invalid data type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFetchInterpolatorSpecificationInvalidDataType()
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("interpolator", this);
        BasicBuilderParameters.fetchInterpolatorSpecification(map);
    }

    /**
     * Tests fetchInterpolatorSpecification() if the map with prefix lookups
     * contains an invalid key.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFetchInterpolatorSpecificationInvalidMapKey()
    {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<Object, Object> prefix = new HashMap<Object, Object>();
        prefix.put(42, EasyMock.createMock(Lookup.class));
        map.put("prefixLookups", prefix);
        BasicBuilderParameters.fetchInterpolatorSpecification(map);
    }

    /**
     * Tests fetchInterpolatorSpecification() if the map with prefix lookups
     * contains an invalid value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFetchInterpolatorSpecificationInvalidMapValue()
    {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<Object, Object> prefix = new HashMap<Object, Object>();
        prefix.put("test", this);
        map.put("prefixLookups", prefix);
        BasicBuilderParameters.fetchInterpolatorSpecification(map);
    }

    /**
     * Tests fetchInterpolatorSpecification() if the collection with default
     * lookups contains an invalid value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFetchInterpolatorSpecificationInvalidCollectionValue()
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("defaultLookups", Collections.singleton("not a lookup"));
        BasicBuilderParameters.fetchInterpolatorSpecification(map);
    }

    /**
     * Tests that an empty map does not cause any problems.
     */
    @Test
    public void testFetchInterpolatorSpecificationEmpty()
    {
        InterpolatorSpecification spec =
                BasicBuilderParameters.fetchInterpolatorSpecification(params
                        .getParameters());
        assertNull("Got an interpolator", spec.getInterpolator());
        assertTrue("Got lookups", spec.getDefaultLookups().isEmpty());
    }

    /**
     * Tries to obtain an {@code InterpolatorSpecification} from a null map.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFetchInterpolatorSpecificationNull()
    {
        BasicBuilderParameters.fetchInterpolatorSpecification(null);
    }

    /**
     * Tests whether a cloned instance contains the same data as the original
     * object.
     */
    @Test
    public void testCloneValues()
    {
        ConfigurationLogger log = EasyMock.createMock(ConfigurationLogger.class);
        ConfigurationInterpolator ci =
                EasyMock.createMock(ConfigurationInterpolator.class);
        ListDelimiterHandler handler1 = EasyMock.createMock(ListDelimiterHandler.class);
        ListDelimiterHandler handler2 = EasyMock.createMock(ListDelimiterHandler.class);
        params.setListDelimiterHandler(handler1);
        params.setLogger(log);
        params.setInterpolator(ci);
        params.setThrowExceptionOnMissing(true);
        BasicBuilderParameters clone = params.clone();
        params.setListDelimiterHandler(handler2);
        params.setThrowExceptionOnMissing(false);
        Map<String, Object> map = clone.getParameters();
        assertSame("Wrong logger", log, map.get("logger"));
        assertSame("Wrong interpolator", ci, map.get("interpolator"));
        assertEquals("Wrong list delimiter handler", handler1,
                map.get("listDelimiterHandler"));
        assertEquals("Wrong exception flag", Boolean.TRUE,
                map.get("throwExceptionOnMissing"));
    }

    /**
     * Tests whether the map with prefix lookups is cloned, too.
     */
    @Test
    public void testClonePrefixLookups()
    {
        Lookup look = EasyMock.createMock(Lookup.class);
        Map<String, Lookup> lookups = Collections.singletonMap("test", look);
        params.setPrefixLookups(lookups);
        BasicBuilderParameters clone = params.clone();
        Map<?, ?> map = (Map<?, ?>) params.getParameters().get("prefixLookups");
        map.clear();
        map = (Map<?, ?>) clone.getParameters().get("prefixLookups");
        assertEquals("Wrong number of lookups", 1, map.size());
        assertSame("Wrong lookup", look, map.get("test"));
    }

    /**
     * Tests whether the collection with default lookups can be cloned, too.
     */
    @Test
    public void testCloneDefaultLookups()
    {
        Lookup look = EasyMock.createMock(Lookup.class);
        Collection<Lookup> looks = Collections.singleton(look);
        params.setDefaultLookups(looks);
        BasicBuilderParameters clone = params.clone();
        Collection<?> defLooks =
                (Collection<?>) params.getParameters().get("defaultLookups");
        defLooks.clear();
        defLooks = (Collection<?>) clone.getParameters().get("defaultLookups");
        assertEquals("Wrong number of default lookups", 1, defLooks.size());
        assertTrue("Wrong default lookup", defLooks.contains(look));
    }

    /**
     * Tests whether a Synchronizer can be set.
     */
    @Test
    public void testSetSynchronizer()
    {
        Synchronizer sync = EasyMock.createMock(Synchronizer.class);
        EasyMock.replay(sync);
        assertSame("Wrong result", params, params.setSynchronizer(sync));
        assertSame("Synchronizer not set", sync,
                params.getParameters().get("synchronizer"));
    }

    /**
     * Tests whether a ConversionHandler can be set.
     */
    @Test
    public void testSetConversionHandler()
    {
        ConversionHandler handler =
                EasyMock.createMock(ConversionHandler.class);
        EasyMock.replay(handler);
        assertSame("Wrong result", params, params.setConversionHandler(handler));
        assertSame("ConversionHandler not set", handler, params.getParameters()
                .get("conversionHandler"));
    }

    /**
     * Tests whether a BeanHelper can be set.
     */
    @Test
    public void testSetBeanHelper()
    {
        BeanHelper helper = new BeanHelper();
        assertSame("Wrong result", params, params.setBeanHelper(helper));
        assertSame("BeanHelper not set", helper,
                BasicBuilderParameters.fetchBeanHelper(params.getParameters()));
    }

    /**
     * Tests fetchBeanHelper() if no helper was set.
     */
    @Test
    public void testFetchBeanHelperNoSet()
    {
        assertNull("Got a BeanHelper",
                BasicBuilderParameters.fetchBeanHelper(params.getParameters()));
    }

    /**
     * Tries to invoke fetchBeanHelper() on a null map.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFetchBeanHelperNullMap()
    {
        BasicBuilderParameters.fetchBeanHelper(null);
    }

    /**
     * Tests whether a decoder can be set.
     */
    @Test
    public void testSetConfigurationDecoder()
    {
        ConfigurationDecoder decoder =
                EasyMock.createMock(ConfigurationDecoder.class);
        EasyMock.replay(decoder);
        assertSame("Wrong result", params,
                params.setConfigurationDecoder(decoder));
        assertSame("Decoder not set", decoder,
                params.getParameters().get("configurationDecoder"));
    }
}

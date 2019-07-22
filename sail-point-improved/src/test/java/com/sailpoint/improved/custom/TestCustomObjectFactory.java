package com.sailpoint.improved.custom;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Custom;
import sailpoint.tools.GeneralException;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test for {@link CustomObjectFactory} class
 */
@RunWith(MockitoJUnitRunner.class)
public class TestCustomObjectFactory {

    /**
     * Test context
     */
    private SailPointContext context;

    /**
     * Init all properties for test
     */
    @Before
    public void init() {
        this.context = mock(SailPointContext.class);
    }

    /**
     * Testing creating new custom object via factory
     * Input:
     * - type of {@link TestSimpleCustomObject}
     * Expected:
     * Not null object
     */
    @Test
    public void createNewCustomObjectTest() throws GeneralException {
        assertNotNull(CustomObjectFactory.create(TestSimpleCustomObject.class));
    }

    /**
     * Testing loading custom object via current sailpoint context
     * Input:
     * - type of {@link TestSimpleCustomObject}
     * Expected:
     * - not null object
     * - calling getObjectByName in sailpoint context
     */
    @Test
    public void loadCustomObjectViaCurrentSailpointContextTest() throws GeneralException {
        SailPointFactory.popContext(this.context);
        assertNotNull(CustomObjectFactory.load(TestSimpleCustomObject.class));
        verify(this.context).getObjectByName(eq(Custom.class), anyString());
    }

    /**
     * Testing loading custom object via passed sailpoint context
     * Input:
     * - type of {@link TestSimpleCustomObject}
     * - sailpoint context instance
     * Expected:
     * - not null object
     * - calling getObjectByName in sailpoint context
     */
    @Test
    public void loadCustomObjectViaSailpointContextTest() throws GeneralException {
        SailPointFactory.popContext(this.context);
        assertNotNull(CustomObjectFactory.load(TestSimpleCustomObject.class));
        verify(this.context).getObjectByName(eq(Custom.class), anyString());
    }
}

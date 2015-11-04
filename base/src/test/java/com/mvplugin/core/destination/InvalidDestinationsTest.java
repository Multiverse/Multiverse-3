package com.mvplugin.core.destination;

import com.mvplugin.core.MultiverseCoreAPI;
import com.mvplugin.core.MultiverseCoreAPIFactory;
import com.mvplugin.core.exceptions.InvalidDestinationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * This class tests destination strings which refer to a valid destination type but are not strings that that
 * destination type will handle.
 */
@RunWith(Parameterized.class)
public class InvalidDestinationsTest {

    // The first arg in each pair is ignored.
    private static final String[][] invalidDestinations = {
            { "", "ca:some dest" },
            { "", "cannon:some dest" },
            { "", "cannon:world:x:y:z:pitch:yaw:speed" },
            { "", "cannon:someworld:5:5.3:5.4:3:3:" },
            { "", "cannon:someworld:5:5.3:5.4:3:3" },
            { "", "cannon: " },
            { "", "cannon:" },
            { "", "l:someworld:5:5.3" },
            { "", "l:someworld:5:5.3:" },
            { "", "l:someworld:5:5.3:5.4:3" },
            { "", "e:someworld:5:5.3:5.4:3:" },
            { "", "exact:world:x:y:z:pitch:yaw" },
            { "", "exact: " },
            { "", "exact:" },
            { "", "exact:blah blah" },
    };


    @Parameterized.Parameters
    public static Collection<String[]> params() {
        return Arrays.asList(invalidDestinations);
    }

    protected MultiverseCoreAPI api;
    protected DestinationRegistry registry;

    @Before
    public void setUp() throws Exception {
        registry = new DestinationRegistry(MultiverseCoreAPIFactory.getMockedMultiverseCoreAPI());
    }

    private String invalidDestinationString;

    public InvalidDestinationsTest(String ignore, String invalidDestinationString) {
        this.invalidDestinationString = invalidDestinationString;
    }

    @Test(expected = InvalidDestinationException.class)
    public void testInvalidDestination() throws Exception {
        registry.parseDestination(invalidDestinationString);
    }
}

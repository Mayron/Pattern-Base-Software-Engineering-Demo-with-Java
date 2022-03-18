package com.comp62542.checkout.singletons;

import junit.framework.TestCase;
import org.junit.jupiter.api.DisplayName;
import java.util.HashMap;
import java.util.Map;

public class CouponSingletonTests extends TestCase {

    @DisplayName("Test when calling getInstance twice then should return the same instance")
    public void testCouponsSingletonInstancesAreEqual() {
        // Act
        CouponsSingleton firstSingleton = CouponsSingleton.getInstance();
        CouponsSingleton secondSingleton = CouponsSingleton.getInstance();

        // Assert
        assertEquals("Instances of Singleton should be equal", firstSingleton, secondSingleton);
    }
}

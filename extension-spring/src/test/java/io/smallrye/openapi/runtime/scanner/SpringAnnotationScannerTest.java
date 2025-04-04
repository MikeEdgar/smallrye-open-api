package io.smallrye.openapi.runtime.scanner;

import java.io.IOException;

import org.eclipse.microprofile.openapi.models.OpenAPI;
import org.json.JSONException;
import org.junit.jupiter.api.Test;

import test.io.smallrye.openapi.runtime.scanner.entities.Greeting;
import test.io.smallrye.openapi.runtime.scanner.entities.GreetingParam;
import test.io.smallrye.openapi.runtime.scanner.resources.GreetingDeleteController;
import test.io.smallrye.openapi.runtime.scanner.resources.GreetingDeleteControllerAlt;
import test.io.smallrye.openapi.runtime.scanner.resources.GreetingGetController;
import test.io.smallrye.openapi.runtime.scanner.resources.GreetingGetControllerAlt;
import test.io.smallrye.openapi.runtime.scanner.resources.GreetingGetControllerAlt2;
import test.io.smallrye.openapi.runtime.scanner.resources.GreetingPostController;
import test.io.smallrye.openapi.runtime.scanner.resources.GreetingPostControllerAlt;
import test.io.smallrye.openapi.runtime.scanner.resources.GreetingPostControllerMultiplePaths;
import test.io.smallrye.openapi.runtime.scanner.resources.GreetingPutController;
import test.io.smallrye.openapi.runtime.scanner.resources.GreetingPutControllerAlt;
import test.io.smallrye.openapi.runtime.scanner.resources.GreetingPutControllerMultiplePaths;

/**
 * Basic Spring annotation scanning
 *
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
class SpringAnnotationScannerTest extends IndexScannerTestBase {

    /**
     * This test a basic, no OpenApi annotations, hello world service
     *
     * @throws IOException
     * @throws JSONException
     */
    @Test
    void testBasicGetSpringDefinitionScanning() throws IOException, JSONException {
        OpenAPI result = scan(GreetingGetController.class, Greeting.class, GreetingParam.class);
        assertJsonEquals("resource.testBasicSpringGetDefinitionScanning.json", result);
    }

    /**
     * This test a basic, no OpenApi annotations, hello world service
     *
     * Here we use the alternative RequestMapping rather than GetMapping
     *
     * @throws IOException
     * @throws JSONException
     */
    @Test
    void testBasicSpringDefinitionScanningAlt() throws IOException, JSONException {
        OpenAPI result = scan(GreetingGetControllerAlt.class, Greeting.class, GreetingParam.class);
        assertJsonEquals("resource.testBasicSpringGetDefinitionScanning.json", result);
    }

    /**
     * This test a basic, no OpenApi annotations, hello world service
     *
     * Here we use the alternative RequestMapping plus path rather than value
     *
     * @throws IOException
     * @throws JSONException
     */
    @Test
    void testBasicSpringDefinitionScanningAlt2() throws IOException, JSONException {
        OpenAPI result = scan(GreetingGetControllerAlt2.class, Greeting.class, GreetingParam.class);
        assertJsonEquals("resource.testBasicSpringGetDefinitionScanning.json", result);
    }

    /**
     * This test a basic, no OpenApi annotations, hello world service
     *
     * @throws IOException
     * @throws JSONException
     */
    @Test
    void testBasicPostSpringDefinitionScanning() throws IOException, JSONException {
        OpenAPI result = scan(GreetingPostController.class, Greeting.class, GreetingParam.class);
        assertJsonEquals("resource.testBasicSpringPostDefinitionScanning.json", result);
    }

    /**
     * This test a basic, no OpenApi annotations, hello world service
     *
     * @throws IOException
     * @throws JSONException
     */
    @Test
    void testBasicPostSpringDefinitionScanningAlt() throws IOException, JSONException {
        OpenAPI result = scan(GreetingPostControllerAlt.class, Greeting.class, GreetingParam.class);
        assertJsonEquals("resource.testBasicSpringPostDefinitionScanning.json", result);
    }

    /**
     * This tests multiple paths
     *
     * @throws IOException
     * @throws JSONException
     */
    @Test
    void testMultiplePathsPostSpringDefinitionScanningAlt() throws IOException, JSONException {
        OpenAPI result = scan(GreetingPostControllerMultiplePaths.class, Greeting.class, GreetingParam.class);
        assertJsonEquals("resource.testMultiplePathsSpringPostDefinitionScanning.json", result);
    }

    /**
     * This test a basic, no OpenApi annotations, hello world service
     *
     * @throws IOException
     * @throws JSONException
     */
    @Test
    void testBasicPostSpringDefinitionScanningWithServletContextJakarta() throws IOException, JSONException {
        OpenAPI result = scan(
                test.io.smallrye.openapi.runtime.scanner.resources.jakarta.GreetingPostControllerWithServletContext.class,
                Greeting.class);
        assertJsonEquals("resource.testBasicSpringPostDefinitionScanning.json", result);
    }

    /**
     * This test a basic, no OpenApi annotations, hello world service
     *
     * @throws IOException
     * @throws JSONException
     */
    @Test
    void testBasicPostSpringDefinitionScanningWithServletContextJavax() throws IOException, JSONException {
        OpenAPI result = scan(
                test.io.smallrye.openapi.runtime.scanner.resources.javax.GreetingPostControllerWithServletContext.class,
                Greeting.class);
        assertJsonEquals("resource.testBasicSpringPostDefinitionScanning.json", result);
    }

    /**
     * This test a basic, no OpenApi annotations, hello world service
     *
     * @throws IOException
     * @throws JSONException
     */
    @Test
    void testBasicPutSpringDefinitionScanning() throws IOException, JSONException {
        OpenAPI result = scan(GreetingPutController.class, Greeting.class);
        assertJsonEquals("resource.testBasicSpringPutDefinitionScanning.json", result);
    }

    /**
     * This test a basic, no OpenApi annotations, hello world service
     *
     * @throws IOException
     * @throws JSONException
     */
    @Test
    void testBasicPutSpringDefinitionScanningAlt() throws IOException, JSONException {
        OpenAPI result = scan(GreetingPutControllerAlt.class, Greeting.class);
        assertJsonEquals("resource.testBasicSpringPutDefinitionScanning.json", result);
    }

    /**
     * This tests multiple paths
     *
     * @throws IOException
     * @throws JSONException
     */
    @Test
    void testMultiplePathsPutSpringDefinitionScanningAlt() throws IOException, JSONException {
        OpenAPI result = scan(GreetingPutControllerMultiplePaths.class, Greeting.class, GreetingParam.class);
        assertJsonEquals("resource.testMultiplePathsSpringPutDefinitionScanning.json", result);
    }

    /**
     * This test a basic, no OpenApi annotations, hello world service
     *
     * @throws IOException
     * @throws JSONException
     */
    @Test
    void testBasicPutSpringDefinitionScanningWithServletContextJakarta() throws IOException, JSONException {
        OpenAPI result = scan(
                test.io.smallrye.openapi.runtime.scanner.resources.jakarta.GreetingPutControllerWithServletContext.class,
                Greeting.class);
        assertJsonEquals("resource.testBasicSpringPutDefinitionScanning.json", result);
    }

    /**
     * This test a basic, no OpenApi annotations, hello world service
     *
     * @throws IOException
     * @throws JSONException
     */
    @Test
    void testBasicPutSpringDefinitionScanningWithServletContextJavax() throws IOException, JSONException {
        OpenAPI result = scan(
                test.io.smallrye.openapi.runtime.scanner.resources.javax.GreetingPutControllerWithServletContext.class,
                Greeting.class);
        assertJsonEquals("resource.testBasicSpringPutDefinitionScanning.json", result);
    }

    /**
     * This test a basic, no OpenApi annotations, hello world service
     *
     * @throws IOException
     * @throws JSONException
     */
    @Test
    void testBasicDeleteSpringDefinitionScanning() throws IOException, JSONException {
        OpenAPI result = scan(GreetingDeleteController.class, Greeting.class);
        assertJsonEquals("resource.testBasicSpringDeleteDefinitionScanning.json", result);
    }

    /**
     * This test a basic, no OpenApi annotations, hello world service
     *
     * @throws IOException
     * @throws JSONException
     */
    @Test
    void testBasicDeleteSpringDefinitionScanningAlt() throws IOException, JSONException {
        OpenAPI result = scan(GreetingDeleteControllerAlt.class, Greeting.class);
        assertJsonEquals("resource.testBasicSpringDeleteDefinitionScanning.json", result);
    }
}

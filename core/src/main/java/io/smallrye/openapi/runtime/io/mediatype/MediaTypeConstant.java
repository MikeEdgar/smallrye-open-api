package io.smallrye.openapi.runtime.io.mediatype;

/**
 * Constants related to MediaType
 *
 * @see <a href="https://github.com/OAI/OpenAPI-Specification/blob/main/versions/3.0.3.md#mediaTypeObject">mediaTypeObject</a>
 *
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 * @author Eric Wittmann (eric.wittmann@gmail.com)
 */
public class MediaTypeConstant {

    public static final String PROP_EXAMPLE = "example";
    public static final String PROP_EXAMPLES = "examples";
    public static final String PROP_ENCODING = "encoding";
    public static final String PROP_SCHEMA = "schema";

    private MediaTypeConstant() {
    }
}

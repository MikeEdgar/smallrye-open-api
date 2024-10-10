package io.smallrye.openapi.api.models.tags;

import org.eclipse.microprofile.openapi.OASFactory;

import io.smallrye.openapi.api.util.VersionUtil;
import io.smallrye.openapi.internal.models.tags.Tag;

/**
 * @deprecated use {@link OASFactory#createTag()} instead.
 */
@Deprecated(since = "4.0", forRemoval = true)
public class TagImpl extends Tag { // NOSONAR

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (VersionUtil.compareMicroProfileVersion("3.0") < 0) {
            // TCK versions before MP OpenAPI release 3.0 check Tag instances are not equal
            return false;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        if (VersionUtil.compareMicroProfileVersion("3.0") < 0) {
            // TCK versions before MP OpenAPI release 3.0 check Tag instances are not equal
            return System.identityHashCode(this);
        }

        return super.hashCode();
    }
}

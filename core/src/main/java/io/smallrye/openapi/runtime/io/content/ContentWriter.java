package io.smallrye.openapi.runtime.io.content;

import java.util.Map;
import java.util.Set;

import org.eclipse.microprofile.openapi.models.media.Content;
import org.eclipse.microprofile.openapi.models.media.MediaType;

import com.fasterxml.jackson.databind.node.ObjectNode;

import io.smallrye.openapi.runtime.io.mediatype.MediaTypeWriter;

/**
 * Writing the Content to json
 *
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 * @author Eric Wittmann (eric.wittmann@gmail.com)
 */
public class ContentWriter {

    private ContentWriter() {
    }

    /**
     * Writes a {@link Content} to the JSON tree.
     *
     * @param parent the json parent node
     * @param model the Content model
     */
    public static void writeContent(ObjectNode parent, Content model) {
        if (model == null) {
            return;
        }
        ObjectNode node = parent.putObject(ContentConstant.PROP_CONTENT);
        if (model.getMediaTypes() != null) {
            Set<Map.Entry<String, MediaType>> entrySet = model.getMediaTypes().entrySet();
            for (Map.Entry<String, MediaType> entry : entrySet) {
                MediaTypeWriter.writeMediaType(node, entry.getValue(), entry.getKey());
            }
        }
    }

}

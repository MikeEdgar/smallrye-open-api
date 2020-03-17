package io.smallrye.openapi.runtime.io.extension;

import java.util.Map;

import org.eclipse.microprofile.openapi.models.Extensible;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.node.ObjectNode;

import io.smallrye.openapi.runtime.io.ObjectWriter;

/**
 * Writing the Extension to json
 * 
 * @see <a href="https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.3.md#specificationExtensions">specificationExtensions</a>
 * 
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 * @author Eric Wittmann (eric.wittmann@gmail.com)
 */
public class ExtensionWriter {
    private static final Logger LOG = Logger.getLogger(ExtensionWriter.class);

    private ExtensionWriter() {
    }

    /**
     * Writes extensions to the JSON tree.
     * 
     * @param node
     * @param model
     */
    public static void writeExtensions(ObjectNode node, Extensible<?> model) {
        Map<String, Object> extensions = model.getExtensions();
        if (extensions == null || extensions.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : extensions.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            ObjectWriter.writeObject(node, key, value);
        }
    }

}

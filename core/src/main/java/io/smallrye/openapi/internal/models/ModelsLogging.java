package io.smallrye.openapi.internal.models;

import java.lang.invoke.MethodHandles;

import org.jboss.logging.BasicLogger;
import org.jboss.logging.Logger;
import org.jboss.logging.annotations.LogMessage;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageLogger;

@MessageLogger(projectCode = "SROAP", length = 5)
interface ModelsLogging extends BasicLogger {
    ModelsLogging logger = Logger.getMessageLogger(MethodHandles.lookup(), ModelsLogging.class,
            ModelsLogging.class.getPackage().getName());

    @LogMessage(level = Logger.Level.INFO)
    @Message(id = 1500, value = "Cyclic object reference detected in OpenAPI model, skipping current node")
    void cylicReferenceDetected();

}

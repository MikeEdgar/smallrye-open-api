package io.smallrye.openapi.runtime.util;

import org.jboss.logging.BasicLogger;
import org.jboss.logging.Logger;
import org.jboss.logging.annotations.LogMessage;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageLogger;

@MessageLogger(projectCode = "SROAP", length = 5)
interface UtilLogging extends BasicLogger {
    UtilLogging logger = Logger.getMessageLogger(UtilLogging.class, UtilLogging.class.getPackage().getName());

    @LogMessage(level = Logger.Level.INFO)
    @Message(id = 8500, value = "Unable to obtain unsorted '%s' member list for class %s")
    void unsortedMemberRetrievalFailed(String memberType, String className);

}

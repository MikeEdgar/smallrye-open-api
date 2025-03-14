package test.io.smallrye.openapi.runtime.scanner.resources;

import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import test.io.smallrye.openapi.runtime.scanner.entities.Greeting;

/**
 * Spring.
 * This class tests if multiple paths are correctly implemented for post operations.
 */
@RestController
@RequestMapping(value = { "/greeting",
        "/hello" }, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class GreetingPostControllerMultiplePaths {

    // 1) Basic path var test
    @PostMapping({ "/greet1", "/greet2" })
    public Greeting greet(@RequestBody Greeting greeting) {
        return greeting;
    }

    // 2) ResponseEntity without a type specified
    @PostMapping("/greetWithResponse")
    @APIResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(ref = "#/components/schemas/Greeting")))
    public ResponseEntity<Greeting> greetWithResponse(@RequestBody Greeting greeting) {
        return ResponseEntity.ok(greeting);
    }

    // 3) ResponseEntity with a type specified (No JaxRS comparison)
    @PostMapping("/greetWithResponseTyped")
    public ResponseEntity<Greeting> greetWithResponseTyped(@RequestBody Greeting greeting) {
        return ResponseEntity.ok(greeting);
    }
}

package nl.bneijt.javapjson;

import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

public class JavapLOutput {

    public void toJsonOnto(JsonGenerator jsonOutput) throws MojoExecutionException {
        try {
            jsonOutput.writeString("TODO");
        } catch (JsonGenerationException e) {
            throw new MojoExecutionException("Could not serialize to Json", e);
        } catch (IOException e) {
            throw new MojoExecutionException("Error while outputting serialization", e);
        }
    }

}

package nl.bneijt.javapjson;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;


/**
 * Goal which touches a timestamp file.
 *
 * @goal touch
 *
 * @phase process-classes
 */
public class JavapJsonMojo
    extends AbstractMojo
{
    private static final String CLASS_EXTENSION = ".class";

    /**
     * Target directory
     * @parameter expression="${project.build.directory}"
     * @required
     */
    private File buildDirectory;

    /**
     * Directory containing the classes and resource files that should be packaged into the JAR.
     *
     * @parameter default-value="${project.build.outputDirectory}"
     * @required
     */
    private File outputDirectory;

    public void execute() throws MojoExecutionException
    {
        JsonFactory jsonFactory = new JsonFactory();
        if(! outputDirectory.exists()) {
            throw new MojoExecutionException("No build output directory found. Was looking at \"" + outputDirectory + "\"");
        }

        String jsonDirectory = buildDirectory.getPath() + File.separator + "javap-json";
        File jsonDirectoryFile = new File(jsonDirectory);
        if(!jsonDirectoryFile.exists()) {
            if(!jsonDirectoryFile.mkdir()) {
                throw new MojoExecutionException("Could not create output directory \"" + jsonDirectoryFile.getPath() + "\"");
            }
            getLog().debug("Created output directory \"" + jsonDirectoryFile.getPath() + "\"");
        }

        for (File classFile : FileUtils.listFiles(outputDirectory, new SuffixFileFilter(CLASS_EXTENSION), TrueFileFilter.INSTANCE)) {
            String output = runJavap(classFile);
            JavapLOutput parseL = JavapParser.parseL(output);

            File outputFile = new File(jsonDirectory + File.separator + "current.json");

            try {
                JsonGenerator jsonOutput = jsonFactory.createJsonGenerator(outputFile, JsonEncoding.UTF8);
                parseL.toJsonOnto(jsonOutput);

                //Move file into correct position
                File classDirectory = classFile.getParentFile();
                String classFileName = classFile.getName();
                String restOfDirectory = classDirectory.getPath().substring(outputDirectory.getPath().length());
                File jsonOutputFileDirectory = new File(jsonDirectory + restOfDirectory);
                File jsonOutputFile = new File(jsonOutputFileDirectory.getPath() + File.separator + classFileName.substring(0, classFileName.length()  - ".class".length()) + ".json");
                if(!jsonOutputFileDirectory.exists())
                    FileUtils.forceMkdir(jsonOutputFileDirectory);
                if(jsonOutputFile.exists()) {
                    FileUtils.deleteQuietly(jsonOutputFile);
                }
                FileUtils.moveFile(outputFile, jsonOutputFile);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                throw new MojoExecutionException("Unable to serialize javap output to Json", e);
            }
        }


    }

    private String runJavap(File classFile) throws MojoExecutionException {
        File classDirectory = classFile.getParentFile();
        String classFileName = classFile.getName();
        ProcessBuilder psBuilder = new ProcessBuilder("javap", "-l", classFileName.substring(0, classFileName.length() - CLASS_EXTENSION.length()));
        psBuilder.directory(classDirectory);
        Process process;
        try {
            process = psBuilder.start();
        } catch (IOException e1) {
            throw new MojoExecutionException("Could not start process builder", e1);
        }
        try {
            process.getOutputStream().close();
        } catch (IOException e) {
            throw new MojoExecutionException("Could not close output stream while executing javap");
        }
        try {
            byte[] buffer = new byte[1024];
            IOUtils.read(process.getInputStream(), buffer);
            return new String(buffer);
        } catch (IOException e) {
            throw new MojoExecutionException("Could not read all input from command javap while reading \"" + classFile + "\"");
        }
    }
}

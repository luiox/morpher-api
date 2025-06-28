package com.github.luiox.morpher.model.io;

import com.github.luiox.morpher.jar.IJarWriter;
import com.github.luiox.morpher.jar.JarUtil;
import com.github.luiox.morpher.model.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.util.jar.Manifest;

public class JarFileExporter implements IResourceExporter {
    private static final Logger logger = LoggerFactory.getLogger(JarFileExporter.class);

    IJarWriter writer;

    public JarFileExporter(@NotNull IJarWriter writer) {
        this.writer = writer;
    }

    public void writeResource(IJarWriter.IEntryWriter writer, IResource resource) {
        if (resource instanceof ClassResource classResource) {
            writer.writeEntry(classResource.getLocation(), classResource.get());
        } else if (resource instanceof ManifestResource manifestResource) {
            try {
                Manifest manifest = manifestResource.get();
                var byteArrayOutputStream = new ByteArrayOutputStream();
                manifest.write(byteArrayOutputStream);
                writer.writeEntry(JarUtil.ManifestFileName, byteArrayOutputStream.toByteArray());
            } catch (Exception e) {
                logger.error("Failed to write manifest, {}", e.getMessage());
            }

        } else if (resource instanceof UnknownResource unknownResource) {
            writer.writeEntry(unknownResource.getLocation(), unknownResource.get());
        } else {
            throw new IllegalArgumentException("Unknown resource type: " + resource.getClass().getName());
        }
    }

    @Override
    public void exportResource(@NotNull IResourceContainer container) throws Exception {
        writer.write(writer -> {
            for (var entry : container.entrySet()) {
                writeResource(writer, entry.getValue());
            }
        });
    }
}

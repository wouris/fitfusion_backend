package sk.kasv.mrazik.fitfusion.utils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class ExerciseReader {

    private final ResourceLoader resourceLoader;

    public ExerciseReader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Resource getResource() {
        return resourceLoader.getResource("classpath:" + "exercises/data.json");
    }
}


package com.otterly76.ott.util;

import com.mojang.serialization.DataResult;
import java.util.Arrays;
import java.util.List;

public class FileUtil {
    public static DataResult<List<String>> decomposePath(String path) {
        String[] parts = path.split("/");
        if (parts.length < 2) {
            return DataResult.error(() -> "Path has too few components: " + path);
        }
        return DataResult.success(Arrays.asList(parts));
    }
}
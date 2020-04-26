package org.digitalmind.buildingblocks.templating.templatingcore.util;

import org.apache.commons.io.FilenameUtils;

public class TemplateNamespaceUtil {
    public static String PATH_DELIMITER = "/";

    public static String getNamespace(String fullName, boolean includeDelimiter) {
        String namespaceNormalized = FilenameUtils.normalize(fullName.trim(), true);
        namespaceNormalized = namespaceNormalized.substring(0, namespaceNormalized.lastIndexOf(PATH_DELIMITER));
        if (includeDelimiter) {
            if (namespaceNormalized.length() != 1 && !namespaceNormalized.endsWith(PATH_DELIMITER)) {
                namespaceNormalized = namespaceNormalized + PATH_DELIMITER;
            }
        } else {
            if (namespaceNormalized.length() != 1 && namespaceNormalized.endsWith(PATH_DELIMITER)) {
                namespaceNormalized = namespaceNormalized.substring(0, namespaceNormalized.length() - 1);
            }
        }
        return namespaceNormalized;
    }

    public static String getName(String fullName) {
        String nameNormalized = fullName.trim();
        if (nameNormalized.endsWith(PATH_DELIMITER)) {
            nameNormalized = nameNormalized.substring(0, nameNormalized.length() - 1);
        }
        if (nameNormalized.lastIndexOf(PATH_DELIMITER) > 0) {
            nameNormalized = nameNormalized.substring(nameNormalized.lastIndexOf(PATH_DELIMITER) + 1);
        }
        return nameNormalized;
    }

    public static String getAbsolute(String path1, String path2) {
        String fullName = FilenameUtils.concat(path1.trim(), path2.trim());
        fullName = FilenameUtils.normalize(fullName, true);
        return fullName;
    }

    public static boolean hasNamespace(String fullName) {
        return !getNamespace(fullName, true).equalsIgnoreCase(PATH_DELIMITER);
    }

}

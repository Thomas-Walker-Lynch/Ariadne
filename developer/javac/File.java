package Ariadne;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class File {
  static boolean debug = false;

  public static Map<String, String> unpack_file_path(String file_fp) {
    if (debug) System.out.println("unpack_file_path::file_fp: " + file_fp);

    // Use java.io.File explicitly to avoid conflict with the custom File class
    java.io.File file = new java.io.File(file_fp);
    String parent_dp = (file.getParent() != null) ? file.getParent() : "";

    if (!parent_dp.isEmpty() && !parent_dp.endsWith(java.io.File.separator)) {
      parent_dp += java.io.File.separator;
    }

    String file_fn = file.getName();
    String file_fn_base = file_fn;
    String file_fn_ext = "";

    int last_index = file_fn.lastIndexOf('.');
    if (last_index > 0) {
      file_fn_base = file_fn.substring(0, last_index);
      if (last_index + 1 < file_fn.length()) {
        file_fn_ext = file_fn.substring(last_index + 1);
      }
    }

    Map<String, String> ret_val = new HashMap<>();
    ret_val.put("dp", parent_dp);
    ret_val.put("fn", file_fn);
    ret_val.put("fn_base", file_fn_base);
    ret_val.put("fn_ext", file_fn_ext);

    if (debug) System.out.println("unpack_file_path::ret_val: " + ret_val);

    return ret_val;
  }

  public static boolean file_exists_q(String fp_string) {
    Path fp_object = Paths.get(fp_string);
    return Files.exists(fp_object);
  }

  /*
    Given a target_fp and a list of list of dependency_fp.

    Returns false if either the target is newer than all the dependencies, or one
    of the specified files is missing. Otherwise returns true.
  */
  public static boolean newer_than_all(String target_fp_string, List<String> dependency_fp_list) throws IOException {
    Path target_fp_object = Paths.get(target_fp_string);
    if (!Files.exists(target_fp_object)) return false;

    long target_last_modified_time = Files.getLastModifiedTime(target_fp_object).toMillis();

    return dependency_fp_list.stream().allMatch(dependency_fp -> {
      try {
        Path dependency_fp_object = Paths.get(dependency_fp);
        if (!Files.exists(dependency_fp_object)) return false;
        long dependency_last_modified_time = Files.getLastModifiedTime(dependency_fp_object).toMillis();
        return target_last_modified_time > dependency_last_modified_time;
      } catch (IOException e) {
        return false;
      }
    });
  }

}
